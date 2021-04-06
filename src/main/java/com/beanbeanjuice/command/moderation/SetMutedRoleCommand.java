package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

/**
 * A command to set the muted {@link Role}.
 *
 * @author beanbeanjuice
 */
public class SetMutedRoleCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        if (!BeanBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        event.getMessage().delete().queue();

        Role role = BeanBot.getGeneralHelper().getRole(args.get(0));

        if (!BeanBot.getGuildHandler().updateGuildModeratorRole(event.getGuild(), role)) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError());
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.green);
        embedBuilder.setAuthor("Successfully changed the Muted Role");
        embedBuilder.setDescription("Successfully changed the muted role to " + role.getAsMention());
        event.getChannel().sendMessage(embedBuilder.build()).queue();

    }

    @Override
    public String getName() {
        return "set-muted-role";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("setmutedrole");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Sets the muted role for the server.";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.ROLE, "Mentioned Role", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return null;
    }
}
