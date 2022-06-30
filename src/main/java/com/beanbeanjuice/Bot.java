package com.beanbeanjuice;

import com.beanbeanjuice.utility.Helper;
import com.beanbeanjuice.utility.logging.LogLevel;
import com.beanbeanjuice.utility.logging.LogManager;
import com.beanbeanjuice.utility.time.Time;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class Bot {

    // Bot Items
    private static final String BOT_VERSION = System.getenv("CAFEBOT_VERSION");
    private static final String BOT_TOKEN = System.getenv("CAFEBOT_TOKEN");
    private static JDA bot;

    // Internal Items
    private static LogManager logger;
    private static Guild homeGuild;
    private static TextChannel homeGuildLogChannel;
    private static final String HOME_GUILD_ID = System.getenv("CAFEBOT_GUILD_ID");
    private static final String HOME_GUILD_LOG_CHANNEL_ID = System.getenv("CAFEBOT_GUILD_LOG_CHANNEL_ID");
    private static final String HOME_GUILD_WEBHOOK_URL = System.getenv("CAFEBOT_GUILD_WEBHOOK_URL");

    // Additional Items
    private static int commandsRun = 0;
    private static final String DISCORD_AVATAR_URL = "https://cdn.beanbeanjuice.com/images/cafeBot/cafeBot.gif";

    public static void main(String[] args) throws LoginException, InterruptedException {
        logger = new LogManager("cafeBot Logging System", homeGuildLogChannel, "logs/");
        logger.addWebhookURL(HOME_GUILD_WEBHOOK_URL);
        logger.log(Bot.class, LogLevel.OKAY, "Starting bot!", true, false);

        bot = JDABuilder.createDefault(BOT_TOKEN)
                .setActivity(Activity.playing("cafeBot " + BOT_VERSION)) // TODO: Eventually update with number of servers.
                .setStatus(OnlineStatus.IDLE)
                .enableIntents(
                        GatewayIntent.GUILD_BANS,
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.DIRECT_MESSAGES
                )
                .enableCache(
                        CacheFlag.EMOJI
                )
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .build()
                .awaitReady();

        homeGuild = bot.getGuildById(HOME_GUILD_ID);
        homeGuildLogChannel = homeGuild.getTextChannelById(HOME_GUILD_LOG_CHANNEL_ID);

        logger.setLogChannel(homeGuildLogChannel);
        logger.log(Bot.class, LogLevel.LOADING, "Enabled Discord Logging...", true, true);

        bot.getPresence().setStatus(OnlineStatus.ONLINE);
        logger.log(Bot.class, LogLevel.OKAY, "The bot is online!");
    }

    /**
     * @return The current {@link LogManager}.
     */
    @NotNull
    public LogManager getLogger() {
        return logger;
    }
}