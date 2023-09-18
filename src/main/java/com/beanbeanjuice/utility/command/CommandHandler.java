package com.beanbeanjuice.utility.command;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.command.cafe.*;
import com.beanbeanjuice.command.fun.*;
import com.beanbeanjuice.command.fun.birthday.BirthdayCommand;
import com.beanbeanjuice.command.fun.rate.RateCommand;
import com.beanbeanjuice.command.games.*;
import com.beanbeanjuice.command.generic.*;
import com.beanbeanjuice.command.moderation.AddPollCommand;
import com.beanbeanjuice.command.interaction.*;
import com.beanbeanjuice.command.moderation.ClearChatCommand;
import com.beanbeanjuice.command.moderation.CreateEmbedCommand;
import com.beanbeanjuice.command.moderation.bind.BindCommand;
import com.beanbeanjuice.command.settings.AiCommand;
import com.beanbeanjuice.command.settings.ListCustomChannelsCommand;
import com.beanbeanjuice.command.settings.birthday.BirthdayChannelCommand;
import com.beanbeanjuice.command.settings.counting.CountingChannelCommand;
import com.beanbeanjuice.command.settings.daily.DailyChannelCommand;
import com.beanbeanjuice.command.settings.goodbye.GoodbyeChannelCommand;
import com.beanbeanjuice.command.settings.logging.LogChannelCommand;
import com.beanbeanjuice.command.settings.poll.PollChannelCommand;
import com.beanbeanjuice.command.moderation.AddRaffleCommand;
import com.beanbeanjuice.command.settings.raffle.RaffleChannelCommand;
import com.beanbeanjuice.command.settings.twitch.TwitchNotificationsCommand;
import com.beanbeanjuice.command.settings.update.BotUpdateCommand;
import com.beanbeanjuice.command.settings.venting.VentingChannelCommand;
import com.beanbeanjuice.command.settings.welcome.WelcomeChannelCommand;
import com.beanbeanjuice.command.social.CountMembersCommand;
import com.beanbeanjuice.command.social.VentCommand;
import com.beanbeanjuice.command.twitch.TwitchChannelCommand;
import com.beanbeanjuice.utility.logging.LogLevel;
import net.dv8tion.jda.api.JDA;
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
        commands.put("bite", new BiteCommand());
        commands.put("blush", new BlushCommand());
        commands.put("bonk", new BonkCommand());
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
        commands.put("nom", new NomCommand());
        commands.put("poke", new PokeCommand());
        commands.put("pout", new PoutCommand());
        commands.put("punch", new PunchCommand());
        commands.put("rage", new RageCommand());
        commands.put("shush", new ShushCommand());
        commands.put("slap", new SlapCommand());
        commands.put("sleep", new SleepCommand());
        commands.put("smile", new SmileCommand());
        commands.put("stab", new StabCommand());
        commands.put("stare", new StareCommand());
        commands.put("throw", new ThrowCommand());
        commands.put("tickle", new TickleCommand());
        commands.put("welcome", new WelcomeCommand());
        commands.put("yell", new YellCommand());

        // Moderation
        commands.put("bind", new BindCommand());
        commands.put("add-poll", new AddPollCommand());
        commands.put("add-raffle", new AddRaffleCommand());
        commands.put("clear-chat", new ClearChatCommand());
        commands.put("create-embed", new CreateEmbedCommand());

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
        commands.put("ai", new AiCommand());
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
            slashCommandData.addOptions(command.getOptions());

            // Setting the permissions for commands.
            if (command.getPermissions() != null)
                slashCommandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(command.getPermissions()));

            List<SubcommandData> subCommands = new ArrayList<>();

            for (ISubCommand subCommand : command.getSubCommands()) {
                SubcommandData subCommandData = new SubcommandData(subCommand.getName(), subCommand.getDescription());
                subCommandData.addOptions(subCommand.getOptions());
                subCommands.add(subCommandData);
            }

            // Adding sub commands if applicable.
            slashCommandData.addSubcommands(subCommands);
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

            // Log the command.
            logCommand(event);

            // Checks if the reply should be hidden or not.
            if (commands.get(event.getName()).isHidden())
                event.deferReply(true).queue();
            else
                event.deferReply().queue();

            // Checks if it IS a sub command.
            if (event.getSubcommandName() != null)
                commands.get(event.getName()).runSubCommand(event.getSubcommandName(), event);
            else
                commands.get(event.getName()).handle(event);

            // Increment commands run for this bot.
            Bot.commandsRun++;
        }
    }

    private void logCommand(@NotNull SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        StringBuilder commandString = new StringBuilder(commandName);

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
