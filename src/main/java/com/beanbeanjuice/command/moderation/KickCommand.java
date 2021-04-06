package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.awt.*;
import java.util.ArrayList;

/**
 * A command used for kicking people.
 *
 * @author beanbeanjuice
 */
public class KickCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        event.getMessage().delete().queue();

        if (!BeanBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        Member punishee = event.getMessage().getMentionedMembers().get(0);

        StringBuilder reason = new StringBuilder();

        for (int i = 1; i < args.size(); i++) {
            reason.append(args.get(i)).append(" ");
        }

        // TODO: Not PMing users
        BeanBot.getGeneralHelper().pmUser(punishee.getUser(), "You have been kicked: " + reason.toString());

        try {
            punishee.kick(reason.toString()).queue();
        } catch (HierarchyException e) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.red);
            embedBuilder.setAuthor("Unable to Kick User");
            embedBuilder.setDescription("The user you are trying to kick has a higher role than the bot.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        embedBuilder.setAuthor("User Kicked");
        embedBuilder.setDescription("`" + punishee.getEffectiveName() + "` has been kicked for `" + reason.toString() + "`.");
        embedBuilder.addField("Kicked By:", user.getAsMention(), true);

        event.getChannel().sendMessage(embedBuilder.build()).queue();

    }

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Kick someone from the server.";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "Discord User", true);
        usage.addUsage(CommandType.TEXT, "Reason", true);

        for (int i = 0; i < 100; i++) {
            usage.addUsage(CommandType.TEXT, "Reason", false);
        }

        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}
