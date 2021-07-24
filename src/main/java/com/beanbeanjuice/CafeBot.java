package com.beanbeanjuice;

import com.beanbeanjuice.command.cafe.BalanceCommand;
import com.beanbeanjuice.command.cafe.MenuCommand;
import com.beanbeanjuice.command.cafe.OrderCommand;
import com.beanbeanjuice.command.cafe.ServeCommand;
import com.beanbeanjuice.command.fun.*;
import com.beanbeanjuice.command.games.*;
import com.beanbeanjuice.command.generic.*;
import com.beanbeanjuice.command.interaction.*;
import com.beanbeanjuice.command.moderation.SetCountingChannelCommand;
import com.beanbeanjuice.command.moderation.*;
import com.beanbeanjuice.command.moderation.SetBirthdayChannelCommand;
import com.beanbeanjuice.command.moderation.mute.MuteCommand;
import com.beanbeanjuice.command.moderation.mute.UnMuteCommand;
import com.beanbeanjuice.command.moderation.SetPollChannelCommand;
import com.beanbeanjuice.command.moderation.SetRaffleChannelCommand;
import com.beanbeanjuice.command.moderation.SetUpdateChannelCommand;
import com.beanbeanjuice.command.moderation.voicebind.GetVoiceRoleBindsCommand;
import com.beanbeanjuice.command.moderation.voicebind.VoiceRoleBindCommand;
import com.beanbeanjuice.command.moderation.welcome.EditWelcomeMessageCommand;
import com.beanbeanjuice.command.moderation.welcome.SetWelcomeChannelCommand;
import com.beanbeanjuice.command.music.*;
import com.beanbeanjuice.command.social.VentCommand;
import com.beanbeanjuice.command.twitch.*;
import com.beanbeanjuice.utility.helper.DailyChannelHelper;
import com.beanbeanjuice.utility.listener.AIResponseListener;
import com.beanbeanjuice.utility.listener.WelcomeListener;
import com.beanbeanjuice.utility.sections.fun.birthday.BirthdayHandler;
import com.beanbeanjuice.utility.sections.cafe.MenuHandler;
import com.beanbeanjuice.utility.sections.cafe.ServeHandler;
import com.beanbeanjuice.utility.command.CommandManager;
import com.beanbeanjuice.utility.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.CountingHelper;
import com.beanbeanjuice.utility.helper.GeneralHelper;
import com.beanbeanjuice.utility.helper.VersionHelper;
import com.beanbeanjuice.utility.sections.games.connectfour.ConnectFourHandler;
import com.beanbeanjuice.utility.sections.games.tictactoe.TicTacToeHandler;
import com.beanbeanjuice.utility.sections.interaction.InteractionHandler;
import com.beanbeanjuice.utility.listener.Listener;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.logger.LogManager;
import com.beanbeanjuice.utility.sections.fun.poll.PollHandler;
import com.beanbeanjuice.utility.sections.fun.raffle.RaffleHandler;
import com.beanbeanjuice.utility.sections.moderation.voicechat.VoiceChatListener;
import com.beanbeanjuice.utility.sections.moderation.voicechat.VoiceChatRoleBindHandler;
import com.beanbeanjuice.utility.sections.moderation.welcome.WelcomeHandler;
import com.beanbeanjuice.utility.sections.music.custom.CustomSongManager;
import com.beanbeanjuice.utility.sections.social.vent.VentHandler;
import com.beanbeanjuice.utility.sql.SQLServer;
import com.beanbeanjuice.utility.sections.twitch.TwitchHandler;
import com.wrapper.spotify.SpotifyApi;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.discordbots.api.client.DiscordBotListAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.security.auth.login.LoginException;

/**
 * The main {@link CafeBot} class.
 *
 * @author beanbeanjuice
 */
@SpringBootApplication
public class CafeBot {

    // General Bot Info
    private static final String BOT_VERSION = System.getenv("CAFEBOT_VERSION");
    private static final String BOT_TOKEN = System.getenv("CAFEBOT_TOKEN");
    private static final String BOT_USER_AGENT = "java:com.beanbeanjuice.cafeBot:" + BOT_VERSION;
    private static JDA jda;
    private static JDABuilder jdaBuilder;
    private static final String DISCORD_AVATAR_URL = "http://cdn.beanbeanjuice.com/images/cafeBot/cafeBot.gif";
    private static int commandsRun = 0;

