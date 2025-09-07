package com.beanbeanjuice.cafebot.commands.settings.bind;

import com.beanbeanjuice.cafebot.api.wrapper.type.VoiceRole;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public class BindListSubCommand extends Command implements ISubCommand {

    public BindListSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        bot.getCafeAPI().getVoiceRoleApi().getVoiceRoles(event.getGuild().getId()).thenAccept((voiceRoles) -> {
            handleVoiceRolesEmbed(event, voiceRoles);
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Getting Roles",
                    "I'm not sure why, but there was a problem... if it's like this in an hour please report it using `/bug`... <:cafeBot_sad:1171726165040447518>"
            )).queue();

            bot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Getting Voice Roles: %s", ex.getMessage()), true, false);
            throw new CompletionException(ex);
        });
    }

    private void handleVoiceRolesEmbed(SlashCommandInteractionEvent event, List<VoiceRole> voiceRoles) {
        event.getHook().sendMessage("Here's your voice role binds! <:cafeBot_thumbs_up:1457847525280321577>")
                .addEmbeds(getVoiceRolesEmbed(event.getGuild(), voiceRoles)).queue();
    }

    private MessageEmbed getVoiceRolesEmbed(Guild guild, List<VoiceRole> voiceRoleList) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Voice Channel <-> Role Binds");

        String description = voiceRoleList.stream().map((voiceRole) -> {
            VoiceChannel channel = guild.getVoiceChannelById(voiceRole.getChannelId());
            Role role = guild.getRoleById(voiceRole.getRoleId());

            if (channel == null || role == null) return null;

            return String.format("%s <-> %s", channel.getAsMention(), role.getAsMention());
        }).collect(Collectors.joining("\n"));

        embedBuilder.setDescription(description);
        embedBuilder.setColor(Helper.getRandomColor());

        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "List all of the voice channel-role binds!";
    }

}
