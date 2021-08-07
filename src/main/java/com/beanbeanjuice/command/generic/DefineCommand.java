package com.beanbeanjuice.command.generic;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.helper.api.dictionary.DictionaryAPI;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.concurrent.CompletionException;

/**
 * An {@link ICommand} used to define words.
 *
 * @author beanbeanjuice
 */
public class DefineCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        String word = args.get(0);
        String languageCode;

        if (args.size() == 1) {
            languageCode = "en_US";
        } else {
            languageCode = args.get(1);
        }

        try {
            event.getChannel().sendMessageEmbeds(new DictionaryAPI(word, languageCode).dictionaryEmbed()).queue();
        } catch (CompletionException e) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Getting Dictionary Word",
                    "The word may not exist, or there may be some error getting it. Please try again and confirm that the word exists. " +
                            "If this error persists, please let me know!"
            )).queue();
        }
    }

    @Override
    public String getName() {
        return "define";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("def");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Define a word! Correct language codes are `en_US` - English," +
                " `hi` - Hindi, `es` - Spanish, `fr` - French, `ja` - Japanese," +
                " `ru` - Russian, `en_GB` - English (UK), `de` - German, `it` - Italian," +
                " `ko` - Korean, `pt-BR` - Brazilian Portuguese, `ar` - Arabic, `tr` - Turkish.";
    }

    @Override
    public String exampleUsage(String prefix) {
        return null;
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Any Word", true);
        usage.addUsage(CommandType.LANGUAGE_CODE, "Correct Language Code", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.GENERIC;
    }
}
