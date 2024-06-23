package com.beanbeanjuice.cafebot.command.generic;

import com.beanbeanjuice.cafebot.Bot;
import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.AuthorizationException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.CafeException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ConflictException;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.ResponseException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ICommand} used to generate codes.
 *
 * @author beanbeanjuice
 */
public class GenerateCodeCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        // Generates the code.
        String generatedCode = Helper.getRandomAlphaNumericString(32);

        // Tries to create the code for the user in the database.
        try {
            Bot.getCafeAPI().getGeneratedCodesEndpoint().createUserGeneratedCode(event.getUser().getId(), generatedCode);
        } catch (ConflictException e) {

            // If the code exists, then update it.
            try {
                Bot.getCafeAPI().getGeneratedCodesEndpoint().updateUserGeneratedCode(event.getUser().getId(), generatedCode);

                event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Generated Code",
                        "Your generated code is: `" + generatedCode + "`."
                )).queue();
            } catch (CafeException e2) {
                event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                        "Error Generating Code",
                        "There has been an error generating the code. Please try again. Error updating."
                )).queue();
                Bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Generating Code: " + e2.getMessage(), e2);
            }

            // Otherwise catch the error.
        } catch (AuthorizationException | ResponseException e) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Generating Code",
                    "There has been an error generating the code, please try again. Error creating."
            )).queue();
            Bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Generating Code: " + e.getMessage(), e);
        }
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Generate a random code!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/generate-code`";
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
