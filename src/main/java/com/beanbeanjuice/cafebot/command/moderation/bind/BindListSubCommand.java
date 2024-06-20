package com.beanbeanjuice.cafebot.command.moderation.bind;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ISubCommand;
import com.beanbeanjuice.cafebot.utility.handler.VoiceChatRoleBindHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} used to list binds between {@link net.dv8tion.jda.api.entities.Role Roles}
 * and {@link VoiceChannel VoiceChannels}.
 *
 * @author beanbeanjuice
 */
public class BindListSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        // Getting the voice-binds for all voice channels.
        if (event.getOption("voice_channel") == null) {
            ArrayList<VoiceChannel> voiceChannels = new ArrayList<>();
            VoiceChatRoleBindHandler.getAllBoundChannelsForGuild(event.getGuild().getId()).forEach((voiceChannelID, roles) -> {
                try {
                    VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(voiceChannelID);

                    if (voiceChannel == null) {
                        // Remove every role in that voice channel if the voice channel does not exist.
                        for (String roleID : roles)
                            VoiceChatRoleBindHandler.unBindRoleFromVoiceChannel(event.getGuild().getId(), voiceChannelID, roleID);
                    }

                    voiceChannels.add(voiceChannel);
                } catch (NullPointerException | IllegalArgumentException e) {

                    // Remove every role in that voice channel if the voice channel does not exist.
                    for (String roleID : roles)
                        VoiceChatRoleBindHandler.unBindRoleFromVoiceChannel(event.getGuild().getId(), voiceChannelID, roleID);
                }
            });

            StringBuilder descriptionBuilder = new StringBuilder();
            descriptionBuilder.append("**Current List of Voice Channels with Bound Roles**\n\n");

            for (int i = 0; i < voiceChannels.size(); i++) {
                try {

                    descriptionBuilder.append(voiceChannels.get(i).getAsMention());
                    if (i != voiceChannels.size() - 1)
                        descriptionBuilder.append(", ");

                } catch (NullPointerException e) {

                    ArrayList<String> roleIDs = new ArrayList<>(VoiceChatRoleBindHandler.getBoundRolesForChannel(
                            event.getGuild().getId(), voiceChannels.get(i).toString()
                    ));

                    for (String roleID : roleIDs) {
                        VoiceChatRoleBindHandler.unBindRoleFromVoiceChannel(
                                event.getGuild().getId(), voiceChannels.get(i).toString(), roleID
                        );
                    }
                }
            }

            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Voice Channel Binds",
                    descriptionBuilder.toString()
            )).queue();
            return;
        }

        // All Binds
        ArrayList<Role> roles = new ArrayList<>();
        VoiceChannel voiceChannel = event.getOption("voice_channel").getAsChannel().asVoiceChannel();

        for (String roleID : new ArrayList<>(VoiceChatRoleBindHandler.getBoundRolesForChannel(event.getGuild().getId(), voiceChannel.getId()))) {
            Role role = Helper.getRole(event.getGuild(), roleID);
            if (role == null)
                VoiceChatRoleBindHandler.unBindRoleFromVoiceChannel(event.getGuild().getId(), voiceChannel.getId(), roleID);
            roles.add(role);
        }

        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append("**Current List of Roles for the Current Voice Channel**\n\n");

        for (int i = 0; i < roles.size(); i++) {
            descriptionBuilder.append(roles.get(i).getAsMention());

            if (i != roles.size() - 1)
                descriptionBuilder.append(", ");
        }

        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Voice Channel Binds",
                descriptionBuilder.toString()
        )).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Get the voice channel bind list!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/bind list`";
    }

    @NotNull
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.CHANNEL, "voice_channel", "The voice channel to get the binds for.", false)
                .setChannelTypes(ChannelType.VOICE));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.MODERATION;
    }

    @NotNull
    @Override
    public String getName() {
        return "list";
    }

}
