package com.beanbeanjuice.cafebot.commands.twitch.user;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public class TwitchListUsersSubCommand extends Command implements ISubCommand {

    public TwitchListUsersSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        cafeBot.getCafeAPI().getTwitchEndpoint().getGuildTwitches(event.getGuild().getId())
                .thenAcceptAsync((channels) -> {
                    event.getHook().sendMessageEmbeds(twitchChannelsEmbed(channels)).queue();
                })
                .exceptionallyAsync((e) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Getting Twitch Channels",
                            String.format("There was an error getting twitch channels for this server: %s", e.getMessage())
                    )).queue();
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
    public String getDescription() {
        return "List the current twitch channels you have on the server.";
    }

}
