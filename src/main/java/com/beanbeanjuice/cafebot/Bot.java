package com.beanbeanjuice.cafebot;

import com.beanbeanjuice.cafebot.utility.api.GitHubUpdateHelper;
import com.beanbeanjuice.cafebot.utility.command.CommandAutoCompleteHandler;
import com.beanbeanjuice.cafebot.utility.handler.CountingHandler;
import com.beanbeanjuice.cafebot.utility.handler.VoiceChatRoleBindHandler;
import com.beanbeanjuice.cafebot.utility.listener.*;
import com.beanbeanjuice.cafebot.utility.section.cafe.BeanCoinDonationHandler;
import com.beanbeanjuice.cafebot.utility.section.cafe.MenuHandler;
import com.beanbeanjuice.cafebot.utility.section.fun.BirthdayHandler;
import com.beanbeanjuice.cafebot.utility.section.moderation.poll.PollHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.command.CommandHandler;
import com.beanbeanjuice.cafebot.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafebot.utility.logging.LogManager;
import com.beanbeanjuice.cafebot.utility.section.moderation.raffle.RaffleHandler;
import com.beanbeanjuice.cafebot.utility.section.settings.DailyChannelHandler;
import com.beanbeanjuice.cafebot.utility.section.twitch.TwitchHandler;
import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The main {@link JDA bot} class.
 *
 * @author beanbeanjuice
 */
public class Bot {

    // Bot Items
    public static final String BOT_VERSION = getVersion();
    private static final String BOT_TOKEN = System.getenv("CAFEBOT_TOKEN");
    public static final String BOT_USER_AGENT = "java:com.beanbeanjuice.cafeBot:" + BOT_VERSION;
    public static final String TWITCH_ACCESS_TOKEN = System.getenv("CAFEBOT_TWITCH_ACCESS_TOKEN");
    private static final RequestLocation location = RequestLocation.valueOf(System.getenv("CAFEBOT_REQUEST_LOCATION"));
    @Getter private static JDA bot;

    // API
    @Getter @Setter private static CafeAPI cafeAPI;

    // Internal Items
    @Getter private static LogManager logger;
    private static final String HOME_GUILD_ID = System.getenv("CAFEBOT_GUILD_ID");
    private static final String HOME_GUILD_LOG_CHANNEL_ID = System.getenv("CAFEBOT_GUILD_LOG_CHANNEL_ID");
    private static final String HOME_GUILD_WEBHOOK_URL = System.getenv("CAFEBOT_GUILD_WEBHOOK_URL");

    // Handlers
    @Getter private static CommandHandler commandHandler;
    @Getter private static AIResponseListener aiResponseListener;

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

    private static String getVersion() {
        String resourceName = "cafebot.properties"; // could also be a constant
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) { props.load(resourceStream); }
        catch (IOException e) { throw new RuntimeException(e); }

        return props.get("version").toString();
    }

}
