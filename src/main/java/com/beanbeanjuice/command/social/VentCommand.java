package com.beanbeanjuice.command.social;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to vent anonymously.
 *
 * @author beanbeanjuice
 */
public class VentCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        event.getMessage().delete().queue();
        TextChannel ventChannel = CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).getVentingChannel();

        // Making sure the venting channel exists.
        if (ventChannel == null) {
            CafeBot.getGeneralHelper().pmUser(user, CafeBot.getGeneralHelper().errorEmbed(
                    "Venting Not Enabled",
                    "The server you are trying to anonymously vent on currently does not have anonymous venting setup."
            ));
            return;
        }

        if (!CafeBot.getVentHandler().addVent(user.getId(), event.getGuild().getId())) {
            CafeBot.getGeneralHelper().pmUser(user, CafeBot.getGeneralHelper().errorEmbed(
                    "Venting Timer Started",
                    "You already have a venting timer started. To cancel the timer, do `!!cancel`."
            ));
            return;
        }

        CafeBot.getVentHandler().getVent(user.getId()).startVentTimer();
    }

    @Override
    public String getName() {
        return "vent";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("anonymously-vent");
        arrayList.add("anonymous-vent");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Anonymously vent! (If the server has that setup...)";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "vent`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.SOCIAL;
    }

}
