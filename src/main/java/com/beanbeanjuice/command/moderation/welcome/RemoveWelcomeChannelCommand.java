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
 * An {@link ICommand} used to remove the welcome {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
 *
 * @author beanbeanjuice
 */
public class RemoveWelcomeChannelCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!CafeBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        if (CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setWelcomeChannelID("0")) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Removed Welcome Channel",
                    "The welcome channel has been successfully removed!"
            )).queue();
            return;
        }
        event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
    }

    @Override
    public String getName() {
        return "remove-welcome-channel";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("removewelcomechannel");
        arrayList.add("unset-welcome-channel");
        arrayList.add("unsetwelcomechannel");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Remove the welcome channel!";
    }

    @Override
    public String exampleUsage() {
        return "`!!unset-welcome-channel";
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
