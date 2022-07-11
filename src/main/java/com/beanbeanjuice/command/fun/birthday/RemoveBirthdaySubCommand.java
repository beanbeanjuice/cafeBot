package com.beanbeanjuice.command.fun.birthday;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.section.fun.BirthdayHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ISubCommand} used to remove a {@link io.github.beanbeanjuice.cafeapi.cafebot.birthdays.Birthday Birthday}.
 *
 * @author beanbeanjuice
 */
public class RemoveBirthdaySubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if (BirthdayHandler.removeBirthday(event.getUser().getId())) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Removed Birthday",
                    "Successfully removed your birthday!"
            )).queue();
            return;
        }

        event.getHook().sendMessageEmbeds(Helper.sqlServerError()).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Remove your birthday!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/birthday remove`";
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.FUN;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

    @NotNull
    @Override
    public String getName() {
        return "remove";
    }

}
