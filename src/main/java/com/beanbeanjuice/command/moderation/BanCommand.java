package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

/**
 * An {@link ICommand} used for banning people.
 *
 * @author beanbeanjuice
 */
public class BanCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!CafeBot.getGeneralHelper().checkPermission(event.getMember(), event.getChannel(), Permission.BAN_MEMBERS)) {
            return;
        }

        User punishee = CafeBot.getGeneralHelper().getUser(args.get(0));
        StringBuilder reason = new StringBuilder();

        // No Reason
        if (args.size() == 1) {
            try {
                ctx.getGuild().getMember(punishee).ban(0).queue();
            } catch (HierarchyException e) {
                event.getChannel().sendMessage(hierarchyErrorEmbed()).queue();
                return;
            }
            reason.append("No reason was specified for the ban.");
        }

        // With Reason
        if (args.size() >= 2) {
            try {
                ctx.getGuild().getMember(punishee).ban(0, reason.toString()).queue();
            } catch (HierarchyException e) {
                event.getChannel().sendMessage(hierarchyErrorEmbed()).queue();
                return;
            }

            for (int i = 1; i < args.size(); i++) {
                reason.append(args.get(i));

                if (i != args.size() - 1) {
                    reason.append(" ");
                }
            }
        }

        event.getChannel().sendMessage(successfulBanWithReasonEmbed(punishee, user, reason.toString())).queue();
        CafeBot.getGeneralHelper().pmUser(punishee, "You have been banned: " + reason.toString());
    }

    @NotNull
    private MessageEmbed successfulBanWithReasonEmbed(@NotNull User punishee, @NotNull User punisher, @NotNull String reason) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setTitle("User Banned");
        embedBuilder.setDescription("`" + punishee.getName() + "` has been banned for `" + reason + "`.");
        embedBuilder.addField("Banned By:", punisher.getAsMention(), true);
        embedBuilder.setThumbnail(punishee.getAvatarUrl());
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed hierarchyErrorEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.red);
        embedBuilder.setTitle("Unable to Ban User");
        embedBuilder.setDescription("The user you are trying to ban has a higher role than the bot.");
        return embedBuilder.build();
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
    public String exampleUsage() {
        return "`!!ban @beanbeanjuice 13 you are trash`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "Discord Mention", true);
        usage.addUsage(CommandType.SENTENCE, "Reason for Ban", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}
