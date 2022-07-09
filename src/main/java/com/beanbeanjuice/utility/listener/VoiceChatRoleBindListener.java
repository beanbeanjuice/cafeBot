package com.beanbeanjuice.utility.listener;

import com.beanbeanjuice.command.moderation.bind.BindCommand;
import com.beanbeanjuice.utility.handler.VoiceChatRoleBindHandler;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.logging.LogLevel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A custom {@link ListenerAdapter} for listening to when a user joins a {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel} that
 * has a {@link net.dv8tion.jda.api.entities.Role Role} bounded to it.
 *
 * @author beanbeanjuice
 */
public class VoiceChatRoleBindListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        ArrayList<String> fromIDs = new ArrayList<>(VoiceChatRoleBindHandler.getBoundRolesForChannel(event.getGuild().getId(), event.getChannelLeft().getId()));
        ArrayList<String> toIDs = new ArrayList<>(VoiceChatRoleBindHandler.getBoundRolesForChannel(event.getGuild().getId(), event.getChannelJoined().getId()));

        // Get bound roles from the channel left and compare it to the bound roles from the channel joined.
        // Remove any roles from the 'from' array list that match.
        fromIDs.removeAll(toIDs);

        for (String roleID : fromIDs) {
            try {
                event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleID)).queue();
            } catch (NullPointerException | IllegalArgumentException e) {
                VoiceChatRoleBindHandler.unBindRoleFromVoiceChannel(event.getGuild().getId(), event.getChannelJoined().getId(), roleID);
            } catch (InsufficientPermissionException e) {
                GuildHandler.getCustomGuild(event.getGuild()).log(new BindCommand(), LogLevel.ERROR, "Insufficient Permissions", "The role I am trying to remove from a user is higher than the bot.");
            }
        }

        for (String roleID : toIDs) {
            try {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleID)).queue();
            } catch (NullPointerException | IllegalArgumentException e) {
                VoiceChatRoleBindHandler.unBindRoleFromVoiceChannel(event.getGuild().getId(), event.getChannelJoined().getId(), roleID);
            } catch (InsufficientPermissionException e) {
                GuildHandler.getCustomGuild(event.getGuild()).log(new BindCommand(), LogLevel.ERROR, "Insufficient Permissions", "The role I am trying to add to a user is higher than the bot.");
            }
        }
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        ArrayList<String> roleIDs = new ArrayList<>(VoiceChatRoleBindHandler.getBoundRolesForChannel(event.getGuild().getId(), event.getChannelJoined().getId()));

        for (String roleID : roleIDs) {
            try {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleID)).queue();
            } catch (NullPointerException | IllegalArgumentException e) {
                VoiceChatRoleBindHandler.unBindRoleFromVoiceChannel(event.getGuild().getId(), event.getChannelJoined().getId(), roleID);
            } catch (InsufficientPermissionException | HierarchyException e) {
                GuildHandler.getCustomGuild(event.getGuild()).log(new BindCommand(), LogLevel.ERROR, "Insufficient Permissions", "The role I am trying to add to a user is higher than the bot.");
            }
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        ArrayList<String> roleIDs = new ArrayList<>(VoiceChatRoleBindHandler.getBoundRolesForChannel(event.getGuild().getId(), event.getChannelLeft().getId()));

        for (String roleID : roleIDs) {
            try {
                event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleID)).queue();
            } catch (NullPointerException | IllegalArgumentException e) {
                VoiceChatRoleBindHandler.unBindRoleFromVoiceChannel(event.getGuild().getId(), event.getChannelLeft().getId(), roleID);
            } catch (InsufficientPermissionException | HierarchyException e) {
                GuildHandler.getCustomGuild(event.getGuild()).log(new BindCommand(), LogLevel.ERROR, "Insufficient Permissions", "The role I am trying to remove from a user is higher than the bot.");
            }
        }
    }

}
