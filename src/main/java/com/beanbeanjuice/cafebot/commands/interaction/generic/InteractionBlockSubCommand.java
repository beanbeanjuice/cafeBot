package com.beanbeanjuice.cafebot.commands.interaction.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.concurrent.CompletionException;

public class InteractionBlockSubCommand extends Command implements ISubCommand {

    public InteractionBlockSubCommand(CafeBot bot) {
        super(bot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        User userToBlock = event.getOption("user").getAsUser(); // Should not be null.

        bot.getCafeAPI().getInteractionsApi().blockUser(event.getUser().getId(), userToBlock.getId()).thenAccept((blockList) -> {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Successful Block",
                    String.format("You'll no longer receive interactions from %s. You have %d blocked users. To unblock, type `/interactions unblock`!", userToBlock.getAsMention(), blockList.size())
            )).queue();
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Blocking User",
                    "I'm sorry... I wasn't able to remove them from the establishment... try again later?"
            )).queue();

            bot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Blocking User: %s", ex.getMessage()), true, true);
            throw new CompletionException(ex);
        });
    }

    @Override
    public String getName() {
        return "block";
    }

    @Override
    public String getDescriptionPath() {
        return "Someone being annoying? Block them! You won't receive any interactions from them anymore.";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to block", true)
        };
    }

}
