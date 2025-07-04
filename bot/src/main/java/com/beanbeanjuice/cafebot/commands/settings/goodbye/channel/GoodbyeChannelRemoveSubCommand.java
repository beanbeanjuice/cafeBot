package com.beanbeanjuice.cafebot.commands.settings.goodbye.channel;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class GoodbyeChannelRemoveSubCommand extends Command implements ISubCommand {

    public GoodbyeChannelRemoveSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        cafeBot.getCafeAPI().getGuildsEndpoint().updateGuildInformation(event.getGuild().getId(), GuildInformationType.GOODBYE_CHANNEL_ID, "0")
                .thenAcceptAsync((ignored) -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Goodbye Channel Removed",
                            "The goodbye channel has been removed!"
                    )).queue();
                })
                .exceptionallyAsync((e) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Removing Goodbye Channel",
                            String.format("There was an error removing the goodbye channel: %s", e.getMessage())
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
        return "Remove the goodbye channel!";
    }

}