    // Logging Stuff
    private static Guild homeGuild;
    private static final String HOME_GUILD_ID = System.getenv("CAFEBOT_GUILD_ID");
    private static TextChannel homeGuildLogChannel;
    private static final String HOME_GUILD_LOG_CHANNEL_ID = System.getenv("CAFEBOT_GUILD_LOG_CHANNEL_ID");
    private static final String HOME_GUILD_WEBHOOK_URL = System.getenv("CAFEBOT_GUILD_WEBHOOK_URL");

    private static final String BOT_PREFIX = "!!";

    // Guild Manager Stuff
    private static GuildHandler guildHandler;

    // Command Stuff
    private static CommandManager commandManager;

    // Spotify Stuff
    private static SpotifyApi spotifyApi;
    private static final String SPOTIFY_API_CLIENT_ID = System.getenv("CAFEBOT_SPOTIFY_ID");
    private static final String SPOTIFY_API_CLIENT_SECRET = System.getenv("CAFEBOT_SPOTIFY_SECRET");

    // Twitch Stuff
    private static final String TWITCH_ACCESS_TOKEN = System.getenv("CAFEBOT_TWITCH_ACCESS_TOKEN");
    private static TwitchHandler twitchHandler;

    // SQL Stuff
    private static SQLServer sqlServer;
    private static final String SQL_URL = System.getenv("CAFEBOT_MYSQL_URL");
    private static final String SQL_PORT = System.getenv("CAFEBOT_MYSQL_PORT");
    private static final String SQL_USERNAME = System.getenv("CAFEBOT_MYSQL_USERNAME");
    private static final String SQL_PASSWORD = System.getenv("CAFEBOT_MYSQL_PASSWORD");
    private static final boolean SQL_ENCRYPT = Boolean.parseBoolean(System.getenv("CAFEBOT_MYSQL_ENCRYPT"));

    // Top.GG API
    private static DiscordBotListAPI topGGAPI;
    private static final String TOPGG_ID = System.getenv("CAFEBOT_TOPGG_ID");
    private static final String TOPGG_TOKEN = System.getenv("CAFEBOT_TOPGG_TOKEN");

    // Logging
    private static LogManager logManager;

    // Other
    private static GeneralHelper generalHelper;

    // Version Helper
    private static VersionHelper versionHelper;

    // Counting Helper
    private static CountingHelper countingHelper;

    // Cafe Stuff
    private static ServeHandler serveHandler;
    private static MenuHandler menuHandler;

    // Poll/Raffle Stuff
    private static PollHandler pollHandler;
    private static RaffleHandler raffleHandler;

    // Interaction Stuff
    private static InteractionHandler interactionHandler;

    // Birthday Stuff
    private static BirthdayHandler birthdayHandler;

    // Game Stuff
    private static TicTacToeHandler ticTacToeHandler;
    private static ConnectFourHandler connectFourHandler;

    // Welcome Stuff
    private static WelcomeHandler welcomeHandler;
    private static WelcomeListener welcomeListener;

    // Song Stuff
    private static CustomSongManager customSongManager;

    // Social Stuff
    private static VentHandler ventHandler;

    // Voice Chat Role Bind Handler
    private static VoiceChatRoleBindHandler voiceChatRoleBindHandler;

    // Daily Stuff
    private static DailyChannelHelper dailyChannelHelper;

    public CafeBot() throws LoginException, InterruptedException {
        generalHelper = new GeneralHelper();
        logManager = new LogManager("cafeBot Logging System", homeGuildLogChannel, "logs/");

        countingHelper = new CountingHelper();
        twitchHandler = new TwitchHandler();
        sqlServer = new SQLServer(SQL_URL, SQL_PORT, SQL_ENCRYPT, SQL_USERNAME, SQL_PASSWORD);

        // APIs
        sqlServer.startConnection();
        generalHelper.startMySQLRefreshTimer();

        logManager.log(CafeBot.class, LogLevel.OKAY, "Connecting to the Top.GG API", true, false);
        topGGAPI = new DiscordBotListAPI.Builder()
                .token(TOPGG_TOKEN)
                .botId(TOPGG_ID)
                .build();

        ventHandler = new VentHandler();

        logManager.addWebhookURL(HOME_GUILD_WEBHOOK_URL);
        logManager.log(CafeBot.class, LogLevel.OKAY, "Starting bot!", true, false);

        jdaBuilder = JDABuilder.createDefault(BOT_TOKEN);
        jdaBuilder.setActivity(Activity.playing("The barista is starting up..."));
        jdaBuilder.setStatus(OnlineStatus.IDLE);

        jdaBuilder.enableIntents(
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_BANS,
                GatewayIntent.GUILD_EMOJIS
//                GatewayIntent.GUILD_MEMBERS,
//                GatewayIntent.DIRECT_MESSAGES
        );
        jdaBuilder.enableCache(
                CacheFlag.EMOTE
//                CacheFlag.VOICE_STATE
        );
//        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
        jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);

