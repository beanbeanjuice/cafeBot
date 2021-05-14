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
 * A command used to set the current {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} to a {@link com.beanbeanjuice.utility.raffle.Raffle Raffle} channel.
 *
 * @author beanbeanjuice
 */
public class SetRaffleChannelCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        if (!BeanBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        if (!BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).setRaffleChannel(event.getChannel().getId())) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        event.getChannel().sendMessage(BeanBot.getGeneralHelper().successEmbed(
                "Set Raffle Channel",
                "This channel has been set to an active raffle channel!"
        )).queue();

    }

    @Override
    public String getName() {
        return "set-raffle-channel";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setrafflechannel");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Set the current channel to a raffle channel.";
    }

    @Override
    public String exampleUsage() {
        return "`!!set-raffle-channel`";
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
