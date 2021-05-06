package com.beanbeanjuice.command.moderation.mute;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
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
 * A command used for muting Discord {@link net.dv8tion.jda.api.entities.Member Members}.
 *
 * @author beanbeanjuice
 */
public class MuteCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!BeanBot.getGeneralHelper().checkPermission(event.getMember(), event.getChannel(), Permission.MANAGE_ROLES)) {
            return;
        }

        Role mutedRole = BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).getMutedRole();

        if (mutedRole == null) {
            event.getChannel().sendMessage(noMutedRoleEmbed()).queue();
            return;
        }

        Member punishee = event.getGuild().getMember(BeanBot.getGeneralHelper().getUser(args.get(0)));

        try {
            event.getGuild().addRoleToMember(punishee, mutedRole).queue();
        } catch (HierarchyException e) {
            event.getChannel().sendMessage(hierarchyErrorEmbed()).queue();
            return;
        }

        event.getChannel().sendMessage(successfulMuteEmbed(punishee)).queue();
    }

    @NotNull
    public MessageEmbed noMutedRoleEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.red);
        embedBuilder.setAuthor("Error Muting User");
        embedBuilder.setDescription("A muted role has not been set. Please check the help command.");
        return embedBuilder.build();
    }

    @NotNull
    public MessageEmbed successfulMuteEmbed(@NotNull Member punishee) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Muted User");
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        embedBuilder.setDescription("Muted " + punishee.getAsMention() + ".");
        return embedBuilder.build();
    }

    @NotNull
    public MessageEmbed hierarchyErrorEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Hierarchy Error");
        embedBuilder.setColor(Color.red);
        embedBuilder.setDescription("The bot needs to have a higher role than the user you are trying to mute!");
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Mute a Discord User.";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "Discord Mention", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }
}
