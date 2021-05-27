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
 * An {@link ICommand} used for kicking people.
 *
 * @author beanbeanjuice
 */
public class KickCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!CafeBot.getGeneralHelper().checkPermission(event.getMember(), event.getChannel(), Permission.KICK_MEMBERS)) {
            return;
        }

        User punishee = CafeBot.getGeneralHelper().getUser(args.get(0));
        StringBuilder reason = new StringBuilder();

        // No Reason
        if (args.size() == 1) {
            reason.append("No reason was specified.");
        }

        // With Reason
        if (args.size() >= 2) {
            for (int i = 1; i < args.size(); i++) {
                reason.append(args.get(i));
                if (i != args.size() - 1) {
                    reason.append(" ");
                }
            }
        }

        try {
            ctx.getGuild().getMember(punishee).kick(reason.toString()).queue();
        } catch (HierarchyException e) {
            event.getChannel().sendMessage(hierarchyErrorEmbed()).queue();
            return;
        }

        CafeBot.getGeneralHelper().pmUser(punishee, "You have been kicked: " + reason.toString());
        event.getChannel().sendMessage(successfulKickEmbed(punishee, user, reason.toString())).queue();
    }

    @NotNull
    private MessageEmbed successfulKickEmbed(@NotNull User punishee, @NotNull User punisher, @NotNull String reason) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setTitle("User Kicked");
        embedBuilder.setDescription("`" + punishee.getName() + "` has been kicked for `" + reason + "`.");
        embedBuilder.addField("Kicked By:", punisher.getAsMention(), true);
        embedBuilder.setThumbnail(punishee.getAvatarUrl());
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed hierarchyErrorEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.red);
        embedBuilder.setTitle("Unable to Kick User");
        embedBuilder.setDescription("The user you are trying to kick has a higher role than the bot.");
        return embedBuilder.build();
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
    public String exampleUsage() {
        return "`!!kick @beanbeanjuice you're trash`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "Discord User", true);
        usage.addUsage(CommandType.SENTENCE, "Reason", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}