        serveHandler = new ServeHandler();
        menuHandler = new MenuHandler();

        // Listeners and Commands
        commandManager = new CommandManager();

        // Generic Commands
        commandManager.addCommands(
                new HelpCommand(),
                new PingCommand(),
                new FeatureRequestCommand(),
                new BugReportCommand(),
                new SupportCommand(),
                new BotInviteCommand(),
                new UserInfoCommand(),
                new MemberCountCommand(),
                new BotUpvoteCommand(),
                new BotDonateCommand()
        );

        // Cafe Commands
        commandManager.addCommands(
                new MenuCommand(),
                new ServeCommand(),
                new OrderCommand(),
                new BalanceCommand()
        );

        // Fun Commands
        commandManager.addCommands(
                new CoffeeMemeCommand(),
                new TeaMemeCommand(),
                new MemeCommand(),
                new JokeCommand(),
                new AddPollCommand(),
                new AddRaffleCommand(),
                new AvatarCommand(),
                new GetBirthdayCommand(),
                new SetBirthdayCommand(),
                new RemoveBirthdayCommand(),
                new CountingStatisticsCommand()
        );

        // Games Commands
        commandManager.addCommands(
                new EightBallCommand(),
                new CoinFlipCommand(),
                new DiceRollCommand(),
                new TicTacToeCommand(),
                new ConnectFourCommand()
        );

        // Social Commands
        commandManager.addCommands(
                new VentCommand()
        );

        // Interaction Commands
        commandManager.addCommands(
                new HugCommand(),
                new PunchCommand(),
                new KissCommand(),
                new BiteCommand(),
                new BlushCommand(),
                new CuddleCommand(),
                new NomCommand(),
                new PokeCommand(),
                new SlapCommand(),
                new StabCommand(),
                new HmphCommand(),
                new PoutCommand(),
                new ThrowCommand(),
                new SmileCommand(),
                new StareCommand(),
                new TickleCommand(),
                new RageCommand(),
                new YellCommand(),
                new HeadPatCommand(),
                new CryCommand(),
                new DanceCommand(),
                new DabCommand(),
                new BonkCommand(),
                new SleepCommand(),
                new DieCommand(),
                new WelcomeCommand(),
                new LickCommand(),
                new ShushCommand()
        );

        // Music Commands
        commandManager.addCommands(
                new PlayCommand(),
                new NowPlayingCommand(),
                new PauseCommand(),
                new QueueCommand(),
                new RepeatCommand(),
                new ShuffleCommand(),
                new SkipCommand(),
                new StopCommand(),
                new PlayLastCommand()
        );

        // Twitch Commands
        commandManager.addCommands(
                new SetLiveChannelCommand(),
                new AddTwitchChannelCommand(),
                new RemoveTwitchChannelCommand(),
                new GetTwitchChannelsCommand(),
                new SetLiveNotificationsRoleCommand()
        );

        // Moderation Commands
        commandManager.addCommands(
                new SetLogChannelCommand(),
                new SetUpdateChannelCommand(),
                new SetCountingChannelCommand(),
                new SetPollChannelCommand(),
                new SetRaffleChannelCommand(),
                new SetBirthdayChannelCommand(),
                new SetModeratorRoleCommand(),
                new SetMutedRoleCommand(),
                new ChangePrefixCommand(),
                new KickCommand(),
                new BanCommand(),
                new ClearChatCommand(),
                new MuteCommand(),
                new UnMuteCommand(),
                new NotifyOnUpdateCommand(),
                new CreateEmbedCommand(),
                new VoiceRoleBindCommand(),
                new GetVoiceRoleBindsCommand(),
                new SetVentingChannelCommand(),
                new SetAIStateCommand(),
                new SetDailyChannelCommand()
        );

