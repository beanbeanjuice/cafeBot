package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class GetCustomChannelsCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

    }

    @NotNull
    @Override
    public String getDescription() {
        return "Retrieve all of the custom channels for this server that you have set on this bot.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/get-custom-channels`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.MODERATION;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return false;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

}
