package com.beanbeanjuice.cafebot.command.generic;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ICommand} used to request to remove data.
 *
 * @author beanbeanjuice
 */
public class RemoveMyDataCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Data Removal",
                "You can request to remove your data on this bot by clicking [this](https://forms.gle/RL4EEBqosVeXLLzP7) link!"
        )).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Request to remove your data!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/remove-my-data`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.GENERIC;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

}
