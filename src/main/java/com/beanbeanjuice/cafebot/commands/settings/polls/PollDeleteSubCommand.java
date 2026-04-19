package com.beanbeanjuice.cafebot.commands.settings.polls;

import com.beanbeanjuice.cafebot.api.wrapper.type.poll.Poll;
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

public class PollDeleteSubCommand extends Command implements ISubCommand {

    public PollDeleteSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        int pollId = Integer.valueOf(event.getOption("id").getAsString());

        bot.getCafeAPI().getPollApi().deletePoll(pollId).thenRun(() -> {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    ctx.getUserI18n().getString("command.poll.subcommand.delete.embed.success.title"),
                    ctx.getUserI18n().getString("command.poll.subcommand.delete.embed.success.description")
            )).queue();
        });
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescriptionPath() {
        return "command.poll.subcommand.delete.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "id", "command.poll.subcommand.delete.arguments.id.description", true, true)
        };
    }

    @Override
    public CompletableFuture<HashMap<String, List<String>>> getAutoComplete(CommandAutoCompleteInteractionEvent event) {
        return bot.getCafeAPI().getPollApi().getPolls(event.getGuild().getId(), true, false).thenApply((polls) -> {
            HashMap<String, List<String>> autoCompleteMap = new HashMap<>();

            List<String> ids = polls.stream().map(Poll::getId).map(Object::toString).toList();

            autoCompleteMap.put("id", new ArrayList<>(ids));

            return autoCompleteMap;
        });
    }

}
