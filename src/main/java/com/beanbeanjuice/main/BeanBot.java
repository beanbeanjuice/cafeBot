package com.beanbeanjuice.main;

import com.beanbeanjuice.command.fun.MemeCommand;
import com.beanbeanjuice.command.fun.JokeCommand;
import com.beanbeanjuice.command.general.HelpCommand;
import com.beanbeanjuice.command.general.PingCommand;
import com.beanbeanjuice.command.moderation.*;
import com.beanbeanjuice.command.moderation.mute.MuteCommand;
import com.beanbeanjuice.command.moderation.mute.UnMuteCommand;
import com.beanbeanjuice.command.music.*;
import com.beanbeanjuice.command.twitch.AddTwitchChannelCommand;
import com.beanbeanjuice.command.twitch.GetTwitchChannelsCommand;
import com.beanbeanjuice.command.twitch.RemoveTwitchChannelCommand;
import com.beanbeanjuice.command.twitch.SetLiveChannelCommand;
import com.beanbeanjuice.utility.command.CommandManager;
import com.beanbeanjuice.utility.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.GeneralHelper;
import com.beanbeanjuice.utility.listener.Listener;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.logger.LogManager;
import com.beanbeanjuice.utility.sql.SQLServer;
import com.beanbeanjuice.utility.twitch.TwitchHandler;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The main {@link BeanBot} class.
 *
 * @author beanbeanjuice
 */
public class BeanBot {

    // General Bot Info
    private static final String BOT_TOKEN = "Nzk4OTc4NDE3OTk0NDk4MDYx.X_84ow.NeaUaBDNzZro3kHsdzTljAoznls";
    private static JDA jda;
    private static JDABuilder jdaBuilder;

    // Logging Stuff
    private static Guild homeGuild;
    private static final String HOME_GUILD_ID = "798830792938881024";
    private static TextChannel homeGuildLogChannel;
    private static final String HOME_GUILD_LOG_CHANNEL_ID = "799005537542471751";
    private static final String HOME_GUILD_WEBHOOK_URL = "https://discordapp.com/api/webhooks/800609733278171197/314ISBsWKDfxdnOEHp6UYd1GKGjhJea3a0tHXmYVai5B5ScTNoo2b4_4tNzfUUbT0_4h";

    private static final String BOT_PREFIX = "!!";

    // Guild Manager Stuff
    private static GuildHandler guildHandler;

    // Command Stuff
    private static CommandManager commandManager;

    // Spotify Stuff
    private static SpotifyApi spotifyApi;
    private static ClientCredentialsRequest clientCredentialsRequest;
    private static final String SPOTIFY_API_CLIENT_ID = "b955ba0137d141a1807107fd0a256257";
    private static final String SPOTIFY_API_CLIENT_SECRET = "ecc6abba4a4548b3bb53c37b9fcffb76";

    // Twitch Stuff
    private static final String TWITCH_ACCESS_TOKEN = "r2iu2i9jt5pwz3yt7spvilzd8fnxsd";
    private static TwitchHandler twitchHandler;

    // SQL Stuff
    private static SQLServer sqlServer;
    private static final String SQL_URL = "beanbeanjuice.com";
    private static final String SQL_PORT = "4001";
    private static final String SQL_USERNAME = "root";
    private static final String SQL_PASSWORD = "XEuE@*mHB-P4huC^RgcfXLTJXA8Hq.";
    private static final boolean SQL_ENCRYPT = true;

    // Logging
    private static LogManager logManager;

    // Other
    private static GeneralHelper generalHelper;
    private static Timer refreshTimer;
    private static TimerTask refreshTimerTask;

    public static void main(String[] args) throws LoginException, InterruptedException {

        twitchHandler = new TwitchHandler();
        sqlServer = new SQLServer(SQL_URL, SQL_PORT, SQL_ENCRYPT, SQL_USERNAME, SQL_PASSWORD);
        sqlServer.startConnection();

        logManager = new LogManager("Log Manager", homeGuild, homeGuildLogChannel);

        logManager.addWebhookURL(HOME_GUILD_WEBHOOK_URL);
        logManager.log(BeanBot.class, LogLevel.OKAY, "Starting bot!", true, false);

        jdaBuilder = JDABuilder.createDefault(BOT_TOKEN);
        jdaBuilder.setActivity(Activity.playing("The Barista v1.0.3 - Default Command: !!help"));

        jdaBuilder.enableIntents(GatewayIntent.GUILD_PRESENCES);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_VOICE_STATES);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_BANS);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_EMOJIS);
        jdaBuilder.enableCache(CacheFlag.ACTIVITY);
        jdaBuilder.enableCache(CacheFlag.CLIENT_STATUS);
        jdaBuilder.enableCache(CacheFlag.EMOTE);
        jdaBuilder.enableCache(CacheFlag.VOICE_STATE);
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
        jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);

        // Listeners and Commands
        commandManager = new CommandManager();
        commandManager.addCommand(new HelpCommand());
        commandManager.addCommand(new PingCommand());

        commandManager.addCommand(new NowPlayingCommand());
        commandManager.addCommand(new PlayCommand());
        commandManager.addCommand(new QueueCommand());
        commandManager.addCommand(new RepeatCommand());
        commandManager.addCommand(new SkipCommand());
        commandManager.addCommand(new StopCommand());

        commandManager.addCommand(new MemeCommand());
        commandManager.addCommand(new JokeCommand());

        commandManager.addCommand(new SetModeratorRoleCommand());
        commandManager.addCommand(new SetMutedRoleCommand());
        commandManager.addCommand(new ChangePrefixCommand());
        commandManager.addCommand(new KickCommand());
        commandManager.addCommand(new BanCommand());
        commandManager.addCommand(new ClearChatCommand());
        commandManager.addCommand(new MuteCommand());
        commandManager.addCommand(new UnMuteCommand());

        commandManager.addCommand(new SetLiveChannelCommand());
        commandManager.addCommand(new AddTwitchChannelCommand());
        commandManager.addCommand(new RemoveTwitchChannelCommand());
        commandManager.addCommand(new GetTwitchChannelsCommand());

        jdaBuilder.addEventListeners(new Listener());

        jda = jdaBuilder.build().awaitReady();

        homeGuild = jda.getGuildById(HOME_GUILD_ID);
        homeGuildLogChannel = homeGuild.getTextChannelById(HOME_GUILD_LOG_CHANNEL_ID);
        logManager.setGuild(homeGuild);
        logManager.setLogChannel(homeGuildLogChannel);

        logManager.log(BeanBot.class, LogLevel.OKAY, "The bot is online!");

        // Connecting to the Spotify API
        connectToSpotifyAPI();
        startRefreshTimer();

        logManager.setGuild(homeGuild);
        logManager.setLogChannel(homeGuildLogChannel);
        guildHandler = new GuildHandler();

        generalHelper = new GeneralHelper();
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
                logManager.log(BeanBot.class, LogLevel.INFO, "Re-establishing Spotify Connection");
                sqlServer = new SQLServer(SQL_URL, SQL_PORT, SQL_ENCRYPT, SQL_USERNAME, SQL_PASSWORD);
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
            logManager.log(BeanBot.class, LogLevel.INFO, "Spotify Access Token Expires In: " + clientCredentials.getExpiresIn());
            logManager.log(BeanBot.class, LogLevel.OKAY, "Successfully connected to the Spotify API!");
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            logManager.log(BeanBot.class, LogLevel.ERROR, e.getMessage());
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

}
