package com.beanbeanjuice.command.moderation.bind;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import com.beanbeanjuice.utility.handler.VoiceChatRoleBindHandler;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} used to bind a {@link net.dv8tion.jda.api.entities.Role Role} to
 * a {@link VoiceChannel VoiceChannel}.
 *
 * @author beanbeanjuice
 */
public class BindChannelSubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        String guildID = event.getGuild().getId();
        String option = event.getOption("option").getAsString();
        boolean set = option.equalsIgnoreCase("set");

        VoiceChannel voiceChannel = event.getOption("voice_channel").getAsChannel().asVoiceChannel();
        String voiceChannelID = voiceChannel.getId();

        Role role = event.getOption("role").getAsRole();
        String roleID = role.getId();

        // Checking if it has not been bound.
        if (set && !VoiceChatRoleBindHandler.getBoundRolesForChannel(guildID, voiceChannelID).contains(roleID)) {
            if (VoiceChatRoleBindHandler.bindRoleToVoiceChannel(guildID, voiceChannelID, roleID)) {
                event.getHook().sendMessageEmbeds(Helper.successEmbed(
                        "Role Bound to Voice Channel",
                        role.getAsMention() + " has been successfully bound to " +
                                voiceChannel.getAsMention() + "!"
                )).queue();

                // Adding the role to current VC members.
                try {
                    for (Member voiceMember : event.getGuild().getVoiceChannelById(voiceChannelID).getMembers())
                        event.getGuild().addRoleToMember(voiceMember, event.getGuild().getRoleById(roleID)).queue();
                } catch (InsufficientPermissionException | HierarchyException e) {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Adding Roles",
                            "There was an error adding roles to existing users: " + e.getMessage()
                    )).queue();
                }
                return;
            }
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Binding Role to Voice Channel",
                    "There was an error binding the role to the specified voice channel."
            )).queue();
            return;
        }

        // If it has been bound, remove it.
        if (!set && VoiceChatRoleBindHandler.unBindRoleFromVoiceChannel(guildID, voiceChannelID, roleID)) {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Role Unbound from Voice Channel",
                    role.getAsMention() + " has been successfully unbound from "
                            + voiceChannel.getAsMention() + "!"
            )).queue();

            // Adding the role to current VC members.
            try {
                for (Member voiceMember : event.getGuild().getVoiceChannelById(voiceChannelID).getMembers())
                    event.getGuild().removeRoleFromMember(voiceMember, event.getGuild().getRoleById(roleID)).queue();

            } catch (InsufficientPermissionException e) {

                event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                        "Error Removing Roles",
                        "There was an error removing roles from existing users: " + e.getMessage()
                )).queue();

            }
            return;
        }

        event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                "Error Unbinding Role from Voice Channel",
                "There was an error unbinding the role from the specified voice channel."
        )).queue();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Bind a voice channel to a role!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/bind generalvoice @bruh`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "option", "Set or unset the role.", true)
                .addChoice("Set", "set")
                .addChoice("Unset", "unset"));
        options.add(new OptionData(OptionType.CHANNEL, "voice_channel", "The voice channel to bind to.", true)
                .setChannelTypes(ChannelType.VOICE));
        options.add(new OptionData(OptionType.ROLE, "role", "The role to bind to the voice channel.", true));
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
        return "channel";
    }

}
