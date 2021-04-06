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
 * A command used for banning people.
 *
 * @author beanbeanjuice
 */
public class BanCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        event.getMessage().delete().queue();

        if (!BeanBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        Member punishee = event.getMessage().getMentionedMembers().get(0);

        StringBuilder reason = new StringBuilder();

        for (int i = 1; i < args.size(); i++) {
            reason.append(args.get(i)).append(" ");
        }

        // TODO: Not PMing users
        BeanBot.getGeneralHelper().pmUser(punishee.getUser(), "You have been banned: " + reason.toString());

        try {
            punishee.ban(Integer.parseInt(args.get(1)), reason.toString()).queue();
        } catch (HierarchyException e) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.red);
            embedBuilder.setAuthor("Unable to Ban User");
            embedBuilder.setDescription("The user you are trying to ban has a higher role than the bot.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        embedBuilder.setAuthor("User Banned");
        embedBuilder.setDescription("`" + punishee.getEffectiveName() + "` has been banned for `" + reason.toString() + "`.");
        embedBuilder.addField("Banned By:", user.getAsMention(), true);

        event.getChannel().sendMessage(embedBuilder.build()).queue();

    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Ban someone!";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "Discord Mention", true);
        usage.addUsage(CommandType.NUMBER, "Number of Days", true);
        usage.addUsage(CommandType.TEXT, "Reason for Ban", true);

        for (int i = 0; i < 100; i++) {
            usage.addUsage(CommandType.TEXT, "Reason for Ban", true);
        }

        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}
