package com.beanbeanjuice.utility.command;

import com.beanbeanjuice.Bot;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A {@link ListenerAdapter} to listen for {@link CommandAutoCompleteInteractionEvent}.
 *
 * @author beanbeanjuice
 */
public class CommandAutoCompleteHandler extends ListenerAdapter {

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        String option = event.getFocusedOption().getName();
        String name = event.getName();

        if (Bot.getCommandHandler().getCommands().containsKey(name)) {
            ICommand command = Bot.getCommandHandler().getCommands().get(name);

            if (command.getAutoComplete() != null && command.getAutoComplete().get(option) != null) {
                ArrayList<String> autoCompleteList = command.getAutoComplete().get(option);
                String[] list = new String[autoCompleteList.size()];
                list = autoCompleteList.toArray(list);

                List<Command.Choice> options = Stream.of(list)
                        .filter(word -> word.startsWith(event.getFocusedOption().getValue())) // only display words that start with the user's current input
                        .map(word -> new Command.Choice(word, word)) // map the words to choices
                        .collect(Collectors.toList());
                try {
                    event.replyChoices(options).queue();
                } catch (IllegalArgumentException ignored) {  // If more than 25 options are present.
                    ArrayList<Command.Choice> options25 = new ArrayList<>();

                    for (int i = 0; i < 25; i++)
                        options25.add(options.get(i));

                    event.replyChoices(options25).queue();
                }
            }
        }
    }

}
