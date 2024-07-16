package com.beanbeanjuice.cafebot;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildsEndpoint;
import com.beanbeanjuice.cafebot.commands.cafe.BalanceCommand;
import com.beanbeanjuice.cafebot.commands.cafe.DonateCommand;
import com.beanbeanjuice.cafebot.commands.cafe.MenuCommand;
import com.beanbeanjuice.cafebot.commands.cafe.ServeCommand;
import com.beanbeanjuice.cafebot.commands.fun.*;
import com.beanbeanjuice.cafebot.commands.fun.birthday.BirthdayCommand;
import com.beanbeanjuice.cafebot.commands.fun.counting.CountingCommand;
import com.beanbeanjuice.cafebot.commands.fun.meme.MemeCommand;
import com.beanbeanjuice.cafebot.commands.fun.rate.RateCommand;
import com.beanbeanjuice.cafebot.commands.games.CoinFlipCommand;
import com.beanbeanjuice.cafebot.commands.games.DiceRollCommand;
import com.beanbeanjuice.cafebot.commands.games.TicTacToeCommand;
import com.beanbeanjuice.cafebot.commands.games.game.GameCommand;
import com.beanbeanjuice.cafebot.commands.generic.PingCommand;
import com.beanbeanjuice.cafebot.commands.generic.*;
import com.beanbeanjuice.cafebot.commands.interaction.*;
import com.beanbeanjuice.cafebot.commands.moderation.ClearChatCommand;
import com.beanbeanjuice.cafebot.commands.settings.AICommand;
import com.beanbeanjuice.cafebot.commands.settings.daily.DailyCommand;
import com.beanbeanjuice.cafebot.commands.settings.goodbye.GoodbyeCommand;
import com.beanbeanjuice.cafebot.commands.settings.update.UpdateCommand;
import com.beanbeanjuice.cafebot.commands.settings.welcome.WelcomeCommand;
import com.beanbeanjuice.cafebot.commands.social.MemberCountCommand;
import com.beanbeanjuice.cafebot.commands.social.vent.VentCommand;
import com.beanbeanjuice.cafebot.commands.twitch.TwitchCommand;
import com.beanbeanjuice.cafebot.utility.api.GitHubVersionEndpointWrapper;
import com.beanbeanjuice.cafebot.utility.commands.CommandHandler;
import com.beanbeanjuice.cafebot.utility.helper.DailyChannelHelper;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.helper.UpdateCheckHelper;
import com.beanbeanjuice.cafebot.utility.listeners.*;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafebot.utility.logging.LogManager;
import com.beanbeanjuice.cafeapi.wrapper.CafeAPI;
import com.beanbeanjuice.cafeapi.wrapper.requests.RequestLocation;
import com.beanbeanjuice.cafebot.utility.sections.cafe.MenuHandler;
import com.beanbeanjuice.cafebot.utility.sections.game.TicTacToeListener;
import com.beanbeanjuice.cafebot.utility.sections.generic.HelpHandler;
import com.beanbeanjuice.cafebot.utility.sections.generic.HelpListener;
import com.beanbeanjuice.cafebot.utility.sections.twitch.TwitchHandler;
import com.sun.management.OperatingSystemMXBean;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CacheRestAction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * The main {@link JDA bot} class.
 *
 * @author beanbeanjuice
 */
public class CafeBot {

    // Bot Items
    @Getter private final String botVersion = getVersion();
    @Getter private final String botUserAgent = "java:com.beanbeanjuice.cafeBot:" + botVersion;
    @Getter private final JDA JDA;

    // API
    @Getter private final CafeAPI cafeAPI;

    // Internal Items
    @Getter private final LogManager logger;

    // Listeners
    @Getter private AIResponseListener aiResponseListener;

    // Helpers
    private DailyChannelHelper dailyChannelHelper;

    // Handlers
    private CommandHandler commandHandler;
    @Getter private MenuHandler menuHandler;
    @Getter private HelpHandler helpHandler;
    @Getter private TwitchHandler twitchHandler;

    // Additional Items
    @Getter private int commandsRun = 0;
    @Getter private final String discordAvatarUrl = "https://cdn.beanbeanjuice.com/images/cafeBot/cafeBot.gif";

