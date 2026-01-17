package com.beanbeanjuice.cafebot.commands.interaction.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class InteractionUnblockSubCommand extends Command implements ISubCommand {

    public InteractionUnblockSubCommand(CafeBot bot) {
        super(bot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String unblockedUser = event.getOption("user-id").getAsString();

        bot.getCafeAPI().getInteractionsApi().unBlockUser(event.getUser().getId(), unblockedUser).thenRun(() -> {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Unblocked User",
                    "That user can now interact with you again!"
            )).queue();
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Unblocking User",
                    "I... can't unblock them... please tell me boss..."
            )).queue();

            bot.getLogger().log(this.getClass(), LogLevel.WARN, String.format("Error Unblocking User: %s", ex.getMessage()), true, true);
            throw new CompletionException(ex);
        });
    }

    @Override
    public String getName() {
        return "unblock";
    }

    @Override
    public String getDescription() {
        return "Unblocks a user you have previously blocked.";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "user-id", "The id of the user you want to unblock.", true, true)
        };
    }

    @Override
    public CompletableFuture<HashMap<String, ArrayList<String>>> getAutoComplete(CommandAutoCompleteInteractionEvent event) {
        return bot.getCafeAPI().getInteractionsApi().getBlockedUsers(event.getUser().getId()).thenApply((blockList) -> {
            HashMap<String, ArrayList<String>> map = new HashMap<>();
            map.put("user-id", new ArrayList<>());

            for (String userId : blockList) map.get("user-id").add(userId);

            return map;
        });
    }

}
