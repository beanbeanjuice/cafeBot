package com.beanbeanjuice.cafebot.commands.settings.bind;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.concurrent.CompletionException;

public class BindSetSubCommand extends Command implements ISubCommand {

    public BindSetSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        I18N bundle = ctx.getUserI18n();
        VoiceChannel channel = event.getOption("channel").getAsChannel().asVoiceChannel();
        Role role = event.getOption("role").getAsRole();
        String guildId = event.getGuild().getId();

        bot.getCafeAPI().getVoiceRoleApi().createVoiceRole(guildId, channel.getId(), role.getId()).thenAccept((voiceRole) -> {
            String description = bundle.getString("command.bind.subcommand.set.embed.success.description")
                    .replace("{role}", role.getAsMention())
                    .replace("{channel}", channel.getAsMention());
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    bundle.getString("command.bind.subcommand.set.embed.success.title"),
                    description
            )).queue();
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    bundle.getString("command.bind.subcommand.set.embed.error.title"),
                    bundle.getString("command.bind.subcommand.set.embed.error.description")
            )).queue();
            throw new CompletionException(ex);
        });
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescriptionPath() {
        return "command.bind.subcommand.set.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.CHANNEL, "channel", "command.bind.subcommand.set.arguments.channel.description", true)
                        .setChannelTypes(ChannelType.VOICE),
                new OptionData(OptionType.ROLE, "role", "command.bind.subcommand.set.arguments.role.description", true)
        };
    }

}
