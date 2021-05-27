package com.beanbeanjuice.command.moderation.raffle;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * A command used to set the current {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} to a {@link com.beanbeanjuice.utility.sections.fun.raffle.Raffle Raffle} channel.
 *
 * @author beanbeanjuice
 */
public class SetRaffleChannelCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!CafeBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        if (CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).setRaffleChannel(event.getChannel().getId())) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Set Raffle Channel",
                    "This channel has been set to an active raffle channel!"
            )).queue();
            return;
        }
        event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
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
