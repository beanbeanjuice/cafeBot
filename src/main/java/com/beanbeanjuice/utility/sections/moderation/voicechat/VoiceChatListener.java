package com.beanbeanjuice.utility.sections.moderation.voicechat;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.command.moderation.voicebind.VoiceRoleBindCommand;
import com.beanbeanjuice.utility.logger.LogLevel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A custom {@link ListenerAdapter} for listening to when a user joins a {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} that
 * has a {@link net.dv8tion.jda.api.entities.Role} bounded to it.
 *
 * @author beanbeanjuice
 */
public class VoiceChatListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        ArrayList<String> roleIDs = new ArrayList<>(CafeBot.getVoiceChatRoleBindHandler().getBoundRoles(event.getGuild().getId(), event.getChannelLeft().getId()));

        for (String roleID : roleIDs) {
            try {
                event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleID)).queue();
            } catch (NullPointerException | IllegalArgumentException e) {
                CafeBot.getVoiceChatRoleBindHandler().unBind(event.getGuild().getId(), event.getChannelLeft().getId(), roleID);
            } catch (InsufficientPermissionException e) {
                CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).log(new VoiceRoleBindCommand(), LogLevel.ERROR, "Insufficient Permissions", "The role I am trying to remove from a user is higher than the bot.");
            }
        }

        roleIDs = new ArrayList<>(CafeBot.getVoiceChatRoleBindHandler().getBoundRoles(event.getGuild().getId(), event.getChannelJoined().getId()));

        for (String roleID : roleIDs) {
            try {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleID)).queue();
            } catch (NullPointerException | IllegalArgumentException e) {
                CafeBot.getVoiceChatRoleBindHandler().unBind(event.getGuild().getId(), event.getChannelJoined().getId(), roleID);
            } catch (InsufficientPermissionException e) {
                CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).log(new VoiceRoleBindCommand(), LogLevel.ERROR, "Insufficient Permissions", "The role I am trying to add to a user is higher than the bot.");
            }
        }
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        ArrayList<String> roleIDs = new ArrayList<>(CafeBot.getVoiceChatRoleBindHandler().getBoundRoles(event.getGuild().getId(), event.getChannelJoined().getId()));

        for (String roleID : roleIDs) {
            try {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleID)).queue();
            } catch (NullPointerException | IllegalArgumentException e) {
                CafeBot.getVoiceChatRoleBindHandler().unBind(event.getGuild().getId(), event.getChannelJoined().getId(), roleID);
            } catch (InsufficientPermissionException e) {
                CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).log(new VoiceRoleBindCommand(), LogLevel.ERROR, "Insufficient Permissions", "The role I am trying to add to a user is higher than the bot.");
            }
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        ArrayList<String> roleIDs = new ArrayList<>(CafeBot.getVoiceChatRoleBindHandler().getBoundRoles(event.getGuild().getId(), event.getChannelLeft().getId()));

        for (String roleID : roleIDs) {
            try {
                event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleID)).queue();
            } catch (NullPointerException | IllegalArgumentException e) {
                CafeBot.getVoiceChatRoleBindHandler().unBind(event.getGuild().getId(), event.getChannelLeft().getId(), roleID);
            } catch (InsufficientPermissionException e) {
                CafeBot.getGuildHandler().getCustomGuild(event.getGuild()).log(new VoiceRoleBindCommand(), LogLevel.ERROR, "Insufficient Permissions", "The role I am trying to remove from a user is higher than the bot.");
            }
        }
    }
}
