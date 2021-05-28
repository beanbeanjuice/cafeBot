package com.beanbeanjuice.command.moderation.welcome;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.sections.moderation.welcome.GuildWelcome;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to edit the welcome {@link String}.
 *
 * @author beanbeanjuice
 */
public class EditWelcomeMessageCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        String thumbnail = null;
        String image = null;
        String description = null;

        ArrayList<String> thingsToRemove = new ArrayList<>();

        for (String string : args) {
            if (string.startsWith("thumbnail:")) {
                thumbnail = string.replace("thumbnail:", "");
                thingsToRemove.add(string);
            }

            if (string.startsWith("image:")) {
                image = string.replace("image:", "");
                thingsToRemove.add(string);
            }
        }

        for (String removeString : thingsToRemove) {
            args.remove(removeString);
        }

        if (args.size() > 0) {
            StringBuilder descriptionBuilder = new StringBuilder();
            for (int i = 0; i < args.size(); i++) {
                descriptionBuilder.append(args.get(i));

                if (i != args.size() - 1) {
                    descriptionBuilder.append(" ");
                }
            }
            description = descriptionBuilder.toString();
        }

        GuildWelcome guildWelcome = new GuildWelcome(description, thumbnail, image);

        if (CafeBot.getWelcomeHandler().setGuildWelcome(event.getGuild().getId(), guildWelcome)) {
            event.getChannel().sendMessage(CafeBot.getWelcomeListener().getWelcomeEmbed(guildWelcome, user)).queue();
            return;
        }

        event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
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
        return "Edit the welcome message! You can use `\\n` to make a new line in the description! Check the usage below for help!";
    }

    @Override
    public String exampleUsage() {
        return "`!!edit-welcome thumbnail:https://www.fakeImageurl.png image:https://www.fakeImageUrl.png2 Welcome, {user} to the server!\\nYou're cool!` or `!!edit-welcome Welcome to the server!`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.SENTENCE, "thumbnail image, big image, description", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return null;
    }
}
