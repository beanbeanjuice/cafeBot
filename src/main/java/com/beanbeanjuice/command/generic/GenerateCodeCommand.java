package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * An {@link ICommand} used to generate codes.
 *
 * @author beanbeanjuice
 */
public class GenerateCodeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        event.getMessage().delete().queue();
        Connection connection = CafeBot.getSQLServer().getConnection();
        String arguments = "REPLACE INTO cafeBot.generated_codes (user_id, generated_code) VALUES (?,?);";
        String generatedCode = CafeBot.getGeneralHelper().getRandomAlphaNumericString(32);

        try {
            PreparedStatement statement = connection.prepareStatement(arguments);
            statement.setLong(1, Long.parseLong(user.getId()));
            statement.setString(2, generatedCode);
            statement.execute();

            CafeBot.getGeneralHelper().pmUser(user, CafeBot.getGeneralHelper().successEmbed(
                    "Generated Code",
                    "Your Generated Code Is: `" + generatedCode + "`."
            ));
        } catch (SQLException e) {
            CafeBot.getGeneralHelper().pmUser(user, CafeBot.getGeneralHelper().errorEmbed(
                    "Error Generating Code",
                    "There has been an error generating the code, please try again."
            ));
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Generating Code: " + e.getMessage(), e);
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
