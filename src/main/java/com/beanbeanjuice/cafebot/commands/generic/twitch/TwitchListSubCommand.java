package com.beanbeanjuice.cafebot.commands.generic.twitch;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public class TwitchListSubCommand extends Command implements ISubCommand {

    public TwitchListSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        bot.getCafeAPI().getTwitchChannelApi().getChannels(event.getGuild().getId())
                .thenAccept((channels) -> event.getHook().sendMessageEmbeds(twitchChannelsEmbed(channels)).queue())
                .exceptionally((ex) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Getting Twitch Channels",
                            "There was an error getting twitch channels for this server."
                    )).queue();

                    bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Getting Twitch Channels: " + ex.getMessage());
                    return null;
                });
    }

    private MessageEmbed twitchChannelsEmbed(List<String> channels) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Twitch Channels");

        String channelsString = String.join("\n", channels);

        String description = String.format(
                """
                These are the following channels that have been added for this server:
                
                %s
                """, channelsString
        );

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
        return "Get a list of all twitch channels for this server!";
    }

}
