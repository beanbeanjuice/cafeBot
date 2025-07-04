package com.beanbeanjuice.cafebot.commands.settings.update;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.guilds.GuildInformationType;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class UpdateStatusSubCommand extends Command implements ISubCommand {

    public UpdateStatusSubCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        boolean status = event.getOption("enabled").getAsBoolean();

        cafeBot.getCafeAPI().getGuildsEndpoint().updateGuildInformation(event.getGuild().getId(), GuildInformationType.NOTIFY_ON_UPDATE, status)
                .thenAcceptAsync((ignored) -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Bot Update Notification Status Changed",
                            "The bot update notification status has been successfully changed."
                    )).queue();
                })
                .exceptionallyAsync((e) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Changing Bot Notification Status",
                            String.format("There was an error changing the bot notification status: %s", e.getMessage())
                    )).queue();
                    return null;
                });
    }

    @Override
    public String getName() {
        return "status";
    }

    @Override
    public String getDescription() {
        return "Enable or disable bot update notifications.";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.BOOLEAN, "enabled", "Enable or disable bot update notifications.", true)
        };
    }

}
