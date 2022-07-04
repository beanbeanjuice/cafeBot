package com.beanbeanjuice.utility.command;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.command.cafe.*;
import com.beanbeanjuice.command.fun.AddPollCommand;
import com.beanbeanjuice.command.generic.HelpCommand;
import com.beanbeanjuice.command.generic.PingCommand;
import com.beanbeanjuice.command.interaction.*;
import com.beanbeanjuice.command.moderation.ClearChatCommand;
import com.beanbeanjuice.command.moderation.CreateEmbedCommand;
import com.beanbeanjuice.command.moderation.ListCustomChannelsCommand;
import com.beanbeanjuice.command.moderation.counting.CountingChannelCommand;
import com.beanbeanjuice.utility.logging.LogLevel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
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
        commands.put("add-poll", new AddPollCommand());

        // Games

        // Generic
        commands.put("ping", new PingCommand());
        commands.put("help", new HelpCommand());

        // Social

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

        // Twitch

        // Moderation
        commands.put("counting-channel", new CountingChannelCommand());
        commands.put("clear-chat", new ClearChatCommand());
        commands.put("create-embed", new CreateEmbedCommand());
        commands.put("list-custom-channels", new ListCustomChannelsCommand());

        // =======================
        //     END OF COMMANDS
        // =======================

        commands.forEach((commandName, command) -> {
            SlashCommandData slashCommandData = Commands.slash(commandName, command.getDescription());
            slashCommandData.setGuildOnly(!command.allowDM());
            slashCommandData.addOptions(command.getOptions());

            // Setting the permissions for commands.
            if (command.getPermissions() != null) {
                slashCommandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(command.getPermissions()));
            }

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

        if (commands.containsKey(event.getName())) {

            if (commands.get(event.getName()).isHidden()) {
                event.deferReply(true).queue();
            } else {
                event.deferReply().queue();
            }

            if (event.getSubcommandName() != null) {
                commands.get(event.getName()).runSubCommand(event.getSubcommandName(), event);
            } else {
                commands.get(event.getName()).handle(event);
            }

            Bot.commandsRun++;
        }
    }

    @NotNull
    public TreeMap<String, ICommand> getCommands() {
        return commands;
    }

}
