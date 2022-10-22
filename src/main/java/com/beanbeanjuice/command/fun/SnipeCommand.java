package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.handler.snipe.SnipeHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ICommand} to use with the {@link com.beanbeanjuice.utility.handler.snipe.SnipeHandler SnipeHandler}.
 *
 * @author beanbeanjuice
 * @since v3.1.0
 */
public class SnipeCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        // Checking if a text channel.
        if (!Helper.isTextChannel(event.getChannel())) {
            event.getHook().sendMessageEmbeds(Helper.notATextChannelEmbed(event.getChannelType())).queue();
            return;
        }

        MessageEmbed embed = SnipeHandler.getLatestSnipe(event.getChannel().asTextChannel().getId());

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
