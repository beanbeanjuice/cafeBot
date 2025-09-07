package com.beanbeanjuice.cafebot.utility.handlers;

import com.beanbeanjuice.cafebot.api.wrapper.api.enums.CustomChannelType;
import com.beanbeanjuice.cafebot.api.wrapper.type.CustomChannel;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.api.GitHubVersionEndpointWrapper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class BotUpdateHandler {

    private final CafeBot cafeBot;
    private final GitHubVersionEndpointWrapper githubVersionWrapper;

    public BotUpdateHandler(CafeBot cafeBot) {
        this.cafeBot = cafeBot;
        this.githubVersionWrapper = new GitHubVersionEndpointWrapper(this.cafeBot);
    }

    public void checkUpdate() {
        this.githubVersionWrapper.getLatestVersionString().thenAcceptAsync((gitHubVersion) -> {
            if (!gitHubVersion.equals(this.cafeBot.getBotVersion())) {
                this.cafeBot.getLogger().log(BotUpdateHandler.class, LogLevel.INFO, "No update found. Not sending update messages.");
                return;
            }

            this.cafeBot.getCafeAPI().getBotSettingsApi().getBotVersion().thenAccept((dbVersion) -> {
                if (dbVersion.equals(gitHubVersion)) {
                    this.cafeBot.getLogger().log(BotUpdateHandler.class, LogLevel.INFO, "Versions match. Not sending update messages.");
                    return;
                }

                this.cafeBot.getLogger().log(BotUpdateHandler.class, LogLevel.WARN, String.format("There is an update (%s)... sending update messages.", gitHubVersion));
                this.cafeBot.getCafeAPI().getBotSettingsApi().updateBotVersion(gitHubVersion);

                this.githubVersionWrapper.getVersion(gitHubVersion).thenAcceptAsync((embed) -> {
                    handleUpdateNotifications();
                });
            });
        });
    }

    private void handleUpdateNotifications() {
        CompletableFuture<MessageEmbed> versionEmbedFuture = this.githubVersionWrapper.getVersion(this.cafeBot.getBotVersion());
        CompletableFuture<Map<String, CustomChannel>> customChannelsFuture = this.cafeBot.getCafeAPI().getCustomChannelApi().getCustomChannels(CustomChannelType.UPDATE_NOTIFICATIONS);

        versionEmbedFuture.thenAcceptBoth(customChannelsFuture, (versionEmbed, customChannels) -> {
            customChannels.forEach((guildId, customChannel) -> {
                handleGuildUpdate(guildId, customChannel.getChannelId(), versionEmbed);
            });
        });
    }

    private void handleGuildUpdate(String guildId, String channelId, MessageEmbed versionEmbed) {
        Guild guild = this.cafeBot.getShardManager().getGuildById(guildId);
        if (guild == null) return;

        TextChannel channel = guild.getTextChannelById(channelId);
        if (channel == null) return;

        sendVersionEmbed(guild, channel, versionEmbed);
    }

    private void sendVersionEmbed(Guild guild, TextChannel channel, MessageEmbed embed) {
        guild.retrieveOwner().queue((owner) -> {
            channel.sendMessage(String.format("%s there was an update!", owner.getAsMention()))
                    .addEmbeds(embed)
                    .queue();
        });
    }

}
