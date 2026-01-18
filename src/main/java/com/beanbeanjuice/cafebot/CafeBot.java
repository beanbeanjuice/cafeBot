package com.beanbeanjuice.cafebot;

import com.beanbeanjuice.cafebot.api.wrapper.CafeAPI;
import com.beanbeanjuice.cafebot.api.wrapper.type.Greeting;
import com.beanbeanjuice.cafebot.commands.cafe.BalanceCommand;
import com.beanbeanjuice.cafebot.commands.cafe.DonateCommand;
import com.beanbeanjuice.cafebot.commands.cafe.MenuCommand;
import com.beanbeanjuice.cafebot.commands.cafe.ServeCommand;
import com.beanbeanjuice.cafebot.commands.fun.*;
import com.beanbeanjuice.cafebot.commands.fun.birthday.BirthdayCommand;
import com.beanbeanjuice.cafebot.commands.fun.meme.MemeCommand;
import com.beanbeanjuice.cafebot.commands.fun.rate.RateCommand;
import com.beanbeanjuice.cafebot.commands.games.CoinFlipCommand;
import com.beanbeanjuice.cafebot.commands.games.CountingStatisticsCommand;
import com.beanbeanjuice.cafebot.commands.games.RollCommand;
import com.beanbeanjuice.cafebot.commands.games.TicTacToeCommand;
import com.beanbeanjuice.cafebot.commands.games.game.GameCommand;
import com.beanbeanjuice.cafebot.commands.generic.*;
import com.beanbeanjuice.cafebot.commands.generic.twitch.TwitchCommand;
import com.beanbeanjuice.cafebot.commands.interaction.*;
import com.beanbeanjuice.cafebot.commands.interaction.generic.InteractionCommand;
import com.beanbeanjuice.cafebot.commands.moderation.ClearChatCommand;
import com.beanbeanjuice.cafebot.commands.settings.airport.AirportMessageCommand;
import com.beanbeanjuice.cafebot.commands.settings.bind.BindCommand;
import com.beanbeanjuice.cafebot.commands.settings.channels.ChannelCommand;
import com.beanbeanjuice.cafebot.commands.settings.polls.PollCommand;
import com.beanbeanjuice.cafebot.commands.settings.raffles.RaffleCommand;
import com.beanbeanjuice.cafebot.commands.settings.roles.RoleCommand;
import com.beanbeanjuice.cafebot.commands.social.MemberCountCommand;
import com.beanbeanjuice.cafebot.commands.social.ConfessCommand;
import com.beanbeanjuice.cafebot.utility.EnvironmentVariable;
import com.beanbeanjuice.cafebot.utility.commands.CommandHandler;
import com.beanbeanjuice.cafebot.utility.handlers.*;
import com.beanbeanjuice.cafebot.utility.handlers.games.TicTacToeHandler;
import com.beanbeanjuice.cafebot.utility.handlers.polls.PollSessionHandler;
import com.beanbeanjuice.cafebot.utility.listeners.*;
import com.beanbeanjuice.cafebot.utility.listeners.ai.AIResponseListener;
import com.beanbeanjuice.cafebot.utility.listeners.AirportListener;
import com.beanbeanjuice.cafebot.utility.listeners.modals.SetAirportMessageModalListener;
import com.beanbeanjuice.cafebot.utility.listeners.games.tictactoe.TicTacToeListener;
import com.beanbeanjuice.cafebot.utility.listeners.modals.polls.PollListener;
import com.beanbeanjuice.cafebot.utility.listeners.modals.polls.PollModalListener;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafebot.utility.logging.LogManager;
import com.beanbeanjuice.cafebot.utility.scheduling.*;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class CafeBot {

    // Bot Items
    @Getter private final String botVersion = getVersion();
    @Getter private final String botUserAgent = "java:com.beanbeanjuice.cafeBot:" + botVersion;
    @Getter private final ShardManager shardManager;

    // API
    @Getter private final CafeAPI cafeAPI;

    // Internal Items
    @Getter private final LogManager logger;

    // Schedulers
    private final List<CustomScheduler> schedulers = new ArrayList<>();
    @Getter private MutualGuildsScheduler mutualGuildsScheduler;

    // Handlers
    private CommandHandler commandHandler;
    @Getter private MenuHandler menuHandler;
    @Getter private HelpHandler helpHandler;
    @Getter private TwitchHandler twitchHandler;
    @Getter private PollSessionHandler pollSessionHandler;
    @Getter private TicTacToeHandler ticTacToeHandler;
    @Getter private ConfessionHandler confessionHandler;
    @Getter private SnipeHandler snipeHandler;

    // Additional Items
    @Getter private final AtomicInteger commandsRun = new AtomicInteger(0); // Atomic since can run across multiple shards.
    @Getter private final String discordAvatarUrl = "https://cdn.beanbeanjuice.com/images/cafeBot/cafeBot.gif";

    public CafeBot() throws InterruptedException, ExecutionException {
        this.logger = new LogManager(
                this,
                "cafeBot Logging System",
                EnvironmentVariable.CAFEBOT_GUILD_ID.getSystemVariable(),
                EnvironmentVariable.CAFEBOT_GUILD_LOG_CHANNEL_ID.getSystemVariable(),
                "logs/"
        );
        this.cafeAPI = new CafeAPI(
                EnvironmentVariable.CAFEBOT_API_URL.getSystemVariable(),
                EnvironmentVariable.CAFEBOT_API_TOKEN.getSystemVariable()
        );

        this.logger.addWebhookURL(EnvironmentVariable.CAFEBOT_GUILD_WEBHOOK_URL.getSystemVariable());
        this.logger.log(CafeBot.class, LogLevel.OKAY, "Starting bot!", true, false);

        Greeting greeting = cafeAPI.getGreetingApi().getAdminHello().get();
        if (greeting == null || greeting.getMessage() == null) {
            this.logger.log(CafeBot.class, LogLevel.ERROR, "Connection to the API is invalid...", true, true);
        }

        this.shardManager = DefaultShardManagerBuilder
                .createDefault(EnvironmentVariable.CAFEBOT_TOKEN.getSystemVariable())
                .setActivity(Activity.playing("The barista is starting..."))
                .setStatus(OnlineStatus.IDLE)
                .enableIntents(
                        GatewayIntent.GUILD_EXPRESSIONS,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_VOICE_STATES
                )
                .setChunkingFilter(ChunkingFilter.NONE)
                .setMemberCachePolicy(MemberCachePolicy.DEFAULT)
                .addEventListeners(new BotAllShardsReadyListener(this), new BotUpdateMessageStartListener(this)) // Instantiate this before.
                .setShardsTotal(5)
                .build();

        this.logger.enableDiscordLogging();
        logger.log(CafeBot.class, LogLevel.INFO, "Enabled Discord Logging...", true, false);

        logger.log(CafeBot.class, LogLevel.INFO, "Adding commands...", true, false);
        setupCommands();
        setupSchedulers();
        this.menuHandler = new MenuHandler(this);

        setupListeners();
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
                new EmbedCommand(this),
                new HelpCommand(this),
                new PingCommand(this),
                new InfoCommand(this),
                new StatsCommand(this),
                new RemoveMyDataCommand(this),
                new SupportCommand(this),
                new WhoCommand(this),
                new TwitchCommand(this),

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
                new AiCommand(this),
                new RateCommand(this),
                new EightBallCommand(this),
                new SnipeCommand(this),

                // Games
                new CoinFlipCommand(this),
                new CountingStatisticsCommand(this),
                new RollCommand(this),
                new GameCommand(this),
                new TicTacToeCommand(this),

                // Interactions
                new InteractionCommand(this),
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
                new HideCommand(this),
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
                new ConfessCommand(this),

                // Moderation
                new ClearChatCommand(this),

                // Settings
                new AirportMessageCommand(this),
                new RoleCommand(this),
                new ChannelCommand(this),
                new RaffleCommand(this),
                new PollCommand(this),
                new BindCommand(this)
        );

        this.shardManager.addEventListener(commandHandler);
        this.helpHandler = new HelpHandler(commandHandler);
        this.twitchHandler = new TwitchHandler(EnvironmentVariable.CAFEBOT_TWITCH_ACCESS_TOKEN.getSystemVariable(), this);
        this.pollSessionHandler = new PollSessionHandler(this);
        this.ticTacToeHandler = new TicTacToeHandler(this);
        this.confessionHandler = new ConfessionHandler();
        this.snipeHandler = new SnipeHandler();
    }

    private void setupSchedulers() {
        this.getLogger().log(this.getClass(), LogLevel.INFO, "Setting up schedulers...", true, false);
        mutualGuildsScheduler = new MutualGuildsScheduler(this);

        this.schedulers.addAll(List.of(
                new DailyChannelScheduler(this),
                mutualGuildsScheduler,
                new BirthdayScheduler(this),
                new UptimeScheduler(this),
                new RaffleScheduler(this),
                new PollScheduler(this),
                new RestartScheduler(this),
                new SnipeScheduler(this)
        ));
        this.schedulers.forEach(CustomScheduler::start);
    }

    public void addEventListener(ListenerAdapter listener) {
        this.shardManager.addEventListener(listener);
    }

    public void addScheduler(CustomScheduler scheduler) {
        this.schedulers.add(scheduler);
    }

    private void setupListeners() {
        this.shardManager.addEventListener(
                new BotAddListener(this),
                new BotRemoveListener(this),
                new CountingListener(this),
                new HelpListener(commandHandler, helpHandler),
                new TicTacToeListener(this),
                new AIResponseListener(this, EnvironmentVariable.CAFEBOT_OPENAI_API_KEY.getSystemVariable(), EnvironmentVariable.CAFEBOT_OPENAI_ASSISTANT_ID.getSystemVariable()),
                new AirportListener(this.getCafeAPI()),
                new SetAirportMessageModalListener(this),
                new MutualGuildsListener(this),
                new RaffleListener(this),
                new PollListener(this),
                new PollModalListener(this),
                new VoiceRoleBindListener(this),
                new ConfessionBanListener(this),
                new HoneypotListener(this),
                new SnipeListener(this)
        );
    }

    private String getVersion() {
        String resourceName = "cafebot.properties"; // could also be a constant
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) { props.load(resourceStream); }
        catch (IOException e) { throw new RuntimeException(e); }

        return props.get("version").toString();
    }

    public int getTotalServers() {
        return Math.toIntExact(this.shardManager.getGuildCache().size());
    }

    public int getTotalChannels() {
        return this.shardManager.getTextChannels().size();
    }

    public int getTotalUsers() {
        int count = 0;

        for (JDA jda : this.shardManager.getShards()) {
            for (Guild guild : jda.getGuilds()) {
                count += guild.getMemberCount();
            }
        }

        return count;
    }

    public int getShardCount() {
        return this.shardManager.getShards().size();
    }

    public RestAction<User> getUser(String userId) {
        userId = userId.replace("<@!", "");
        userId = userId.replace("<@", ""); // Edge Case for Mobile
        userId = userId.replace(">", "");

        try {
            return this.shardManager.retrieveUserById(userId);
        } catch (NullPointerException | NumberFormatException e) {
            this.logger.log(CafeBot.class, LogLevel.ERROR, "Error getting user from cache: " + e.getMessage());
            throw e;
        }
    }

    public void pmUser(User user, MessageEmbed embed) {
        user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embed)).queue();
    }

    public void increaseCommandsRun() {
        this.commandsRun.incrementAndGet();
    }

    public SelfUser getSelfUser() {
        return this.shardManager.getShards().getFirst().getSelfUser();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        new CafeBot();
    }

}
