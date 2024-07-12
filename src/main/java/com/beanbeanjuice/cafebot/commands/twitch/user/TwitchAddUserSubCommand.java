package com.beanbeanjuice.cafebot.commands.twitch.user;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class TwitchAddUserSubCommand extends Command implements ISubCommand {

    public TwitchAddUserSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String username = event.getOption("username").getAsString();  // Should not be null.

        cafeBot.getCafeAPI().getTwitchEndpoint().addGuildTwitch(event.getGuild().getId(), username)
                .thenAcceptAsync((ignored) -> {
                    cafeBot.getTwitchHandler().addStream(username);
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Added Channel",
                            String.format("Successfully added **%s**.", username)
                    )).queue();
                })
                .exceptionallyAsync((e) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Adding Channel",
                            String.format("There was an error adding **%s**: %s", username, e.getMessage())
                    )).queue();
                    return null;
                });
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "Add a twitch channel to listen for live notifications!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "username", "Their twitch username.", true)
        };
    }

}
