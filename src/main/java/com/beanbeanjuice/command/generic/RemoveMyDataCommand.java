package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to request to remove data.
 *
 * @author beanbeanjuice
 */
public class RemoveMyDataCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().successEmbed(
                "Data Removal",
                "You can request to remove your data on this bot by clicking [this](https://forms.gle/RL4EEBqosVeXLLzP7) link!"
        )).queue();
    }

    @Override
    public String getName() {
        return "remove-my-data";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("removemydata");
        arrayList.add("personal-data-removal");
        arrayList.add("personaldataremoval");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Request to remove your data!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "remove-my-data`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GENERIC;
    }
}