    public CafeBot() throws InterruptedException {
        this.logger = new LogManager(
                this,
                "cafeBot Logging System",
                System.getenv("CAFEBOT_GUILD_ID"),
                System.getenv("CAFEBOT_GUILD_LOG_CHANNEL_ID"),
                "logs/"
        );
        this.cafeAPI = new CafeAPI(
                "beanbeanjuice",
                System.getenv("API_PASSWORD"),
                RequestLocation.valueOf(System.getenv("CAFEBOT_REQUEST_LOCATION"))
        );
        this.cafeAPI.setKawaiiAPI(System.getenv("KAWAII_API_TOKEN"));

        this.logger.addWebhookURL(System.getenv("CAFEBOT_GUILD_WEBHOOK_URL"));
        this.logger.log(CafeBot.class, LogLevel.OKAY, "Starting bot!", true, false);

        this.JDA = JDABuilder
                .createDefault(System.getenv("CAFEBOT_TOKEN"))
                .setActivity(Activity.playing("The barista is starting..."))
                .setStatus(OnlineStatus.IDLE)
                .enableIntents(
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT
                )
//                .enableCache(
//                        CacheFlag.EMOJI
//                )
//                .setMemberCachePolicy(MemberCachePolicy.ALL)
//                .setChunkingFilter(ChunkingFilter.ALL)
                .build()
                .awaitReady();

        logger.log(CafeBot.class, LogLevel.INFO, "Checking servers...");
        this.checkGuilds();

        this.logger.enableDiscordLogging();
        logger.log(CafeBot.class, LogLevel.INFO, "Enabled Discord Logging...", true, true);

        JDA.getPresence().setStatus(OnlineStatus.ONLINE);
        logger.log(CafeBot.class, LogLevel.OKAY, "The bot is online!");

        logger.log(CafeBot.class, LogLevel.INFO, "Adding commands...");
        setupCommands();
        this.menuHandler = new MenuHandler(this);

        setupListeners();

        this.JDA.getGuilds();

        this.startUpdateTimer();
        this.startBioUpdateTimer();
    }

    private void checkGuilds() {
        GuildsEndpoint guildsEndpoint = this.cafeAPI.getGuildsEndpoint();
        guildsEndpoint.getAllGuildInformation().thenAcceptAsync((information) -> {
            this.JDA.getGuilds().forEach((guild) -> {
                if (information.containsKey(guild.getId())) return;

                guildsEndpoint.createGuildInformation(guild.getId());
            });
        });
    }

    private void setupCommands() {
        this.commandHandler = new CommandHandler(this);

        this.commandHandler.addCommands(
                // Generic
                new BotDonateCommand(this),
                new BotInviteCommand(this),
                new BotUpvoteCommand(this),
                new VersionCommand(this),
                new BugReportCommand(this),
                new FeatureCommand(this),
                new DefineCommand(this),
                new GenerateCode(this),
                new HelpCommand(this),
                new PingCommand(this),
                new InfoCommand(this),
                new StatsCommand(this),
                new RemoveMyDataCommand(this),
                new SupportCommand(this),
                new WhoCommand(this),

                // Cafe
                new BalanceCommand(this),
                new DonateCommand(this),
                new ServeCommand(this),
                new MenuCommand(this),

                // Fun
                new AvatarCommand(this),
                new BannerCommand(this),
                new BirthdayCommand(this),
                new MemeCommand(this),
                new JokeCommand(this),
                new CountingCommand(this),
                new RateCommand(this),
                new EightBallCommand(this),

                // Games
                new CoinFlipCommand(this),
                new DiceRollCommand(this),
                new GameCommand(this),
                new TicTacToeCommand(this),

                // Interactions
                new AmazedCommand(this),
                new AskCommand(this),
                new BiteCommand(this),
                new BlushCommand(this),
                new BonkCommand(this),
                new BoopCommand(this),
                new CryCommand(this),
                new CuddleCommand(this),
                new DabCommand(this),
                new DanceCommand(this),
                new DieCommand(this),
                new GreetCommand(this),
                new HeadPatCommand(this),
                new HmphCommand(this),
                new HugCommand(this),
                new KissCommand(this),
                new LickCommand(this),
                new LoveCommand(this),
                new NomCommand(this),
                new NoseBleedCommand(this),
                new OkCommand(this),
                new PokeCommand(this),
                new PoutCommand(this),
                new PunchCommand(this),
                new RageCommand(this),
                new ShootCommand(this),
                new ShushCommand(this),
                new SlapCommand(this),
                new SleepCommand(this),
                new SmileCommand(this),
                new StabCommand(this),
                new StareCommand(this),
                new ThrowCommand(this),
                new TickleCommand(this),
                new UWUCommand(this),
                new WaveCommand(this),
                new WinkCommand(this),
                new YellCommand(this),

                // Social
                new MemberCountCommand(this),
                new VentCommand(this),

                // Twitch
                new TwitchCommand(this),

                // Moderation
                new ClearChatCommand(this),

                // Settings
                new AICommand(this),
                new WelcomeCommand(this),
                new GoodbyeCommand(this),
                new UpdateCommand(this),
                new DailyCommand(this)

//                new EmbedCommand(this)
        );

        this.JDA.addEventListener(commandHandler);
        this.helpHandler = new HelpHandler(commandHandler);
        this.twitchHandler = new TwitchHandler(System.getenv("CAFEBOT_TWITCH_ACCESS_TOKEN"), this);

        UpdateCheckHelper updateCheckHelper = new UpdateCheckHelper(this);
        updateCheckHelper.checkUpdate();

        this.dailyChannelHelper = new DailyChannelHelper(this);
        dailyChannelHelper.start();
    }

    public void addEventListener(final ListenerAdapter listener) {
        this.JDA.addEventListener(listener);
    }