        // Experimental Commands
        commandManager.addCommands(
                new SetWelcomeChannelCommand(),
                new EditWelcomeMessageCommand()
        );

        jdaBuilder.addEventListeners(
                new Listener(),
                new AIResponseListener()
        );

        jda = jdaBuilder.build().awaitReady();

        homeGuild = jda.getGuildById(HOME_GUILD_ID);
        homeGuildLogChannel = homeGuild.getTextChannelById(HOME_GUILD_LOG_CHANNEL_ID);
        logManager.setLogChannel(homeGuildLogChannel);

        // Connecting to the Spotify API
        generalHelper.startSpotifyRefreshTimer();

        guildHandler = new GuildHandler();

        versionHelper = new VersionHelper();
        versionHelper.contactGuilds();

        pollHandler = new PollHandler();
        raffleHandler = new RaffleHandler();

        interactionHandler = new InteractionHandler();

        birthdayHandler = new BirthdayHandler();

        ticTacToeHandler = new TicTacToeHandler();
        connectFourHandler = new ConnectFourHandler();

        welcomeHandler = new WelcomeHandler();
        welcomeListener = new WelcomeListener();
        jda.addEventListener(welcomeListener);

        voiceChatRoleBindHandler = new VoiceChatRoleBindHandler();
        jda.addEventListener(new VoiceChatListener());

        customSongManager = new CustomSongManager();

        dailyChannelHelper = new DailyChannelHelper();

