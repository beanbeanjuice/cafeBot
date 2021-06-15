package com.beanbeanjuice.utility.sections.moderation.voicechat;

import com.beanbeanjuice.CafeBot;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VoiceChatListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        ArrayList<String> roleIDs = CafeBot.getVoiceChatRoleBindHandler().getBindedRoles(event.getGuild().getId(), event.getChannelJoined().getId());

        for (String roleID : roleIDs) {
            try {
                event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleID));
            } catch (NullPointerException e) {
                CafeBot.getVoiceChatRoleBindHandler().unBind(event.getGuild().getId(), event.getChannelJoined().getId(), roleID);
            }
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        ArrayList<String> roleIDs = CafeBot.getVoiceChatRoleBindHandler().getBindedRoles(event.getGuild().getId(), event.getChannelLeft().getId());

    }
}
