package com.beanbeanjuice.cafebot.commands.settings.bind;

import com.beanbeanjuice.cafebot.CafeBot;
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

public class BindRemoveSubCommand extends Command implements ISubCommand {

    public BindRemoveSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        VoiceChannel channel = event.getOption("channel").getAsChannel().asVoiceChannel();
        Role role = event.getOption("role").getAsRole();
        String guildId = event.getGuild().getId();

        bot.getCafeAPI().getVoiceRoleApi().deleteVoiceRole(guildId, channel.getId(), role.getId()).thenAccept((voiceRole) -> {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Role Removed!",
                    String.format("Hi there!~ I unbound %s from %s!", role.getAsMention(), channel.getAsMention())
            )).queue();
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Removing Bound Role",
                    "I'm... not sure why, but my boss said you can't do that..."
            )).queue();
            throw new CompletionException(ex);
        });
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescriptionPath() {
        return "Remove a bound role from a voice channel";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.CHANNEL, "channel", "The voice channel you want to use.", true)
                        .setChannelTypes(ChannelType.VOICE),
                new OptionData(OptionType.ROLE, "role", "The role you want to set when joining the channel.", true)
        };
    }

}
