package com.beanbeanjuice.command.moderation.counting;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.CommandOption;
import com.beanbeanjuice.utility.command.ISubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class RemoveCountingChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessage("Removed!").queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Remove the counting channel.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/counting-channel remove`";
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
        return null;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return false;
    }

    @NotNull
    @Override
    public String getName() {
        return "remove";
    }

}
