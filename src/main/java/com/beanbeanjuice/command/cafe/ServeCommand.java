package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.sections.cafe.object.CafeCustomer;
import com.beanbeanjuice.utility.sections.cafe.object.ServeWord;
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
 * A command used to serve words.
 *
 * @author beanbeanjuice
 */
public class ServeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        String word = args.get(0).toLowerCase();
        ServeWord serveWord = CafeBot.getServeHandler().getWord(word);

        // Checking if the word entered is a word.
        if (serveWord == null) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().errorEmbed(
                    "Not A Word",
                    "`" + word + "` is not a word. If this is a mistake, please create a " +
                            "bug report using `" + ctx.getPrefix() + "bug-report`."
            )).queue();
            return;
        }

        CafeCustomer cafeCustomer = CafeBot.getServeHandler().getCafeCustomer(user);

        // Checking if CafeCustomer is null.
        if (cafeCustomer == null) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        // Checking if the user CAN serve someone.
        if (!CafeBot.getServeHandler().canServe(cafeCustomer)) {
            event.getChannel().sendMessage(cannotServeEmbed(CafeBot.getServeHandler().minutesBetween(cafeCustomer))).queue();
            return;
        }

        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        Double calculatedTip = CafeBot.getServeHandler().calculateTip(serveWord);

        // Updates the Balance for the User
        if (!CafeBot.getServeHandler().updateTip(cafeCustomer, currentDate, calculatedTip)) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        // Updates the Word Used
        if (!CafeBot.getServeHandler().updateWord(serveWord)) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        if (args.size() == 1) {
            event.getChannel().sendMessage(serveSingleEmbed(word, calculatedTip, (cafeCustomer.getBeanCoinAmount()) + calculatedTip)).queue();
        } else {
            event.getChannel().sendMessage(serveSomeoneEmbed(word, calculatedTip, (cafeCustomer.getBeanCoinAmount() + calculatedTip),
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
        embedBuilder.addField("Tip From Serving", CafeBot.getServeHandler().roundDouble(tipFromWord).toString(), true);
        embedBuilder.addField("Current Balance", CafeBot.getServeHandler().roundDouble(currentBalance).toString(), true);
        embedBuilder.addField("Time Until Next Serving", CafeBot.getServeHandler().getMinutesUntilCanServe().toString(), true);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        return embedBuilder.build();
    }

    private MessageEmbed serveSomeoneEmbed(@NotNull String word, @NotNull Double tipFromWord, @NotNull Double currentBalance, @NotNull User server, @NotNull User userServed) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Successfully Served!");
        embedBuilder.setDescription(server.getAsMention() + " has successfully served a `" + word + "` to " + userServed.getAsMention() + "!");
        embedBuilder.addField("Tip From Serving", CafeBot.getServeHandler().roundDouble(tipFromWord).toString(), true);
        embedBuilder.addField("Current Balance", CafeBot.getServeHandler().roundDouble(currentBalance).toString(), true);
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
    public String exampleUsage() {
        return "`!!serve keyboard` or `!!serve mouse @beanbeanjuice`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Any English Word", true);
        usage.addUsage(CommandType.USER, "A Mentioned User", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.CAFE;
    }
}
