package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.utility.api.dictionary.DictionaryHelper;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.helper.Helper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.CompletionException;

/**
 * An {@link ICommand} used to define words.
 *
 * @author beanbeanjuice
 */
public class DefineCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        String word = event.getOption("word").getAsString();
        String languageCode;

        if (event.getOption("language_code") == null) {
            languageCode = "en_US";
        } else {
            languageCode = event.getOption("language_code").getAsString();
        }

        try {
            event.getHook().sendMessageEmbeds(new DictionaryHelper(word, languageCode).dictionaryEmbed()).queue();
        } catch (CompletionException e) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Getting Dictionary Word",
                    "The word may not exist, or there may be some error getting it. Please try again and confirm that the word exists. " +
                            "If this error persists, please let me know!"
            )).queue();
        }
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Codes: `en_US`, `hi`, `es`, `fr`, `ja`, `ru`, `en_GB`, `de`, `it`, `ko`, `pt-BR`, `ar`, `tr`.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/define word` or `/define word es`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "word", "Any word.", true));
        options.add(new OptionData(OptionType.STRING, "language_code", "Language code to translate.", false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.GENERIC;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

}
