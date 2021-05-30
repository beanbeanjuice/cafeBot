package com.beanbeanjuice.command.moderation.mute;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

/**
 * A command used to unmute {@link net.dv8tion.jda.api.entities.Member Members}.
 *
 * @author beanbeanjuice
 */
public class UnMuteCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!CafeBot.getGeneralHelper().checkPermission(event.getMember(), event.getChannel(), Permission.MANAGE_ROLES)) {
            return;
        }

        Role mutedRole = CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).getMutedRole();

        if (mutedRole == null) {
            event.getChannel().sendMessage(noMutedRoleEmbed()).queue();
            return;
        }

        Member punishee = event.getGuild().getMember(CafeBot.getGeneralHelper().getUser(args.get(0)));

        try {
            event.getGuild().removeRoleFromMember(punishee, mutedRole).queue();
        } catch (HierarchyException e) {
            event.getChannel().sendMessage(hierarchyErrorEmbed()).queue();
            return;
        }

        event.getChannel().sendMessage(successfulUnmuteEmbed(punishee)).queue();
        CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).log(this, LogLevel.INFO, "User Un-Muted", "`" + punishee.getUser().getAsTag() + "` was un-muted by " +
                user.getAsMention() + ".");
    }

    @NotNull
    private MessageEmbed successfulUnmuteEmbed(@NotNull Member punishee) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Unmuted User");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setDescription("Unmuted " + punishee.getAsMention() + ".");
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed noMutedRoleEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.red);
        embedBuilder.setTitle("Error Un-Muting User");
        embedBuilder.setDescription("A muted role has not been set. Please check the help command.");
        return embedBuilder.build();
    }

    @NotNull
    private MessageEmbed hierarchyErrorEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Hierarchy Error");
        embedBuilder.setColor(Color.red);
        embedBuilder.setDescription("The bot needs to have a higher role than the user you are trying to unmute!");
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "un-mute";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("unmute");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Unmute a Discord user!";
    }

    @Override
    public String exampleUsage() {
        return "`!!unmute @beanbeanjuice`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "Discord User Mention", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}
