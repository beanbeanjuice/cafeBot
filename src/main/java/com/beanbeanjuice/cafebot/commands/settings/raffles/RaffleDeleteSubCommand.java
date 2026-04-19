package com.beanbeanjuice.cafebot.commands.settings.raffles;

import com.beanbeanjuice.cafebot.api.wrapper.type.Raffle;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RaffleDeleteSubCommand extends Command implements ISubCommand {

    public RaffleDeleteSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        int raffleId = Integer.valueOf(event.getOption("id").getAsString());

        bot.getCafeAPI().getRaffleApi().deleteRaffle(raffleId).thenRun(() -> {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    ctx.getUserI18n().getString("command.raffle.subcommand.delete.embed.success.title"),
                    ctx.getUserI18n().getString("command.raffle.subcommand.delete.embed.success.description")
            )).queue();
        });
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescriptionPath() {
        return "command.raffle.subcommand.delete.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "id", "command.raffle.subcommand.delete.arguments.id.description", true, true)
        };
    }

    @Override
    public CompletableFuture<HashMap<String, List<String>>> getAutoComplete(CommandAutoCompleteInteractionEvent event) {
        return bot.getCafeAPI().getRaffleApi().getRaffles(event.getGuild().getId(), true, false).thenApply((raffles) -> {
            HashMap<String, List<String>> autoCompleteMap = new HashMap<>();

            List<String> ids = raffles.stream().map(Raffle::getId).map(Object::toString).toList();

            autoCompleteMap.put("id", new ArrayList<>(ids));

            return autoCompleteMap;
        });
    }

}
