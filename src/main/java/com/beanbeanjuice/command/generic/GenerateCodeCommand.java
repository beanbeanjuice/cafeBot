package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.cafeapi.exception.AuthorizationException;
import com.beanbeanjuice.cafeapi.exception.CafeException;
import com.beanbeanjuice.cafeapi.exception.ConflictException;
import com.beanbeanjuice.cafeapi.exception.ResponseException;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to generate codes.
 *
 * @author beanbeanjuice
 */
public class GenerateCodeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        // Deletes the message.
        event.getMessage().delete().queue();

        // Generates the code.
        String generatedCode = CafeBot.getGeneralHelper().getRandomAlphaNumericString(32);

        // Tries to create the code for the user in the database.
        try {
            CafeBot.getCafeAPI().generatedCodes().createUserGeneratedCode(user.getId(), generatedCode);
        } catch (ConflictException e) {

            // If the code exists, then update it.
            try {
                CafeBot.getCafeAPI().generatedCodes().updateUserGeneratedCode(user.getId(), generatedCode);

                CafeBot.getGeneralHelper().pmUser(user, CafeBot.getGeneralHelper().successEmbed(
                        "Generated Code",
                        "Your Generated Code Is: `" + generatedCode + "`."
                ));
                return;
            } catch (CafeException e2) {
                CafeBot.getGeneralHelper().pmUser(user, CafeBot.getGeneralHelper().errorEmbed(
                        "Error Generating Code",
                        "There has been an error generating the code, please try again. Error Updating."
                ));
                CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Generating Code: " + e2.getMessage(), e2);
                return;
            }

            // Otherwise catch the error.
        } catch (AuthorizationException | ResponseException e) {
            CafeBot.getGeneralHelper().pmUser(user, CafeBot.getGeneralHelper().errorEmbed(
                    "Error Generating Code",
                    "There has been an error generating the code, please try again. Error creating."
            ));
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Generating Code: " + e.getMessage(), e);
            return;
        }

    }

    @Override
    public String getName() {
        return "generate-code";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("generatecode");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Generates a random 32-digit alphanumeric code!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "generate-code`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GENERIC;
    }
}
