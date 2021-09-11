package com.beanbeanjuice.command.moderation.welcome;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.cafeapi.cafebot.welcomes.GuildWelcome;
import com.beanbeanjuice.cafeapi.exception.CafeException;
import com.beanbeanjuice.cafeapi.exception.ConflictException;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

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
        if (!CafeBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        HashMap<String, String> parsedMap = CafeBot.getGeneralHelper().createCommandTermMap(getCommandTerms(), args);
        String thumbnail = parsedMap.get("thumbnail");
        String image = parsedMap.get("image");
        String description = parsedMap.get("description");
        String message = parsedMap.get("message");

        GuildWelcome guildWelcome = new GuildWelcome(event.getGuild().getId(), description, thumbnail, image, message);

        // Sets it in the API
        if (setGuildWelcome(guildWelcome)) {
            if (guildWelcome.getMessage() != null) {
                event.getChannel().sendMessage(guildWelcome.getMessage()).setEmbeds(CafeBot.getWelcomeListener().getWelcomeEmbed(guildWelcome, user)).queue();
            } else {
                event.getChannel().sendMessageEmbeds(CafeBot.getWelcomeListener().getWelcomeEmbed(guildWelcome, user)).queue();
            }
            return;
        }

        event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError()).queue();
    }

    /**
     * Sets the {@link GuildWelcome} in the {@link com.beanbeanjuice.cafeapi.CafeAPI CafeAPI}.
     * @param guildWelcome The {@link GuildWelcome} to set.
     * @return True, if the {@link GuildWelcome} was set successfully.
     */
    @NotNull
    public Boolean setGuildWelcome(@NotNull GuildWelcome guildWelcome) {
        try {
            return CafeBot.getCafeAPI().welcomes().createGuildWelcome(guildWelcome);
        } catch (ConflictException e) {
            return CafeBot.getCafeAPI().welcomes().updateGuildWelcome(guildWelcome);
        } catch (CafeException e) {
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Setting Guild Welcome: " + e.getMessage(), e);
            return false;
        }
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
        return CategoryType.EXPERIMENTAL;
    }
}
