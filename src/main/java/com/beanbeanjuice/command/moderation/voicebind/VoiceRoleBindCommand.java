package com.beanbeanjuice.command.moderation.voicebind;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to bind a {@link net.dv8tion.jda.api.entities.Role Role} to a specified {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel}.
 *
 * @author beanbeanjuice
 */
public class VoiceRoleBindCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        // Checking if the user running this command is an administrator.
        if (!CafeBot.getGeneralHelper().isAdministrator(event.getMember(), event)) {
            return;
        }

        // Checking if the member is in a voice channel.
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Not In Voice Channel",
                    "You must be in a voice channel to use this command."
            )).queue();
            return;
        }

        String guildID = event.getGuild().getId();
        String voiceChannelID = event.getMember().getVoiceState().getChannel().getId();
        String roleID = CafeBot.getGeneralHelper().getRole(event.getGuild(), args.get(0)).getId();

        // Checking if it has not been bound.
        if (!CafeBot.getVoiceChatRoleBindHandler().getBoundRoles(guildID, voiceChannelID).contains(roleID)) {
            if (CafeBot.getVoiceChatRoleBindHandler().bind(guildID, voiceChannelID, roleID)) {
                event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                        "Role Bound to Voice Channel",
                        event.getGuild().getRoleById(roleID).getAsMention() + " has been successfully bound to " +
                                event.getGuild().getVoiceChannelById(voiceChannelID).getAsMention() + "!"
                )).queue();

                // Adding the role to current VC members.
                try {
                    for (Member voiceMember : event.getGuild().getVoiceChannelById(voiceChannelID).getMembers()) {
                        event.getGuild().addRoleToMember(voiceMember, event.getGuild().getRoleById(roleID)).queue();
                    }
                } catch (InsufficientPermissionException e) {
                    event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                            "Error Adding Roles",
                            "There was an error adding roles to existing users: " + e.getMessage()
                    )).queue();
                }
                return;
            }
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Binding Role to Voice Channel",
                    "There was an error binding the role to the specified voice channel."
            )).queue();
            return;
        }

        // If it has been bound, remove it.
        if (CafeBot.getVoiceChatRoleBindHandler().unBind(guildID, voiceChannelID, roleID)) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Role Unbound from Voice Channel",
                    event.getGuild().getRoleById(roleID).getAsMention() + " has been successfully unbound from "
                            + event.getGuild().getVoiceChannelById(voiceChannelID).getAsMention() + "!"
            )).queue();

            // Adding the role to current VC members.
            try {
                for (Member voiceMember : event.getGuild().getVoiceChannelById(voiceChannelID).getMembers()) {
                    event.getGuild().removeRoleFromMember(voiceMember, event.getGuild().getRoleById(roleID)).queue();
                }
            } catch (InsufficientPermissionException e) {
                event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                        "Error Removing Roles",
                        "There was an error removing roles from existing users: " + e.getMessage()
                )).queue();
            }
            return;
        }

        event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                "Error Unbinding Role from Voice Channel",
                "There was an error unbinding the role from the specified voice channel."
        )).queue();
        return;

    }

    @Override
    public String getName() {
        return "voice-role-bind";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("voicerolebind");
        arrayList.add("bind-role");
        arrayList.add("bindrole");
        arrayList.add("role-bind");
        arrayList.add("rolebind");
        arrayList.add("vc-role-bind");
        arrayList.add("vc-bind");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Bind a role to give someone when they join a VC and remove it when they leave!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "bind-voice @GamingVoice`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.ROLE, "Role Mention/ID", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }

}
