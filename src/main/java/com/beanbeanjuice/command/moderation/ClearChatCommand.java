package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to clear a {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
 *
 * @author beanbeanjuice
 * @since v3.1.1
 */
public class ClearChatCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        int amount = event.getOption("amount").getAsInt();

        try {
            event.getMessageChannel().getIterableHistory().takeAsync(amount)
                    .thenApply(messages -> event.getChannel().purgeMessages(messages));
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Deleting Messages",
                    "Attempting to delete `" + amount + "` messages. This might take a while..."
            )).queue();
        } catch (InsufficientPermissionException e) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Deleting Messages",
                    "There was an error deleting your messages. The " +
                            "bot may have insufficient permissions. `" + e.getMessage() + "`"
            )).queue();
        }
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Clear the chat!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/clearchat 99`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "amount", "The number of messages to clear.", true));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.MODERATION;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

    @Nullable
    @Override
    public ArrayList<Permission> getPermissions() {
        ArrayList<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MESSAGE_MANAGE);
        return permissions;
    }

}
