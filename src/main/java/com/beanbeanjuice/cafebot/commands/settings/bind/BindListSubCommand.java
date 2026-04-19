package com.beanbeanjuice.cafebot.commands.settings.bind;

import com.beanbeanjuice.cafebot.api.wrapper.type.VoiceRole;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
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
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        I18N bundle = ctx.getUserI18n();
        bot.getCafeAPI().getVoiceRoleApi().getVoiceRoles(event.getGuild().getId()).thenAccept((voiceRoles) -> {
            handleVoiceRolesEmbed(event, voiceRoles, bundle);
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    bundle.getString("command.bind.subcommand.list.embed.error.title"),
                    bundle.getString("command.bind.subcommand.list.embed.error.description")
            )).queue();

            bot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Getting Voice Roles: %s", ex.getMessage()), true, false);
            throw new CompletionException(ex);
        });
    }

    private void handleVoiceRolesEmbed(SlashCommandInteractionEvent event, List<VoiceRole> voiceRoles, I18N bundle) {
        event.getHook().sendMessage(bundle.getString("command.bind.subcommand.list.embed.header"))
                .addEmbeds(getVoiceRolesEmbed(event.getGuild(), voiceRoles, bundle)).queue();
    }

    private MessageEmbed getVoiceRolesEmbed(Guild guild, List<VoiceRole> voiceRoleList, I18N bundle) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(bundle.getString("command.bind.subcommand.list.embed.title"));

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
    public String getDescriptionPath() {
        return "command.bind.subcommand.list.description";
    }

}
