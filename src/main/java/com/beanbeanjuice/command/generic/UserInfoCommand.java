package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.CafeBot;
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
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;

/**
 * An {@link ICommand} to get {@link User} information.
 *
 * @author beanbeanjuice
 */
public class UserInfoCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        Member member;
        if (args.size() == 0) {
            member = event.getMember();
        } else {
            member = event.getGuild().getMember(CafeBot.getGeneralHelper().getUser(args.get(0)));
        }

        event.getChannel().sendMessage(userInfoEmbed(member)).queue();
    }

    @NotNull
    private MessageEmbed userInfoEmbed(@NotNull Member member) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy @ h:mma");
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setThumbnail(member.getUser().getAvatarUrl());
        embedBuilder.setTitle(member.getUser().getName());
        embedBuilder.addField("Joined", member.getTimeJoined().format(formatter), true);
        embedBuilder.addField("Registered", member.getTimeCreated().format(formatter), true);
        embedBuilder.setFooter("ID: " + member.getId());
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());

        embedBuilder.setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl());

        ArrayList<Role> roles = new ArrayList<>(member.getRoles());
        StringBuilder roleBuilder = new StringBuilder();
        for (int i = 0; i < roles.size(); i++) {
            roleBuilder.append(roles.get(i).getAsMention());

            if (i != roles.size() - 1) {
                roleBuilder.append(" ");
            }
        }

        EnumSet<Permission> permissions = member.getPermissions();
        StringBuilder permissionBuilder = new StringBuilder();
        int count = 0;
        for (Permission permission : permissions) {
            permissionBuilder.append(permission.getName());
            if (count++ != permissions.size() - 1) {
                permissionBuilder.append(", ");
            }
        }

        embedBuilder.setDescription(member.getAsMention());
        embedBuilder.setTimestamp(new Date().toInstant());
        embedBuilder.addField("Roles [" + roles.size() + "]", roleBuilder.toString(), false);
        embedBuilder.addField("Permissions", permissionBuilder.toString(), false);
        return embedBuilder.build();

    }

    @Override
    public String getName() {
        return "user-info";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("whois");
        arrayList.add("who-is");
        arrayList.add("userinfo");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Get information about a user.";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "whois` or `" + prefix + "whois @beanbeanjuice`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "Discord Mention", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GENERIC;
    }

}
