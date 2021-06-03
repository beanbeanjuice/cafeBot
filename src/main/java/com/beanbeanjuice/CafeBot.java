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
import com.beanbeanjuice.command.moderation.welcome.EditWelcomeMessageCommand;
import com.beanbeanjuice.command.moderation.welcome.SetWelcomeChannelCommand;
import com.beanbeanjuice.command.music.*;
import com.beanbeanjuice.command.twitch.*;
import com.beanbeanjuice.utility.listener.WelcomeListener;
import com.beanbeanjuice.utility.sections.fun.birthday.BirthdayHandler;
import com.beanbeanjuice.utility.sections.cafe.MenuHandler;
import com.beanbeanjuice.utility.sections.cafe.ServeHandler;
import com.beanbeanjuice.utility.command.CommandManager;
import com.beanbeanjuice.utility.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.CountingHelper;
import com.beanbeanjuice.utility.helper.GeneralHelper;
import com.beanbeanjuice.utility.helper.JSONHelper;
import com.beanbeanjuice.utility.helper.VersionHelper;
import com.beanbeanjuice.utility.sections.games.connectfour.ConnectFourHandler;
import com.beanbeanjuice.utility.sections.games.tictactoe.TicTacToeHandler;
import com.beanbeanjuice.utility.sections.interaction.InteractionHandler;
import com.beanbeanjuice.utility.listener.Listener;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.logger.LogManager;
import com.beanbeanjuice.utility.sections.fun.poll.PollHandler;
import com.beanbeanjuice.utility.sections.fun.raffle.RaffleHandler;
import com.beanbeanjuice.utility.sections.moderation.welcome.WelcomeHandler;
import com.beanbeanjuice.utility.sql.SQLServer;
import com.beanbeanjuice.utility.sections.twitch.TwitchHandler;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The main {@link CafeBot} class.
 *
 * @author beanbeanjuice
 */
@SpringBootApplication
public class CafeBot {

    // File Information
    // -- 'beta.json' -> Beta Bot Information
    // -- 'release.json' -> Release Bot Information
    private static final String FILE_INFO = "beta.json";

    // General Bot Info
    private static final String BOT_VERSION = JSONHelper.getValue(FILE_INFO, "bot", "version").textValue();
    private static final String BOT_TOKEN = JSONHelper.getValue(FILE_INFO, "bot", "token").textValue();
    private static final String BOT_USER_AGENT = "java:com.beanbeanjuice.cafeBot:" + BOT_VERSION;
    private static JDA jda;
    private static JDABuilder jdaBuilder;
    private static final String DISCORD_AVATAR_URL = "http://cdn.beanbeanjuice.com/images/cafeBot/cafeBot.gif";

    // Logging Stuff
    private static Guild homeGuild;
    private static final String HOME_GUILD_ID = JSONHelper.getValue(FILE_INFO, "bot", "guild_id").textValue();
    private static TextChannel homeGuildLogChannel;
    private static final String HOME_GUILD_LOG_CHANNEL_ID = JSONHelper.getValue(FILE_INFO, "bot", "guild_log_channel_id").textValue();
    private static final String HOME_GUILD_WEBHOOK_URL = JSONHelper.getValue(FILE_INFO, "bot", "guild_webhook_url").textValue();

    private static final String BOT_PREFIX = "!!";

    // Guild Manager Stuff
    private static GuildHandler guildHandler;

    // Command Stuff
    private static CommandManager commandManager;

    // Spotify Stuff
    private static SpotifyApi spotifyApi;
    private static ClientCredentialsRequest clientCredentialsRequest;
    private static final String SPOTIFY_API_CLIENT_ID = JSONHelper.getValue(FILE_INFO, "spotify", "id").textValue();
    private static final String SPOTIFY_API_CLIENT_SECRET = JSONHelper.getValue(FILE_INFO, "spotify", "secret").textValue();

    // Twitch Stuff
    private static final String TWITCH_ACCESS_TOKEN = JSONHelper.getValue(FILE_INFO, "twitch", "access_token").textValue();
    private static TwitchHandler twitchHandler;

    // SQL Stuff
    private static SQLServer sqlServer;
    private static final String SQL_URL = JSONHelper.getValue(FILE_INFO, "mysql", "url").textValue();
    private static final String SQL_PORT = JSONHelper.getValue(FILE_INFO, "mysql", "port").textValue();
    private static final String SQL_USERNAME = JSONHelper.getValue(FILE_INFO, "mysql", "username").textValue();
    private static final String SQL_PASSWORD = JSONHelper.getValue(FILE_INFO, "mysql", "password").textValue();
    private static final boolean SQL_ENCRYPT = JSONHelper.getValue(FILE_INFO, "mysql", "encrypt").booleanValue();

