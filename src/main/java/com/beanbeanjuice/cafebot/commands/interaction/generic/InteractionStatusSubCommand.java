package com.beanbeanjuice.cafebot.commands.interaction.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.i18n.I18N;
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
        final I18N bundle = ctx.getUserI18n();
        boolean status = event.getOption("status").getAsBoolean(); // Should not be null.
        bot.getCafeAPI().getInteractionsApi().setInteractionStatus(event.getUser().getId(), status).thenAccept(response -> {
            String description = status
                    ? bundle.getString("command.interaction.status.success.enabled")
                    : bundle.getString("command.interaction.status.success.disabled");
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            bundle.getString("command.interaction.status.success.title"),
                            description
                    )
            ).queue();
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    bundle.getString("command.interaction.status.error.title"),
                    bundle.getString("command.interaction.status.error.description")
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
        return "command.interaction.status.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.BOOLEAN, "status", "command.interaction.status.arguments.status.description", true)
        };
    }

}
