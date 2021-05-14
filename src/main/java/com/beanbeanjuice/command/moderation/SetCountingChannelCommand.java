package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * A command used for setting the counting {@link net.dv8tion.jda.api.entities.TextChannel TextChannel}.
 *
 * @author beanbeanjuice
 */
public class SetCountingChannelCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        if (!BeanBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        if (BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).setCountingChannel(event.getChannel())) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().successEmbed(
                    "Updated Counting Channel",
                    "Successfully set the counting channel to this channel. " +
                            "To remove counting, just delete the channel."
            )).queue();

            BeanBot.getCountingHelper().createNewRow(event.getGuild());
            return;
        }

        event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                "Error Updating Counting Channel",
                "There was an error updating the counting channel. Please try again."
        )).queue();

    }

    @Override
    public String getName() {
        return "set-counting-channel";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setcountingchannel");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Set the current channel to the active counting channel.";
    }

    @Override
    public String exampleUsage() {
        return "`!!setcountingchannel`";
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
