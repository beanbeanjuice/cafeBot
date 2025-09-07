package com.beanbeanjuice.cafebot.commands.settings.bind;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
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
    public void handle(SlashCommandInteractionEvent event) {
        VoiceChannel channel = event.getOption("channel").getAsChannel().asVoiceChannel();
        Role role = event.getOption("role").getAsRole();
        String guildId = event.getGuild().getId();

        bot.getCafeAPI().getVoiceRoleApi().createVoiceRole(guildId, channel.getId(), role.getId()).thenAccept((voiceRole) -> {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Role Bound!",
                    String.format("<:cafeBot_thumbs_up:1457847525280321577> Hi there!~ I bound %s to %s! Whenever someone joins that channel, they'll be given that role. The role will also be removed when they leave!", role.getAsMention(), channel.getAsMention())
            )).queue();
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Binding Role",
                    "I'm... not sure why, but my boss said you can't do that..."
            )).queue();
            throw new CompletionException(ex);
        });
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Bind a role to a voice channel!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.CHANNEL, "channel", "Voice channel", true)
                        .setChannelTypes(ChannelType.VOICE),
                new OptionData(OptionType.ROLE, "role", "Role", true)
        };
    }

}
