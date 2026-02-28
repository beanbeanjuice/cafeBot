package com.beanbeanjuice.cafebot.commands.interaction.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.concurrent.CompletionException;

public class InteractionStatusSubCommand extends Command implements ISubCommand {

    public InteractionStatusSubCommand(CafeBot bot) {
        super(bot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        boolean status = event.getOption("status").getAsBoolean(); // Should not be null.
        bot.getCafeAPI().getInteractionsApi().setInteractionStatus(event.getUser().getId(), status).thenAccept(response -> {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Interaction Status Changed",
                            String.format("You've changed your interaction status to %s. Users can %s interact with you.", status, status ? "now" : "no longer")
                    )
            ).queue();
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Changing Interaction Status",
                    "I... don't know why I can't change it right now... try asking me later?"
            )).queue();
            throw new CompletionException(ex);
        });
    }

    @Override
    public String getName() {
        return "status";
    }

    @Override
    public String getDescriptionPath() {
        return "Enable or disable the ability to send interactions to you!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.BOOLEAN, "status", "Set to \"true\" to enable interactions.", true)
        };
    }

}
