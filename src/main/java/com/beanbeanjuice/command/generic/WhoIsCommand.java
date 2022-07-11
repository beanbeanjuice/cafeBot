package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
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
public class WhoIsCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (event.getOption("user") != null)
            member = event.getOption("user").getAsMember();

        event.getHook().sendMessageEmbeds(userInfoEmbed(member)).queue();
    }

    @NotNull
    private MessageEmbed userInfoEmbed(@NotNull Member member) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy @ h:mma");
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setThumbnail(member.getUser().getAvatarUrl())
                .setTitle(member.getUser().getName())
                .addField("Joined", member.getTimeJoined().format(formatter), true)
                .addField("Registered", member.getTimeCreated().format(formatter), true)
                .setFooter("ID: " + member.getId())
                .setColor(Helper.getRandomColor())
                .setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl());

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

        embedBuilder.setDescription(member.getAsMention())
                .setTimestamp(new Date().toInstant())
                .addField("Roles [" + roles.size() + "]", roleBuilder.toString(), false)
                .addField("Permissions", permissionBuilder.toString(), false);
        return embedBuilder.build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Get information on a specified user!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/who-is`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "The user to get information of.", false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.GENERIC;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

}
