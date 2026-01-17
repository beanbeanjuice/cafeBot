package com.beanbeanjuice.cafebot.utility.listeners;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VoiceRoleBindListener extends ListenerAdapter {

    private final CafeBot bot;

    public VoiceRoleBindListener(CafeBot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if (event.getChannelJoined() != null && event.getChannelJoined().getType() == ChannelType.VOICE) {
            handleVoiceJoin(event);
            return;
        }

        if (event.getChannelLeft() != null && event.getChannelLeft().getType() == ChannelType.VOICE) {
            handleVoiceLeave(event);
            return;
        }

    }

    private void handleVoiceJoin(GuildVoiceUpdateEvent event) {
        Guild guild = event.getGuild();
        VoiceChannel channel = event.getChannelJoined().asVoiceChannel();

        bot.getCafeAPI().getVoiceRoleApi().getVoiceRoles(guild.getId()).thenAccept((voiceRoles) -> {
            voiceRoles.forEach((voiceRole) -> {
                if (!channel.getId().equals(voiceRole.getChannelId())) return;

                Role role = guild.getRoleById(voiceRole.getRoleId());
                if (role == null) return;

                guild.addRoleToMember(event.getEntity().getUser(), role).queue((success) -> {return;}, (error) -> {
                    bot.getLogger().logToGuild(guild, Helper.errorEmbed(
                            "Voice Role Bind Error",
                            "There was an error setting a voice role bind... " + error.getMessage()
                    ));
                });
            });
        });
    }

    private void handleVoiceLeave(GuildVoiceUpdateEvent event) {
        Guild guild = event.getGuild();
        VoiceChannel channel = event.getChannelLeft().asVoiceChannel();

        bot.getCafeAPI().getVoiceRoleApi().getVoiceRoles(guild.getId()).thenAccept((voiceRoles) -> {
            voiceRoles.forEach((voiceRole) -> {
                if (!channel.getId().equals(voiceRole.getChannelId())) return;

                Role role = guild.getRoleById(voiceRole.getRoleId());
                if (role == null) return;

                guild.removeRoleFromMember(event.getEntity().getUser(), role).queue((success) -> {return;}, (error) -> {
                    bot.getLogger().logToGuild(guild, Helper.errorEmbed(
                            "Voice Role Bind Error",
                            "There was an error removing a voice role bind... " + error.getMessage()
                    ));
                });
            });
        });
    }

}
