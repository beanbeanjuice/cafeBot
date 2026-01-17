package com.beanbeanjuice.cafebot.commands.generic;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.api.dictionary.DictionaryAPIWrapper;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class DefineCommand extends Command implements ICommand {

    public DefineCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String word = event.getOption("word").getAsString();

        Optional<OptionMapping> languageCodeMapping = Optional.ofNullable(event.getOption("languageCode"));
        String languageCode = languageCodeMapping.map(OptionMapping::getAsString).orElse("en_US");

        DictionaryAPIWrapper dictionaryAPIWrapper = new DictionaryAPIWrapper(bot);
        dictionaryAPIWrapper.getDictionaryEmbed(word, languageCode)
                .thenAcceptAsync((embed) -> event.getHook().sendMessageEmbeds(embed).queue())
                .exceptionallyAsync((ignored) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Defining Word",
                            "<:cafeBot_sad:1171726165040447518> I-... I don't know that word..."
                    )).queue();
                    return null;
                });
    }

    @Override
    public String getName() {
        return "define";
    }

    @Override
    public String getDescription() {
        return "Define something!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERIC;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "word", "The word to define.", true),
                new OptionData(OptionType.STRING, "language_code", "The language code to define.", false)
                        .addChoice("English", "en_US")
                        .addChoice("English (Britain)", "en_GB")
                        .addChoice("Spanish", "es")
                        .addChoice("French", "fr")
                        .addChoice("Russian", "ru")
                        .addChoice("German", "de")
                        .addChoice("Turkish", "tr")
                        .addChoice("Arabic", "ar")
                        .addChoice("Korean", "ko")
                        .addChoice("Italian", "it")
                        .addChoice("Japanese", "ja")
                        .addChoice("Hindi", "hi")
                        .addChoice("Portuguese", "pt-BR")
        };
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
    }

    @Override
    public boolean isEphemeral() {
        return true;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public boolean allowDM() {
        return true;
    }

}
