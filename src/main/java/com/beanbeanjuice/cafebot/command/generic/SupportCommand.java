package com.beanbeanjuice.cafebot.command.generic;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ICommand} used for support.
 *
 * @author beanbeanjuice
 */
public class SupportCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessage("Join this server for support! https://discord.gg/KrUFw3uHST").queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Get support with the bot!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/support`";
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