    private void setupListeners() {
        this.aiResponseListener = new AIResponseListener(this);
        this.JDA.addEventListener(
                new BotAddListener(this),
                new BotRemoveListener(this),
                new CountingListener(this),
                new HelpListener(commandHandler, helpHandler),
                new TicTacToeListener(cafeAPI.getWinStreaksEndpoint()),
                aiResponseListener,
                new WelcomeListener(this),
                new GoodbyeListener(this)
        );
    }

    private String getVersion() {
        String resourceName = "cafebot.properties"; // could also be a constant
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) { props.load(resourceStream); }
        catch (IOException e) { throw new RuntimeException(e); }

        return "v" + props.get("version").toString();
    }

    private void startUpdateTimer() {
        CafeBot bot = this;

        Timer updateTimer = new Timer();
        TimerTask updateTimerTask = new TimerTask() {
            @Override
            public void run() {
                bot.getLogger().log(CafeBot.class, LogLevel.DEBUG, "Sending bot status message...");

                bot.getUser("690927484199370753").queue((owner) -> {
                    bot.getJDA().getRestPing().queue((ping) -> {
                        bot.pmUser(owner, getUpdateEmbed(ping, bot.getJDA().getGatewayPing()));
                    });
                });
            }
        };
        updateTimer.scheduleAtFixedRate(updateTimerTask, 0, TimeUnit.DAYS.toMillis(1));
    }

    private void startBioUpdateTimer() {
        String initialString = "/help | " + this.botVersion + " - ";

        CafeBot bot = this;

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int num = Helper.getRandomInteger(1, 4);
                String finalString = "";

                switch (num) {
                    case 1 -> finalString = "Currently in " + getTotalServers() + " cafés!";
                    case 2 -> finalString = "Waiting " + getTotalChannels() + " tables!";
                    case 3 -> finalString = "Serving " + getTotalUsers() + " customers!";
                }

                bot.getJDA().getPresence().setActivity(Activity.playing(initialString + finalString));
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, TimeUnit.MINUTES.toMillis(1));
    }

    private MessageEmbed getUpdateEmbed(@NotNull Long botPing, @NotNull Long gatewayPing) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder descriptionBuilder = new StringBuilder();
        double cpuLoad = (double) Math.round((ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getCpuLoad()*100) * 100) / 100;
        long systemMemoryTotal = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getTotalMemorySize()/1048576;
        long systemMemoryUsage = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getCommittedVirtualMemorySize()/1048576;
        long dedicatedMemoryTotal = Runtime.getRuntime().maxMemory()/1048576;
        long dedicatedMemoryUsage = Runtime.getRuntime().totalMemory()/1048576;
        embedBuilder.setTitle("Daily CafeBot Update");
        descriptionBuilder.append("**__System Status__**: Online\n\n");
        descriptionBuilder.append("**__Rest Ping__** - `").append(botPing).append("`\n")
                .append("**__Gateway Ping__** - `").append(gatewayPing).append("`\n")
                .append("**__Current Version__** - `").append(this.botVersion).append("`\n")
                .append("**__CPU Usage__** - `").append(cpuLoad).append("%`\n")
                .append("**__OS Memory Usage__** - `").append(systemMemoryUsage).append("` mb / `").append(systemMemoryTotal).append("` mb\n")
                .append("**__Bot Memory Usage__** - `").append(dedicatedMemoryUsage).append("` mb / `").append(dedicatedMemoryTotal).append("` mb\n")
                .append("**__Bot Uptime__** - `").append(Helper.formatTimeDays(ManagementFactory.getRuntimeMXBean().getUptime())).append("`\n")
                .append("**__Commands Run (After Restart)__** - `").append(this.commandsRun).append("`\n");

        embedBuilder.setDescription(descriptionBuilder.toString());
        embedBuilder.setThumbnail(this.discordAvatarUrl);
        embedBuilder.setColor(Color.green);
        return embedBuilder.build();
    }

    public int getTotalChannels() {
        return this.JDA.getTextChannels().size();
    }

    public int getTotalServers() {
        return this.JDA.getGuilds().size();
    }

    // TODO: This is only getting cached users.
    public int getTotalUsers() {
        return this.JDA.getUsers().size();
    }

    private CacheRestAction<User> getUser(String userID) {
        userID = userID.replace("<@!", "");
        userID = userID.replace("<@", ""); // Edge Case for Mobile
        userID = userID.replace(">", "");

        try {
            return this.JDA.retrieveUserById(userID);
        } catch (NullPointerException | NumberFormatException e) {
            this.logger.log(CafeBot.class, LogLevel.ERROR, "Error getting user from cache: " + e.getMessage());
            throw e;
        }
    }

    public void pmUser(User user, MessageEmbed embed) {
        user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embed)).queue();
    }

    public void increaseCommandsRun() {
        this.commandsRun++;
    }

    public static void main(String[] args) throws InterruptedException {
        new CafeBot();
    }

}
