package com.beanbeanjuice.command.twitch;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to remove the live {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
 *
 * @author beanbeanjuice
 */
public class RemoveLiveChannelCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!CafeBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        if (ctx.getCustomGuild().updateTwitchDiscordChannel("0")) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Removed Live Channel",
                    "Successfully removed the live channel!"
            )).queue();
            return;
        }

        event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
    }

    @Override
    public String getName() {
        return "remove-live-channel";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("removelivechannel");
        arrayList.add("unset-live-channel");
        arrayList.add("unsetlivechannel");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Remove the live channel!";
    }

    @Override
    public String exampleUsage() {
        return "`!!remove-live-channel`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.TWITCH;
    }
}
