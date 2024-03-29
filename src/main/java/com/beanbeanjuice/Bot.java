package com.beanbeanjuice;

import com.beanbeanjuice.utility.api.GitHubUpdateHelper;
import com.beanbeanjuice.utility.command.CommandAutoCompleteHandler;
import com.beanbeanjuice.utility.handler.CountingHandler;
import com.beanbeanjuice.utility.handler.VoiceChatRoleBindHandler;
import com.beanbeanjuice.utility.listener.*;
import com.beanbeanjuice.utility.section.cafe.BeanCoinDonationHandler;
import com.beanbeanjuice.utility.section.cafe.MenuHandler;
import com.beanbeanjuice.utility.section.fun.BirthdayHandler;
import com.beanbeanjuice.utility.section.moderation.poll.PollHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.command.CommandHandler;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.logging.LogLevel;
import com.beanbeanjuice.utility.logging.LogManager;
import com.beanbeanjuice.utility.section.moderation.raffle.RaffleHandler;
import com.beanbeanjuice.utility.section.settings.DailyChannelHandler;
import com.beanbeanjuice.utility.section.twitch.TwitchHandler;
import com.beanbeanjuice.cafeapi.CafeAPI;
import com.beanbeanjuice.cafeapi.requests.RequestLocation;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
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
    public static final String TWITCH_ACCESS_TOKEN = System.getenv("CAFEBOT_TWITCH_ACCESS_TOKEN");
    private static final RequestLocation location = RequestLocation.valueOf(System.getenv("CAFEBOT_REQUEST_LOCATION"));
    private static JDA bot;

    // API
    private static CafeAPI cafeAPI;

    // Internal Items
    private static LogManager logger;
    private static final String HOME_GUILD_ID = System.getenv("CAFEBOT_GUILD_ID");
    private static final String HOME_GUILD_LOG_CHANNEL_ID = System.getenv("CAFEBOT_GUILD_LOG_CHANNEL_ID");
    private static final String HOME_GUILD_WEBHOOK_URL = System.getenv("CAFEBOT_GUILD_WEBHOOK_URL");

    // Handlers
    private static CommandHandler commandHandler;
    private static AIResponseListener aiResponseListener;

    // Additional Items
    public static int commandsRun = 0;
    public static final String DISCORD_AVATAR_URL = "https://cdn.beanbeanjuice.com/images/cafeBot/cafeBot.gif";

    public Bot() throws LoginException, InterruptedException {
        logger = new LogManager("cafeBot Logging System", HOME_GUILD_ID, HOME_GUILD_LOG_CHANNEL_ID, "logs/");
        Helper.startCafeAPIRefreshTimer(location);

        logger.addWebhookURL(HOME_GUILD_WEBHOOK_URL);
        logger.log(Bot.class, LogLevel.OKAY, "Starting bot!", true, false);

        bot = JDABuilder.createDefault(BOT_TOKEN)
                .setActivity(Activity.playing("The barista is starting..."))
                .setStatus(OnlineStatus.IDLE)
                .enableIntents(
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT
                )
                .enableCache(
                        CacheFlag.EMOJI
                )
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .build()
                .awaitReady();

        logger.enableDiscordLogging();

        TwitchHandler.start();  // Start twitch handler prior to guild handler.
        GuildHandler.start();  // Starting hte guild handler.

        logger.log(Bot.class, LogLevel.LOADING, "Adding commands...", false, false);

        commandHandler = new CommandHandler(bot);
        aiResponseListener = new AIResponseListener();

        bot.addEventListener(
                commandHandler,
                new ServerListener(),  // Listening for Guild Joins/Leaves
                new MessageListener(),  // Listening for specific messages
                new MessageDeleteListener(),  // Listening for message deletions
                new WelcomeListener(),  // Listening for user joins for a guild.
                new GoodbyeListener(),  // Listening for user leaves for a guild.
                aiResponseListener,  // Listening for messages.
                new VoiceChatRoleBindListener(),  // Listening for voice joins/leaves
                new CommandAutoCompleteHandler()  // Listens for auto complete interactions
        );

        logger.log(Bot.class, LogLevel.INFO, "Enabled Discord Logging...", true, true);

        // Helpers that need to be instantiated.
        CountingHandler.start();
        BeanCoinDonationHandler.start();
        MenuHandler.start();
        PollHandler.start();
        RaffleHandler.start();
        BirthdayHandler.start();
        DailyChannelHandler.start();
        VoiceChatRoleBindHandler.start();

        bot.getPresence().setStatus(OnlineStatus.ONLINE);
        Helper.startBioUpdateTimer();
        logger.log(Bot.class, LogLevel.OKAY, "The bot is online!");

        new GitHubUpdateHelper().start();  // Notify Guilds of Update
        Helper.startUpdateTimer();
    }

    public static void main(String[] args) throws LoginException, InterruptedException {
        new Bot();
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
     * @return The current {@link AIResponseListener}.
     */
    @NotNull
    public static AIResponseListener getAIResponseListener() {
        return aiResponseListener;
    }

}
