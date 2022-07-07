package com.beanbeanjuice;

import com.beanbeanjuice.utility.section.cafe.BeanCoinDonationHandler;
import com.beanbeanjuice.utility.section.cafe.MenuHandler;
import com.beanbeanjuice.utility.section.fun.BirthdayHandler;
import com.beanbeanjuice.utility.section.moderation.poll.PollHandler;
import com.beanbeanjuice.utility.helper.CountingHelper;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.command.CommandHandler;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.listener.MessageListener;
import com.beanbeanjuice.utility.listener.ServerListener;
import com.beanbeanjuice.utility.logging.LogLevel;
import com.beanbeanjuice.utility.logging.LogManager;
import com.beanbeanjuice.utility.section.moderation.raffle.RaffleHandler;
import io.github.beanbeanjuice.cafeapi.CafeAPI;
import io.github.beanbeanjuice.cafeapi.requests.RequestLocation;
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

/**
 * The main {@link JDA bot} class.
 *
 * @author beanbeanjuice
 */
public class Bot {

    // Bot Items
    public static final String BOT_VERSION = System.getenv("CAFEBOT_VERSION");
    private static final String BOT_TOKEN = System.getenv("CAFEBOT_TOKEN");
    public static final String BOT_USER_AGENT = "java:com.beanbeanjuice.cafeBot:" + BOT_VERSION;
    private static JDA bot;

    // API
    private static CafeAPI cafeAPI;

    // Internal Items
    private static LogManager logger;
    private static Guild homeGuild;
    private static TextChannel homeGuildLogChannel;
    private static final String HOME_GUILD_ID = System.getenv("CAFEBOT_GUILD_ID");
    private static final String HOME_GUILD_LOG_CHANNEL_ID = System.getenv("CAFEBOT_GUILD_LOG_CHANNEL_ID");
    private static final String HOME_GUILD_WEBHOOK_URL = System.getenv("CAFEBOT_GUILD_WEBHOOK_URL");

    // Handlers
    private static CommandHandler commandHandler;
    private static GuildHandler guildHandler;
    private static MenuHandler menuHandler;
    private static PollHandler pollHandler;
    private static BirthdayHandler birthdayHandler;

    // Helpers
    private static CountingHelper countingHelper;
    private static BeanCoinDonationHandler beanCoinDonationHandler;

    // Additional Items
    public static int commandsRun = 0;
    public static final String DISCORD_AVATAR_URL = "https://cdn.beanbeanjuice.com/images/cafeBot/cafeBot.gif";

    public static void main(String[] args) throws LoginException, InterruptedException {
        logger = new LogManager("cafeBot Logging System", homeGuildLogChannel, "logs/");
        Helper.startCafeAPIRefreshTimer(RequestLocation.BETA);  // TODO: Change in production.

        logger.addWebhookURL(HOME_GUILD_WEBHOOK_URL);
        logger.log(Bot.class, LogLevel.OKAY, "Starting bot!", true, false);

        bot = JDABuilder.createDefault(BOT_TOKEN)
                .setActivity(Activity.playing("The barista is starting..."))
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

        guildHandler = new GuildHandler();
        homeGuild = bot.getGuildById(HOME_GUILD_ID);
        homeGuildLogChannel = homeGuild.getTextChannelById(HOME_GUILD_LOG_CHANNEL_ID);

        logger.log(Bot.class, LogLevel.LOADING, "Adding commands...", false, false);
        commandHandler = new CommandHandler(bot);
        bot.addEventListener(
                commandHandler,
                new ServerListener(), // Listening for Guild Joins/Leaves
                new MessageListener() // Listening for specific messages TODO: Add "AI" support.
        );

        logger.setLogChannel(homeGuildLogChannel);
        logger.log(Bot.class, LogLevel.INFO, "Enabled Discord Logging...", true, true);

        // Helpers that need to be instantiated.
        countingHelper = new CountingHelper();
        beanCoinDonationHandler = new BeanCoinDonationHandler();
        menuHandler = new MenuHandler();  // TODO: Not sure if this is needed. Static class?
        pollHandler = new PollHandler();  // TODO: Not sure if this is needed. Static class?
        RaffleHandler.start();
        birthdayHandler = new BirthdayHandler();  // TODO: Not sure if this is needed. Static class?

        bot.getPresence().setStatus(OnlineStatus.ONLINE);
        updateGuildPresence();
        logger.log(Bot.class, LogLevel.OKAY, "The bot is online!");
    }

    /**
     * @return The current {@link JDA bot}.
     */
    @NotNull
    public static JDA getBot() {
        return bot;
    }

    /**
     * @return The current {@link LogManager}.
     */
    @NotNull
    public static LogManager getLogger() {
        return logger;
    }

    /**
     * @return The current {@link CafeAPI}.
     */
    @NotNull
    public static CafeAPI getCafeAPI() {
        return cafeAPI;
    }

    /**
     * Sets the current {@link CafeAPI}.
     * @param newCafeAPI The new {@link CafeAPI}.
     */
    public static void setCafeAPI(@NotNull CafeAPI newCafeAPI) {
        cafeAPI = newCafeAPI;
    }

    /**
     * @return The current {@link CommandHandler}.
     */
    @NotNull
    public static CommandHandler getCommandHandler() {
        return commandHandler;
    }

    /**
     * Updates the presence for the {@link JDA}.
     */
    public static void updateGuildPresence() {
        bot.getPresence().setActivity(Activity.playing("cafeBot " + BOT_VERSION + " - Currently in " + bot.getGuilds().size() + " servers!"));
    }

    /**
     * @return The current {@link GuildHandler}.
     */
    @NotNull
    public static GuildHandler getGuildHandler() {
        return guildHandler;
    }

    /**
     * @return The current {@link CountingHelper}.
     */
    @NotNull
    public static CountingHelper getCountingHelper() {
        return countingHelper;
    }

    /**
     * @return The current {@link BeanCoinDonationHandler}.
     */
    @NotNull
    public static BeanCoinDonationHandler getBeanCoinDonationHandler() {
        return beanCoinDonationHandler;
    }

    /**
     * @return The current {@link PollHandler}.
     */
    @NotNull
    public static PollHandler getPollHandler() {
        return pollHandler;
    }

    /**
     * @return The current {@link BirthdayHandler}.
     */
    @NotNull
    public static BirthdayHandler getBirthdayHandler() {
        return birthdayHandler;
    }

}