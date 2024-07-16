package com.beanbeanjuice.cafebot.utility.helper;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformation;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.api.GitHubVersionEndpointWrapper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;

public class UpdateCheckHelper {

    private final CafeBot cafeBot;
    private final GitHubVersionEndpointWrapper githubVersionWrapper;
    private final ArrayList<Guild> guildsToNotify;

    public UpdateCheckHelper(final CafeBot cafeBot) {
        this.cafeBot = cafeBot;
        this.githubVersionWrapper = new GitHubVersionEndpointWrapper(this.cafeBot);
        this.guildsToNotify = new ArrayList<>();
    }

    public void checkUpdate() {
        this.githubVersionWrapper.getLatestVersionString().thenAcceptAsync((gitHubVersion) -> {
            if (!gitHubVersion.equals(this.cafeBot.getBotVersion())) {
                this.cafeBot.getLogger().log(UpdateCheckHelper.class, LogLevel.INFO, "No update found. Not sending update messages.");
                return;
            }

            this.cafeBot.getCafeAPI().getVersionsEndpoint().getCurrentCafeBotVersion().thenAcceptAsync((databaseVersion) -> {
                if (databaseVersion.equals(gitHubVersion)) {
                    this.cafeBot.getLogger().log(UpdateCheckHelper.class, LogLevel.INFO, "Versions match. Not sending update messages.");
                    return;
                }

                this.cafeBot.getLogger().log(UpdateCheckHelper.class, LogLevel.WARN, String.format("There is an update (%s)... sending update messages.", gitHubVersion));
                this.cafeBot.getCafeAPI().getVersionsEndpoint().updateCurrentCafeBotVersion(gitHubVersion);

                this.githubVersionWrapper.getVersion(gitHubVersion).thenAcceptAsync((embed) -> {
                    this.guildsToNotify.addAll(this.cafeBot.getJDA().getGuilds());
                    handleUpdateNotifications();
                });
            });
        });
    }

    private void handleUpdateNotifications() {
        this.cafeBot.getCafeAPI().getGuildsEndpoint().getAllGuildInformation().thenAcceptAsync((guildsMap) -> {
            this.guildsToNotify.forEach((guild) -> {
                if (!guildsMap.containsKey(guild.getId())) return;

                handleGuildUpdate(guild, guildsMap.get(guild.getId()));
            });
        });
    }

    private void handleGuildUpdate(final Guild guild, final GuildInformation information) {
        if (!Boolean.parseBoolean(information.getSetting(GuildInformationType.NOTIFY_ON_UPDATE))) return;

        String updateChannelID = information.getSetting(GuildInformationType.UPDATE_CHANNEL_ID);

        this.githubVersionWrapper.getVersion(this.cafeBot.getBotVersion())
                .thenAcceptAsync((versionEmbed) -> sendVersionEmbed(guild, updateChannelID, versionEmbed));
    }

    private void sendVersionEmbed(final Guild guild, final String updateChannelID, final MessageEmbed embed) {
        guild.retrieveOwner().queue((owner) -> {
            TextChannel updateChannel = guild.getTextChannelById(updateChannelID);
            if (updateChannel == null) {
                try { updateChannel = guild.getDefaultChannel().asTextChannel(); }
                catch (NullPointerException | IllegalStateException e) { return; }
            }

            updateChannel.sendMessage(String.format("%s there was an update!", owner.getAsMention()))
                    .addEmbeds(embed)
                    .queue();
        });
    }

}
