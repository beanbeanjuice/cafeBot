package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.cafeapi.cafebot.cafe.CafeType;
import com.beanbeanjuice.cafeapi.cafebot.cafe.CafeUser;
import com.beanbeanjuice.cafeapi.cafebot.words.Word;
import com.beanbeanjuice.cafeapi.exception.CafeException;
import com.beanbeanjuice.cafeapi.generic.CafeGeneric;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * A command used to serve {@link Word}.
 *
 * @author beanbeanjuice
 */
public class ServeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        String word = args.get(0).toLowerCase();
        Word serveWord;
        try {
            // Checking if the word entered is a word.
            serveWord = CafeBot.getCafeAPI().words().getWord(word);
        } catch (CafeException e) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Not A Word",
                    "`" + word + "` is not a word. If this is a mistake, please create a " +
                            "bug report using `" + ctx.getPrefix() + "bug-report`."
            )).queue();
            return;
        }

        CafeUser cafeUser = CafeBot.getServeHandler().getCafeUser(user);

        if (cafeUser == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Getting User Information",
                    "There has been an error getting your user information. Please try again."
            )).queue();
            return;
        }

        // Converting Timestamp to UTC time.
        Timestamp currentTimestamp = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis()).toString());

        // Checking if the user CAN serve someone.
        if (!CafeBot.getServeHandler().canServe(cafeUser)) {
            event.getChannel().sendMessageEmbeds(cannotServeEmbed(CafeBot.getServeHandler().minutesBetween(cafeUser))).queue();
            return;
        }

        // Calculates the tip
        Double calculatedTip = CafeBot.getServeHandler().calculateTip(serveWord);

        // Updates the Balance and Last Serving Time for the user.
        try {
            CafeBot.getCafeAPI().cafeUsers().updateCafeUser(cafeUser.getUserID(), CafeType.LAST_SERVING_TIME, currentTimestamp);
            CafeBot.getCafeAPI().cafeUsers().updateCafeUser(cafeUser.getUserID(), CafeType.BEAN_COINS, cafeUser.getBeanCoins() + calculatedTip);
        } catch (CafeException e) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Updating Cafe User",
                    "There has been an error. " + e.getMessage()
            )).queue();
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Updating Cafe User: " + e.getMessage(), e);
            return;
        }

        // Attempts to update the word
        try {
            CafeBot.getCafeAPI().words().updateWord(serveWord.getWord(), serveWord.getUses() + 1);
        } catch (CafeException e) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Updating Word",
                    "You have still received your tip from the word, but there has been an error updating it. " + e.getMessage()
            )).queue();
            CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Error Updating Word '" + word + "': " + e.getMessage(), e);
        }

        // Sends the message embed.
        if (args.size() == 1) {
            event.getChannel().sendMessageEmbeds(serveSingleEmbed(word, calculatedTip, (cafeUser.getBeanCoins()) + calculatedTip)).queue();
        } else {
            event.getChannel().sendMessageEmbeds(serveSomeoneEmbed(word, calculatedTip, (cafeUser.getBeanCoins() + calculatedTip),
                    user, CafeBot.getGeneralHelper().getUser(args.get(1)))).queue();
        }

    }

    private MessageEmbed cannotServeEmbed(@NotNull Integer minutesLeft) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Cannot Serve - Cooldown");
        int cooldownAmount = CafeBot.getServeHandler().getMinutesUntilCanServe() - minutesLeft;
        embedBuilder.setDescription("You cannot serve anything right now because you are on a cooldown for `" + cooldownAmount + "` more minutes!");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    private MessageEmbed serveSingleEmbed(@NotNull String word, @NotNull Double tipFromWord, @NotNull Double currentBalance) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Successfully Served!");
        embedBuilder.setDescription("You have successfully served a `" + word + "`!");
        embedBuilder.addField("Tip From Serving", CafeBot.getGeneralHelper().roundDouble(tipFromWord).toString(), true);
        embedBuilder.addField("Current Balance", CafeBot.getGeneralHelper().roundDouble(currentBalance).toString(), true);
        embedBuilder.addField("Time Until Next Serving", CafeBot.getServeHandler().getMinutesUntilCanServe().toString(), true);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    private MessageEmbed serveSomeoneEmbed(@NotNull String word, @NotNull Double tipFromWord, @NotNull Double currentBalance, @NotNull User server, @NotNull User userServed) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Successfully Served!");
        embedBuilder.setDescription(server.getAsMention() + " has successfully served a `" + word + "` to " + userServed.getAsMention() + "!");
        embedBuilder.addField("Tip From Serving", CafeBot.getGeneralHelper().roundDouble(tipFromWord).toString(), true);
        embedBuilder.addField("Current Balance", CafeBot.getGeneralHelper().roundDouble(currentBalance).toString(), true);
        embedBuilder.addField("Time Until Next Serving", CafeBot.getServeHandler().getMinutesUntilCanServe().toString(), true);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "serve";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Serve some stuff!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "serve keyboard` or `" + prefix + "serve mouse @beanbeanjuice`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Any English Word", true);
        usage.addUsage(CommandType.USER, "Discord Mention", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.CAFE;
    }
}
