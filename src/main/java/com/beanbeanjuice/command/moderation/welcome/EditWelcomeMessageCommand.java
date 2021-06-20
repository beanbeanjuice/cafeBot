package com.beanbeanjuice.command.moderation.welcome;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.sections.moderation.welcome.GuildWelcome;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An {@link ICommand} used to edit the welcome {@link String}.
 *
 * @author beanbeanjuice
 */
public class EditWelcomeMessageCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        HashMap<String, String> parsedMap = CafeBot.getGeneralHelper().parseUnderscores(getCommandTerms(), args);
        String thumbnail = parsedMap.get("thumbnail");
        String image = parsedMap.get("image");
        String description = parsedMap.get("description");
        String message = parsedMap.get("message");

        GuildWelcome guildWelcome = new GuildWelcome(description, thumbnail, image, message);

        if (CafeBot.getWelcomeHandler().setGuildWelcome(event.getGuild().getId(), guildWelcome)) {
            event.getChannel().sendMessage(guildWelcome.getMessage()).embed(CafeBot.getWelcomeListener().getWelcomeEmbed(guildWelcome, user)).queue();
            return;
        }

        event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
    }

    private ArrayList<String> getCommandTerms() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("thumbnail");
        arrayList.add("image");
        arrayList.add("description");
        arrayList.add("message");
        return arrayList;
    }

    @Override
    public String getName() {
        return "edit-welcome-message";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("editwelcomemessage");
        arrayList.add("edit-welcome");
        arrayList.add("editwelcome");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Edit the welcome message! You can use `\\n` to make a new line in the description! Check the usage above for help!\n" +
                "Command terms are `message`, `description`, `image`, and `thumbnail`.";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "edit-welcome message:@awesomerole, someone joined the server! thumbnail:https://www.fakeImageurl.png image:https://www.fakeImageUrl.png2 description:Welcome, {user} to the server!\\nYou're cool!` or `" + prefix + "edit-welcome description:Welcome to the server!`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.SENTENCE, "Welcome Information from Example", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}
