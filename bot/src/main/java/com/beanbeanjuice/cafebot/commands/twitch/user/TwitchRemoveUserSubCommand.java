package com.beanbeanjuice.cafebot.commands.twitch.user;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class TwitchRemoveUserSubCommand extends Command implements ISubCommand {

    public TwitchRemoveUserSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String username = event.getOption("username").getAsString();  // Should not be null.

        cafeBot.getCafeAPI().getTwitchEndpoint().removeGuildTwitch(event.getGuild().getId(), username)
                .thenAcceptAsync((ignored) -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Twitch Channel Removed",
                            "They were successfully removed. You should no longer receive live notifications from them."
                    )).queue();
                })
                .exceptionallyAsync((e) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Removing Twitch Channel",
                            "There was an error removing them, you may still receive live notifications for their channel."
                    )).queue();
                    return null;
                });
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remove a twitch channel from this server.";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "username", "The username you want to remove.", true)
        };
    }

}
