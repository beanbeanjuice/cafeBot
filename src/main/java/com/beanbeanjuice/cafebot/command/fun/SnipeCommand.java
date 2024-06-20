package com.beanbeanjuice.cafebot.command.fun;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.handler.snipe.SnipeHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ICommand} to use with the {@link SnipeHandler SnipeHandler}.
 *
 * @author beanbeanjuice
 * @since v3.1.0
 */
public class SnipeCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        MessageEmbed embed = SnipeHandler.getLatestSnipe(event.getChannel().getId());

        if (embed == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "No Snipe",
                    "There was nothing to snipe!"
            )).queue();
            return;
        }

        event.getHook().sendMessageEmbeds(embed).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Snipe a message in this channel!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/snipe`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.FUN;
    }

}
