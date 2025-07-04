package com.beanbeanjuice.cafebot.commands.twitch.role;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class TwitchRoleRemoveSubCommand extends Command implements ISubCommand {

    public TwitchRoleRemoveSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        cafeBot.getCafeAPI().getGuildsEndpoint().updateGuildInformation(event.getGuild().getId(), GuildInformationType.LIVE_NOTIFICATIONS_ROLE_ID, "0")
                .thenAcceptAsync((ignored) -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Successfully Removed Live Notifications Role",
                            "The live notifications role has been successfully removed."
                    )).queue();
                })
                .exceptionallyAsync((e) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Removing Live Notifications Role",
                            String.format("There was an error removing the live notifications role: %s", e.getMessage())
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
        return "Remove the live notifications role.";
    }

}
