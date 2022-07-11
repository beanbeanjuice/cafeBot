package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.section.cafe.ServeHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.logging.LogLevel;
import io.github.beanbeanjuice.cafeapi.cafebot.cafe.CafeType;
import io.github.beanbeanjuice.cafeapi.cafebot.cafe.CafeUser;
import io.github.beanbeanjuice.cafeapi.cafebot.words.Word;
import io.github.beanbeanjuice.cafeapi.exception.api.CafeException;
import io.github.beanbeanjuice.cafeapi.generic.CafeGeneric;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * An {@link ICommand} used to serve people.
 *
 * @author beanbeanjuice
 */
public class ServeCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        String word = event.getOption("word").getAsString();
        Word serveWord;
        try {
            // Checking if the word entered is a word.
            serveWord = Bot.getCafeAPI().WORD.getWord(word);
        } catch (CafeException e) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Not A Word",
                    "`" + word + "` is not a word. If this is a mistake, please create a " +
                            "bug report using `/bug-report`."
            )).queue();
            return;
        }

        CafeUser cafeUser = ServeHandler.getCafeUser(event.getUser());

        if (cafeUser == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Getting User Information",
                    "There has been an error getting your user information. Please try again."
            )).queue();
            return;
        }

        // Converting Timestamp to UTC time.
        Timestamp currentTimestamp = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis()).toString());

        // Checking if the user CAN serve someone.
        if (!ServeHandler.canServe(cafeUser)) {
            event.getHook().sendMessageEmbeds(cannotServeEmbed(ServeHandler.minutesBetween(cafeUser))).queue();
            return;
        }

        // Calculates the tip
        Double calculatedTip = ServeHandler.calculateTip(serveWord);

        // Updates the Balance and Last Serving Time for the user.
        try {
            Bot.getCafeAPI().CAFE_USER.updateCafeUser(cafeUser.getUserID(), CafeType.LAST_SERVING_TIME, currentTimestamp);
            Bot.getCafeAPI().CAFE_USER.updateCafeUser(cafeUser.getUserID(), CafeType.BEAN_COINS, cafeUser.getBeanCoins() + calculatedTip);
        } catch (CafeException e) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Updating Cafe User",
                    "There has been an error. " + e.getMessage()
            )).queue();
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Updating Cafe User: " + e.getMessage(), e);
            return;
        }

        // Attempts to update the word
        try {
            Bot.getCafeAPI().WORD.updateWord(serveWord.getWord(), serveWord.getUses() + 1);
        } catch (CafeException e) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Updating Word",
                    "You have still received your tip from the word, but there has been an error updating it. " + e.getMessage()
            )).queue();
            Bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Updating Word '" + word + "': " + e.getMessage(), e);
        }

        // Sends the message embed.
        if (event.getOption("user") == null) {
            event.getHook().sendMessageEmbeds(serveSingleEmbed(word, calculatedTip, (cafeUser.getBeanCoins()) + calculatedTip)).queue();
        } else {
            event.getHook().sendMessageEmbeds(serveSomeoneEmbed(word, calculatedTip, (cafeUser.getBeanCoins() + calculatedTip),
                    event.getUser(), event.getOption("user").getAsUser())).queue();
        }
    }

    private MessageEmbed cannotServeEmbed(@NotNull Integer minutesLeft) {
        int cooldownAmount = ServeHandler.getMinutesUntilCanServe() - minutesLeft;
        return new EmbedBuilder()
                .setTitle("Cannot Serve - Cooldown")
                .setDescription("You cannot serve anything right now because you are on a cooldown for `" + cooldownAmount + "` more minutes!")
                .setColor(Color.red)
                .build();
    }

    private MessageEmbed serveSingleEmbed(@NotNull String word, @NotNull Double tipFromWord, @NotNull Double currentBalance) {
        return new EmbedBuilder()
                .setTitle("Successfully Served!")
                .setDescription("You have successfully served a `" + word + "`!")
                .addField("Tip From Serving", Helper.roundDouble(tipFromWord).toString(), true)
                .addField("Current Balance", Helper.roundDouble(currentBalance).toString(), true)
                .addField("Time Until Next Serving", ServeHandler.getMinutesUntilCanServe().toString(), true)
                .setColor(Helper.getRandomColor())
                .build();
    }

    private MessageEmbed serveSomeoneEmbed(@NotNull String word, @NotNull Double tipFromWord, @NotNull Double currentBalance, @NotNull User server, @NotNull User userServed) {
        return new EmbedBuilder()
                .setTitle("Successfully Served!")
                .setDescription(server.getAsMention() + " has successfully served a `" + word + "` to " + userServed.getAsMention() + "!")
                .addField("Tip From Serving", Helper.roundDouble(tipFromWord).toString(), true)
                .addField("Current Balance", Helper.roundDouble(currentBalance).toString(), true)
                .addField("Time Until Next Serving", ServeHandler.getMinutesUntilCanServe().toString(), true)
                .setColor(Helper.getRandomColor())
                .build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Serve a \"word\" to someone... or not!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/serve computer` or `/serve cupcake`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "word", "Any word to serve!", true, false));
        options.add(new OptionData(OptionType.USER, "user", "A user to serve the word to!", false, false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.CAFE;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

}