    // Logging
    private static LogManager logManager;

    // Other
    private static GeneralHelper generalHelper;
    private static Timer refreshTimer;
    private static TimerTask refreshTimerTask;

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

    public static void main(String[] args) throws LoginException, InterruptedException {

        SpringApplication.run(CafeBot.class, args);

        logManager = new LogManager("Log Manager", homeGuildLogChannel);

        countingHelper = new CountingHelper();
        twitchHandler = new TwitchHandler();
        sqlServer = new SQLServer(SQL_URL, SQL_PORT, SQL_ENCRYPT, SQL_USERNAME, SQL_PASSWORD);
        sqlServer.startConnection();

        logManager.addWebhookURL(HOME_GUILD_WEBHOOK_URL);
        logManager.log(CafeBot.class, LogLevel.OKAY, "Starting bot!", true, false);

        jdaBuilder = JDABuilder.createDefault(BOT_TOKEN);
        jdaBuilder.setActivity(Activity.playing("The barista is starting up..."));
        jdaBuilder.setStatus(OnlineStatus.IDLE);

        jdaBuilder.enableIntents(
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_BANS,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_MEMBERS
        );
        jdaBuilder.enableCache(
                CacheFlag.EMOTE,
                CacheFlag.VOICE_STATE
        );
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
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
                new MemberCountCommand()
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
                new LickCommand()
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
                new StopCommand()
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
                new SetWelcomeChannelCommand(),
                new EditWelcomeMessageCommand(),
                new SetModeratorRoleCommand(),
                new SetMutedRoleCommand(),
                new ChangePrefixCommand(),
                new KickCommand(),
                new BanCommand(),
                new ClearChatCommand(),
                new MuteCommand(),
                new UnMuteCommand(),
                new NotifyOnUpdateCommand()
        );

        jdaBuilder.addEventListeners(new Listener());

        jda = jdaBuilder.build().awaitReady();

        homeGuild = jda.getGuildById(HOME_GUILD_ID);
        homeGuildLogChannel = homeGuild.getTextChannelById(HOME_GUILD_LOG_CHANNEL_ID);
        logManager.setLogChannel(homeGuildLogChannel);

        logManager.log(CafeBot.class, LogLevel.OKAY, "The bot is online!");

        // Connecting to the Spotify API
        connectToSpotifyAPI();
        startRefreshTimer();

        guildHandler = new GuildHandler();

        generalHelper = new GeneralHelper();

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

        // Final Things
        updateGuildPresence();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
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
     * Starts the re-establishing of a Spotify Key Timer.
     */
    public static void startRefreshTimer() {
        refreshTimer = new Timer();
        refreshTimerTask = new TimerTask() {

            @Override
            public void run() {
                connectToSpotifyAPI();
                logManager.log(CafeBot.class, LogLevel.INFO, "Re-establishing Spotify Connection", true, false);

                try {
                    logManager.log(CafeBot.class, LogLevel.INFO, "Refreshing MySQL Connection...", true, false);
                    sqlServer.getConnection().close(); // Closes the SQL Connection
                    sqlServer.startConnection(); // Reopens the SQL Connection

                    // If the SQL Connection is still closed, then it must throw an sql exception.
                    if (!sqlServer.checkConnection()) {
                        throw new SQLException("The connection is still closed.");
                    }

                    logManager.log(CafeBot.class, LogLevel.OKAY, "Successfully refreshed the MySQL Connection!", true, false);
                } catch (SQLException e) {
                    logManager.log(CafeBot.class, LogLevel.WARN, "Unable to Connect to the SQL Server: " + e.getMessage(), true, false);

                    sqlServer = new SQLServer(SQL_URL, SQL_PORT, SQL_ENCRYPT, SQL_USERNAME, SQL_PASSWORD);
                    sqlServer.startConnection();
                }
            }
        };
        refreshTimer.scheduleAtFixedRate(refreshTimerTask, 3000000, 3000000);
    }

    /**
     * Connects to the Spotify API
     */
    public static void connectToSpotifyAPI() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(SPOTIFY_API_CLIENT_ID)
                .setClientSecret(SPOTIFY_API_CLIENT_SECRET)
                .build();

        clientCredentialsRequest = spotifyApi.clientCredentials().build();

        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            logManager.log(CafeBot.class, LogLevel.INFO, "Spotify Access Token Expires In: " + clientCredentials.getExpiresIn() + " Seconds", true, false);
            logManager.log(CafeBot.class, LogLevel.OKAY, "Successfully connected to the Spotify API!", true, false);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            logManager.log(CafeBot.class, LogLevel.ERROR, e.getMessage());
        }
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
