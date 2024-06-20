package com.beanbeanjuice.cafebot.utility.command;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafebot.command.cafe.*;
import com.beanbeanjuice.cafebot.command.fun.*;
import com.beanbeanjuice.cafebot.command.games.*;
import com.beanbeanjuice.cafebot.command.generic.*;
import com.beanbeanjuice.cafebot.command.interaction.*;
import com.beanbeanjuice.cafebot.command.fun.birthday.BirthdayCommand;
import com.beanbeanjuice.cafebot.command.fun.rate.RateCommand;
import com.beanbeanjuice.cafebot.command.moderation.PollCommand;
import com.beanbeanjuice.cafebot.command.moderation.ClearChatCommand;
import com.beanbeanjuice.cafebot.command.moderation.EmbedCommand;
import com.beanbeanjuice.cafebot.command.moderation.bind.BindCommand;
import com.beanbeanjuice.cafebot.command.settings.AICommand;
import com.beanbeanjuice.cafebot.command.settings.ListCustomChannelsCommand;
import com.beanbeanjuice.cafebot.command.settings.birthday.BirthdayChannelCommand;
import com.beanbeanjuice.cafebot.command.settings.counting.CountingChannelCommand;
import com.beanbeanjuice.cafebot.command.settings.daily.DailyChannelCommand;
import com.beanbeanjuice.cafebot.command.settings.goodbye.GoodbyeChannelCommand;
import com.beanbeanjuice.cafebot.command.settings.logging.LogChannelCommand;
import com.beanbeanjuice.cafebot.command.settings.poll.PollChannelCommand;
import com.beanbeanjuice.cafebot.command.moderation.RaffleCommand;
import com.beanbeanjuice.cafebot.command.settings.raffle.RaffleChannelCommand;
import com.beanbeanjuice.cafebot.command.settings.twitch.TwitchNotificationsCommand;
import com.beanbeanjuice.cafebot.command.settings.update.BotUpdateCommand;
import com.beanbeanjuice.cafebot.command.settings.venting.VentingChannelCommand;
import com.beanbeanjuice.cafebot.command.settings.welcome.WelcomeChannelCommand;
import com.beanbeanjuice.cafebot.command.social.CountMembersCommand;
import com.beanbeanjuice.cafebot.command.social.VentCommand;
import com.beanbeanjuice.cafebot.command.twitch.TwitchChannelCommand;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class CommandHandler extends ListenerAdapter {
    private final TreeMap<String, ICommand> commands;

    public CommandHandler(@NotNull JDA jda) {
        commands = new TreeMap<>();
        List<SlashCommandData> slashCommands = new ArrayList<>();

        // ========================
        //     COMMANDS GO HERE
        // ========================

        // Cafe
        commands.put("balance", new BalanceCommand());
        commands.put("donate-beancoins", new DonateBeanCoinsCommand());
        commands.put("menu", new MenuCommand());
        commands.put("order", new OrderCommand());
        commands.put("serve", new ServeCommand());

        // Fun
        commands.put("rate", new RateCommand());
        commands.put("birthday", new BirthdayCommand());
        commands.put("avatar", new AvatarCommand());
        commands.put("banner", new BannerCommand());
        commands.put("choose-random", new ChooseRandom());
        commands.put("coffee-meme", new CoffeeMemeCommand());
        commands.put("counting-statistics", new CountingStatisticsCommand());
        commands.put("joke", new JokeCommand());
        commands.put("meme", new MemeCommand());
        commands.put("snipe", new SnipeCommand());
        commands.put("tea-meme", new TeaMemeCommand());
        commands.put("8ball", new EightBallCommand());

        // Games
        commands.put("coin-flip", new CoinFlipCommand());
        commands.put("connect-4", new ConnectFourCommand());
        commands.put("dice-roll", new DiceRollCommand());
        commands.put("get-game-data", new GetGameDataCommand());
        commands.put("tic-tac-toe", new TicTacToeCommand());

        // Generic
        commands.put("bot-donate", new BotDonateCommand());
        commands.put("bot-invite", new BotInviteCommand());
        commands.put("bot-upvote", new BotUpvoteCommand());
        commands.put("bot-version", new BotVersionCommand());
        commands.put("bug-report", new BugReportCommand());
        commands.put("define", new DefineCommand());
        commands.put("feature-request", new FeatureRequestCommand());
        commands.put("generate-code", new GenerateCodeCommand());
        commands.put("help", new HelpCommand());
        commands.put("info", new InfoCommand());
        commands.put("ping", new PingCommand());
        commands.put("remove-my-data", new RemoveMyDataCommand());
        commands.put("stats", new StatsCommand());
        commands.put("support", new SupportCommand());
        commands.put("whois", new WhoIsCommand());

        // Interaction
        commands.put("amaze", new AmazedCommand());
        commands.put("ask", new AskCommand());
        commands.put("bite", new BiteCommand());
        commands.put("blush", new BlushCommand());
        commands.put("bonk", new BonkCommand());
        commands.put("boop", new BoopCommand());
        commands.put("cry", new CryCommand());
        commands.put("cuddle", new CuddleCommand());
        commands.put("dab", new DabCommand());
        commands.put("dance", new DanceCommand());
        commands.put("die", new DieCommand());
        commands.put("headpat", new HeadpatCommand());
        commands.put("hmph", new HmphCommand());
        commands.put("hug", new HugCommand());
        commands.put("kiss", new KissCommand());
        commands.put("lick", new LickCommand());
        commands.put("love", new LoveCommand());
        commands.put("nom", new NomCommand());
        commands.put("nosebleed", new NosebleedCommand());
        commands.put("ok", new OkCommand());
        commands.put("poke", new PokeCommand());
        commands.put("pout", new PoutCommand());
        commands.put("punch", new PunchCommand());
        commands.put("rage", new RageCommand());
        commands.put("shoot", new ShootCommand());
        commands.put("shush", new ShushCommand());
        commands.put("slap", new SlapCommand());
        commands.put("sleep", new SleepCommand());
        commands.put("smile", new SmileCommand());
        commands.put("stab", new StabCommand());
        commands.put("stare", new StareCommand());
        commands.put("throw", new ThrowCommand());
        commands.put("tickle", new TickleCommand());
        commands.put("uwu", new UwUCommand());
        commands.put("wave", new WaveCommand());
        commands.put("welcome", new WelcomeCommand());
        commands.put("wink", new WinkCommand());
        commands.put("yell", new YellCommand());

        // Moderation
        commands.put("bind", new BindCommand());
        commands.put("poll", new PollCommand());
        commands.put("raffle", new RaffleCommand());
        commands.put("clear-chat", new ClearChatCommand());
        commands.put("embed", new EmbedCommand());

        // Settings
        commands.put("birthday-channel", new BirthdayChannelCommand());
        commands.put("counting-channel", new CountingChannelCommand());
        commands.put("daily-channel", new DailyChannelCommand());
        commands.put("log-channel", new LogChannelCommand());
        commands.put("poll-channel", new PollChannelCommand());
        commands.put("raffle-channel", new RaffleChannelCommand());
        commands.put("twitch-notifications", new TwitchNotificationsCommand());
        commands.put("bot-update", new BotUpdateCommand());
        commands.put("venting-channel", new VentingChannelCommand());
        commands.put("welcome-channel", new WelcomeChannelCommand());
        commands.put("goodbye-channel", new GoodbyeChannelCommand());
        commands.put("ai", new AICommand());
        commands.put("list-custom-channels", new ListCustomChannelsCommand());

        // Social
        commands.put("count-members", new CountMembersCommand());
        commands.put("vent", new VentCommand());

        // Twitch
        commands.put("twitch-channel", new TwitchChannelCommand());

        // =======================
        //     END OF COMMANDS
        // =======================

        commands.forEach((commandName, command) -> {
            SlashCommandData slashCommandData = Commands.slash(commandName, command.getDescription());
            slashCommandData.setGuildOnly(!command.allowDM());

            switch (command.getType()) {
                case NORMAL -> {
                    // Adding the command options.
                    slashCommandData.addOptions(command.getOptions());

                    // Adding any sub commands that may exist.
                    List<SubcommandData> subCommands = new ArrayList<>();

                    for (ISubCommand subCommand : command.getSubCommands()) {
                        // TODO: Check if subcommand is a modal.
                        SubcommandData subCommandData = new SubcommandData(subCommand.getName(), subCommand.getDescription());
                        subCommandData.addOptions(subCommand.getOptions());
                        subCommands.add(subCommandData);
                    }

                    slashCommandData.addSubcommands(subCommands);
                }

                case MODAL -> {
                    // Do nothing. TODO: Remove this?
                }
            }

            // Setting the permissions for commands.
            if (command.getPermissions() != null)
                slashCommandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(command.getPermissions()));

            slashCommands.add(slashCommandData);
        });

        jda.updateCommands().addCommands(slashCommands).queue((e) -> {
            Bot.getLogger().log(this.getClass(), LogLevel.INFO, "Waiting for slash commands to propagate.", false, false);
        });
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        // Checking if the commands is something that should be run.
        if (commands.containsKey(event.getName())) {
            ICommand command = commands.get(event.getName());

            // Log the command.
            logCommand(event);

            // Checks if the reply should be hidden or not.

            if (command.getType() == CommandType.NORMAL) {
                if (command.isHidden())
                    event.deferReply(true).queue();
                else
                    event.deferReply().queue();
            }

            // Checks if it IS a sub command.
            if (event.getSubcommandName() != null)
                command.runSubCommand(event.getSubcommandName(), event);
            else
                command.handle(event);

            // Increment commands run for this bot.
            Bot.commandsRun++;
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        super.onModalInteraction(event);

        Bot.getLogger().log(CommandHandler.class, LogLevel.DEBUG, event.getModalId());

        if (commands.containsKey(event.getModalId().replace("-modal", ""))) {

            ICommand command = commands.get(event.getModalId().replace("-modal", ""));

            // Checks if the reply should be hidden.
            if (command.isHidden())
                event.deferReply(true).queue();
            else
                event.deferReply().queue();

            command.handleModal(event);
        }
    }

    private void logCommand(@NotNull SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        StringBuilder commandString = new StringBuilder();

        commandString.append("<").append(event.getUser().getId()).append("> /")
                        .append(commandName);

        if (event.getSubcommandName() != null)
            commandString.append(" ").append(event.getSubcommandName());

        for (int i = 0; i < event.getOptions().size(); i++) {
            OptionMapping optionMapping = event.getOptions().get(i);

            // Check if the mapping is null
            if (optionMapping != null) {
                String type = optionMapping.getType().toString();
                String value = optionMapping.getAsString();
                commandString.append(" <").append(type).append(":").append(value).append(">");
            }
        }

        Bot.getLogger().log(commands.get(event.getName()).getClass(), LogLevel.DEBUG, commandString.toString(), false, false);
    }

    @NotNull
    public TreeMap<String, ICommand> getCommands() {
        return commands;
    }

}
