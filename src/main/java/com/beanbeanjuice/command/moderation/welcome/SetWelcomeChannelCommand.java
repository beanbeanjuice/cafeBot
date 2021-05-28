package com.beanbeanjuice.command.moderation.welcome;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to update the welcome {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
 *
 * @author beanbeanjuice
 */
public class SetWelcomeChannelCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!CafeBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        if (CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setWelcomeChannelID(event.getChannel().getId())) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Set Welcome Channel",
                    "This channel has been set to the welcome channel! Make sure to " +
                            "edit the welcome message with the `edit-welcome-message` command!"
            )).queue();
            return;
        }
        event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
    }

    @Override
    public String getName() {
        return "set-welcome-channel";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setwelcomechannel");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Set the welcome channel!";
    }

    @Override
    public String exampleUsage() {
        return "`!!set-welcome-channel`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}
