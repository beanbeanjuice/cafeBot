package com.beanbeanjuice.command.moderation.counting;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.CommandOption;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.ISubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class CountingChannelCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        String subCommandName = event.getSubcommandName();

        if (subCommandName.equals("set")) {
            new SetCountingChannelSubCommand().handle(event);
        } else if (subCommandName.equals("remove")) {
            new RemoveCountingChannelSubCommand().handle(event);
        }
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set or remove the counting channel.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/counting-channel set` or `/counting-channel remove`";
    }

    @Nullable
    @Override
    public ArrayList<CommandOption> getOptions() {
        return null;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.MODERATION;
    }

    @Nullable
    @Override
    public ArrayList<ISubCommand> getSubCommands() {
        ArrayList<ISubCommand> subCommands = new ArrayList<>();
        subCommands.add(new SetCountingChannelSubCommand());
        subCommands.add(new RemoveCountingChannelSubCommand());
        return subCommands;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return false;
    }

}