        // Final Things
        logManager.log(CafeBot.class, LogLevel.OKAY, "The bot is online!");
        updateGuildPresence();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
    }

    public static void main(String[] args) {
        SpringApplication.run(CafeBot.class, args);
    }

    /**
     * @return The current {@link DiscordBotListAPI} for this session.
     */
    @NotNull
    public static DiscordBotListAPI getTopGGAPI() {
        return topGGAPI;
    }

    /**
     * @return The current {@link VentHandler} for this session.
     */
    @NotNull
    public static VentHandler getVentHandler() {
        return ventHandler;
    }

    /**
     * @return The current {@link VoiceChatRoleBindHandler} for this session.
     */
    @NotNull
    public static VoiceChatRoleBindHandler getVoiceChatRoleBindHandler() {
        return voiceChatRoleBindHandler;
    }

    /**
     * @return The current amount of commands run during this session.
     */
    @NotNull
    public static Integer getCommandsRun() {
        return commandsRun;
    }

    /**
     * Increase the amount of commands run during this session by 1.
     */
    public static void increaseCommandsRun() {
        commandsRun++;
    }

    /**
     * @return The current {@link CustomSongManager}.
     */
    @NotNull
    public static CustomSongManager getCustomSongManager() {
        return customSongManager;
    }

    /**
     * @return the current USER AGENT {@link String}.
     */
    @NotNull
    public static String getBotUserAgent() {
        return BOT_USER_AGENT;
    }

    /**
     * @return The current {@link WelcomeHandler}.
     */
    @NotNull
    public static WelcomeHandler getWelcomeHandler() {
        return welcomeHandler;
    }

    /**
     * @return The current {@link WelcomeListener}.
     */
    @NotNull
    public static WelcomeListener getWelcomeListener() {
        return welcomeListener;
    }

    /**
     * @return The current {@link ConnectFourHandler}.
     */
    @NotNull
    public static ConnectFourHandler getConnectFourHandler() {
        return connectFourHandler;
    }

    /**
     * @return The current {@link TicTacToeHandler}.
     */
    @NotNull
    public static TicTacToeHandler getTicTacToeHandler() {
        return ticTacToeHandler;
    }

    /**
     * @return The current {@link BirthdayHandler}.
     */
    @NotNull
    public static BirthdayHandler getBirthdayHandler() {
        return birthdayHandler;
    }

    /**
     * @return The current {@link InteractionHandler}.
     */
    @NotNull
    public static InteractionHandler getInteractionHandler() {
        return interactionHandler;
    }

    /**
     * @return The current {@link RaffleHandler}.
     */
    @NotNull
    public static RaffleHandler getRaffleHandler() {
        return raffleHandler;
    }

    /**
     * @return The current {@link PollHandler}.
     */
    @NotNull
    public static PollHandler getPollHandler() {
        return pollHandler;
    }

    /**
     * @return The current {@link MenuHandler}.
     */
    @NotNull
    public static MenuHandler getMenuHandler() {
        return menuHandler;
    }

    /**
     * @return The current {@link ServeHandler}.
     */
    @NotNull
    public static ServeHandler getServeHandler() {
        return serveHandler;
    }

    /**
     * @return The current {@link CountingHelper}.
     */
    @NotNull
    public static CountingHelper getCountingHelper() {
        return countingHelper;
    }

    /**
     * Updates the presence for the {@link JDA}.
     */
    public static void updateGuildPresence() {
        jda.getPresence().setActivity(Activity.playing("!! | cafeBot " + BOT_VERSION + " - Currently in " + jda.getGuilds().size() + " servers!"));
    }

    /**
     * @return The current {@link TwitchHandler}.
     */
    @NotNull
    public static TwitchHandler getTwitchHandler() {
        return twitchHandler;
    }

    /**
     * @return The current Twitch Access Token
     */
    @NotNull
    public static String getTwitchAccessToken() {
        return TWITCH_ACCESS_TOKEN;
    }

    /**
     * @return The current {@link SpotifyApi}.
     */
    @Nullable
    public static SpotifyApi getSpotifyApi() {
        return spotifyApi;
    }

    /**
     * Sets the new {@link SpotifyApi} for the {@link CafeBot}.
     * @param newSpotifyAPI The new {@link SpotifyApi} to be set.
     */
    public static void setSpotifyApi(SpotifyApi newSpotifyAPI) {
        spotifyApi = newSpotifyAPI;
    }

    /**
     * @return The client ID for the {@link SpotifyApi}.
     */
    @NotNull
    public static String getSpotifyApiClientID() {
        return SPOTIFY_API_CLIENT_ID;
    }

    /**
     * @return The client secret for the {@link SpotifyApi}.
     */
    @NotNull
    public static String getSpotifyApiClientSecret() {
        return SPOTIFY_API_CLIENT_SECRET;
    }

    /**
     * @return The current {@link GeneralHelper}.
     */
    @Nullable
    public static GeneralHelper getGeneralHelper() {
        return generalHelper;
    }

    /**
     * @return The current {@link GuildHandler}.
     */
    @Nullable
    public static GuildHandler getGuildHandler() {
        return guildHandler;
    }

    /**
     * @return The current {@link JDA}.
     */
    @Nullable
    public static JDA getJDA() {
        return jda;
    }

    /**
     * @return The bot's default prefix.
     */
    @NotNull
    public static String getPrefix() {
        return BOT_PREFIX;
    }

    /**
     * @return The current {@link LogManager}.
     */
    @Nullable
    public static LogManager getLogManager() {
        return logManager;
    }

    /**
     * @return The current {@link SQLServer}.
     */
    @Nullable
    public static SQLServer getSQLServer() {
        return sqlServer;
    }

    /**
     * Set the new main {@link SQLServer}.
     * @param newSQLServer The new {@link SQLServer} object.
     */
    public static void setSQLServer(@NotNull SQLServer newSQLServer) {
        sqlServer = newSQLServer;
    }

    /**
     * @return The current URL for the main {@link SQLServer}.
     */
    @NotNull
    public static String getSQLURL() {
        return SQL_URL;
    }

    /**
     * @return The current port for the main {@link SQLServer}.
     */
    @NotNull
    public static String getSQLPort() {
        return SQL_PORT;
    }

    /**
     * @return Whether or not to encrypt the connection for the main {@link SQLServer}.
     */
    @NotNull
    public static Boolean getSQLEncrypt() {
        return SQL_ENCRYPT;
    }

    /**
     * @return The username for the main {@link SQLServer}.
     */
    @NotNull
    public static String getSQLUsername() {
        return SQL_USERNAME;
    }

    /**
     * @return The password for the main {@link SQLServer}.
     */
    @NotNull
    public static String getSQLPassword() {
        return SQL_PASSWORD;
    }

    /**
     * @return The current {@link CommandManager}.
     */
    @Nullable
    public static CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * @return The current Bot Version as a {@link String}.
     */
    @NotNull
    public static String getBotVersion() {
        return BOT_VERSION;
    }

    /**
     * @return The Discord avatar URL for the bot.
     */
    @NotNull
    public static String getDiscordAvatarUrl() {
        return DISCORD_AVATAR_URL;
    }

}
