package com.beanbeanjuice.command.moderation.voicebind;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

/**
 * An {@link ICommand} used to get the list of {@link net.dv8tion.jda.api.entities.Role Roles} bound to a {@link net.dv8tion.jda.api.entities.VoiceChannel VoiceChannel}.
 *
 * @author beanbeanjuice
 */
public class GetVoiceRoleBindsCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            ArrayList<VoiceChannel> voiceChannels = new ArrayList<>();
            CafeBot.getVoiceChatRoleBindHandler().getBoundChannels(event.getGuild().getId()).forEach((voiceChannelID, roles) -> {
                try {
                    VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(voiceChannelID);

                    if (voiceChannel == null) {
                        // Remove every role in that voice channel if the voice channel does not exist.
                        for (String roleID : roles) {
                            CafeBot.getVoiceChatRoleBindHandler().unBind(event.getGuild().getId(), voiceChannelID, roleID);
                        }
                    }

                    voiceChannels.add(voiceChannel);
                } catch (NullPointerException | IllegalArgumentException e) {

                    // Remove every role in that voice channel if the voice channel does not exist.
                    for (String roleID : roles) {
                        CafeBot.getVoiceChatRoleBindHandler().unBind(event.getGuild().getId(), voiceChannelID, roleID);
                    }
                }
            });

            StringBuilder descriptionBuilder = new StringBuilder();
            descriptionBuilder.append("**Current List of Voice Channels with Bound Roles**\n\n");

            for (int i = 0; i < voiceChannels.size(); i++) {
                descriptionBuilder.append(voiceChannels.get(i).getAsMention());

                if (i != voiceChannels.size() - 1) {
                    descriptionBuilder.append(", ");
                }
            }

            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Voice Channel Binds",
                    descriptionBuilder.toString()
            )).queue();
            return;
        }

        if (event.getMember().getVoiceState().inVoiceChannel()) {
            ArrayList<Role> roles = new ArrayList<>();

            VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();

            for (String roleID : new ArrayList<>(CafeBot.getVoiceChatRoleBindHandler().getBoundRoles(event.getGuild().getId(), voiceChannel.getId()))) {
                Role role = CafeBot.getGeneralHelper().getRole(event.getGuild(), roleID);

                if (role == null) {
                    CafeBot.getVoiceChatRoleBindHandler().unBind(event.getGuild().getId(), voiceChannel.getId(), roleID);
                }
                roles.add(role);
            }

            StringBuilder descriptionBuilder = new StringBuilder();
            descriptionBuilder.append("**Current List of Roles for the Current Voice Channel**\n\n");

            for (int i = 0; i < roles.size(); i++) {
                descriptionBuilder.append(roles.get(i).getAsMention());

                if (i != roles.size() - 1) {
                    descriptionBuilder.append(", ");
                }
            }

            event.getChannel().sendMessage(CafeBot.getGeneralHelper().successEmbed(
                    "Voice Channel Binds",
                    descriptionBuilder.toString()
            )).queue();
            return;
        }
    }

    @Override
    public String getName() {
        return "get-voice-role-binds";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("getvoicerolebinds");
        arrayList.add("get-binds");
        arrayList.add("getbinds");
        arrayList.add("get-role-binds");
        arrayList.add("getrolebinds");
        arrayList.add("get-bind-roles");
        arrayList.add("getbindroles");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Get a list of voice channels that have roles bound to them! If you want to get a list of roles bound to a " +
                "specific channel, just join the VC you want to check and run the command!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "get-binds`";
    }

    @Override
    public Usage getUsage() {
        return new Usage();
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.MODERATION;
    }

}
