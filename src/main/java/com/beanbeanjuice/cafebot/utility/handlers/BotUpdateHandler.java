package com.beanbeanjuice.cafebot.utility.handlers;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.type.CustomChannel;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.api.GitHubVersionEndpointWrapper;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class BotUpdateHandler {

    private final CafeBot cafeBot;
    private final GitHubVersionEndpointWrapper githubVersionWrapper;

    private boolean hasUpdate;
    private MessageEmbed updateEmbed;
    private String githubVersion;

    public BotUpdateHandler(CafeBot cafeBot) throws ExecutionException, InterruptedException {
        this.cafeBot = cafeBot;
        this.githubVersionWrapper = new GitHubVersionEndpointWrapper(this.cafeBot);

        hasUpdate = false;
        checkUpdate();

        this.cafeBot.getLogger().log(this.getClass(), LogLevel.INFO, "Finished caching GitHub version.", false, false);
    }

    // We want to use blocking calls here because we need this data before the bot starts.
    private void checkUpdate() throws ExecutionException, InterruptedException {
        this.githubVersion = this.githubVersionWrapper.getLatestVersionString().get();

        if (!this.githubVersion.equals(this.cafeBot.getBotVersion())) {
            this.cafeBot.getLogger().log(BotUpdateHandler.class, LogLevel.INFO, "No update found. Not sending update messages.");
            return;
        }

        String dbVersion = this.cafeBot.getCafeAPI().getBotSettingsApi().getBotVersion().get();

        if (dbVersion.equals(githubVersion)) {
            this.cafeBot.getLogger().log(BotUpdateHandler.class, LogLevel.INFO, "Versions match. Not sending update messages.");
            return;
        }

        this.cafeBot.getLogger().log(BotUpdateHandler.class, LogLevel.WARN, String.format("There is an update (%s)... queueing update messages.", this.githubVersion));
        this.cafeBot.getCafeAPI().getBotSettingsApi().updateBotVersion(githubVersion);
        this.updateEmbed = this.githubVersionWrapper.getVersion(this.githubVersion).get();
        hasUpdate = true;
    }

    public void sendUpdateNotifications(int shardId) {
        if (!hasUpdate) return;

        cafeBot.getLogger().log(this.getClass(), LogLevel.INFO, String.format("Sending Updates for Shard ID#%d", shardId), true, false);

        CompletableFuture<Map<String, CustomChannel>> customChannelsFuture = this.cafeBot.getCafeAPI().getCustomChannelApi().getCustomChannels(CustomChannelType.UPDATE_NOTIFICATIONS);
        customChannelsFuture.thenAccept((customChannels) -> {
            customChannels.forEach((guildId, customChannel) -> {
                handleGuildUpdate(guildId, customChannel.getChannelId(), updateEmbed);
            });
        });
    }

    private void handleGuildUpdate(String guildId, String channelId, MessageEmbed versionEmbed) {
        Guild guild = this.cafeBot.getShardManager().getGuildById(guildId);
        if (guild == null) return; // Bot no longer in guild.

        TextChannel channel = guild.getTextChannelById(channelId);
        if (channel == null) {
            this.cafeBot.getLogger().logToGuild(guild, Helper.errorEmbed(
                    "Update Notification",
                    "I tried to send an update notification, but it seems your update channel is improperly set. Please set it using `/channel set` or removing it by using `/channel remove`."
            ));
            return;
        }

        sendVersionEmbed(guild, channel, versionEmbed);
    }

    private void sendVersionEmbed(Guild guild, TextChannel channel, MessageEmbed embed) {
        Member self = guild.getSelfMember();

        if (!self.hasPermission(channel, Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND)) {
            cafeBot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Sending Update Message to %s", guild.getName()), false, false);
            cafeBot.getLogger().logToGuild(guild, Helper.errorEmbed("Error Sending Update", "There was an update, but I was unable to send it in the update channel. Use `/channel set` to set it up!"));
            return;
        }

        guild.retrieveOwner().queue((owner) -> {
            if (owner == null) return;

            channel.sendMessage(String.format("%s, there was an update!", owner.getAsMention()))
                    .addEmbeds(embed)
                    .queue();
        });
    }

}
