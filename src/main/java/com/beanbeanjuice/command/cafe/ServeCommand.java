package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.cafe.CafeCustomer;
import com.beanbeanjuice.utility.cafe.ServeWord;
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
import java.sql.Date;

/**
 * A command used to serve words.
 *
 * @author beanbeanjuice
 */
public class ServeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        String word = args.get(0).toLowerCase();
        ServeWord serveWord = BeanBot.getServeHandler().getWord(word);

        // Checking if the word entered is a word.
        if (serveWord == null) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                    "Not A Word",
                    "`" + word + "` is not a word. If this is a mistake, please create a " +
                            "bug report using `" + ctx.getPrefix() + "bug-report`."
            )).queue();
            return;
        }

        CafeCustomer cafeCustomer = BeanBot.getServeHandler().getCafeCustomer(user);

        // Checking if CafeCustomer is null.
        if (cafeCustomer == null) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }


        if (!BeanBot.getServeHandler().canServe(cafeCustomer)) {
            event.getChannel().sendMessage(cannotServeEmbed(BeanBot.getServeHandler().minutesBetween(cafeCustomer))).queue();
            return;
        }

        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        Double calculatedTip = BeanBot.getServeHandler().calculateTip(serveWord);

        System.out.println(currentDate.toString()); // TODO: Remove

        if (!BeanBot.getServeHandler().updateTip(cafeCustomer, currentDate, calculatedTip) && !BeanBot.getServeHandler().updateWord(serveWord)) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        if (args.size() == 1) {
            event.getChannel().sendMessage(serveSingleEmbed(word, calculatedTip, (cafeCustomer.getBeanCoinAmount()) + calculatedTip)).queue();
        } else {
            event.getChannel().sendMessage(serveSomeoneEmbed(word, calculatedTip, (cafeCustomer.getBeanCoinAmount() + calculatedTip),
                    user, BeanBot.getGeneralHelper().getUser(args.get(1)))).queue();
        }

    }

    private MessageEmbed cannotServeEmbed(@NotNull Integer minutesLeft) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Cannot Serve - Cooldown");
        int cooldownAmount = BeanBot.getServeHandler().getMinutesUntilCanServe() - minutesLeft;
        embedBuilder.setDescription("You cannot serve anything right now because you are on a cooldown for `" + cooldownAmount + "` more minutes!");
        embedBuilder.setColor(Color.red);
        return embedBuilder.build();
    }

    private MessageEmbed serveSingleEmbed(@NotNull String word, @NotNull Double tipFromWord, @NotNull Double currentBalance) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Successfully Served!");
        embedBuilder.setDescription("You have successfully served a `" + word + "`!");
        embedBuilder.addField("Tip From Serving", BeanBot.getServeHandler().roundDouble(tipFromWord).toString(), true);
        embedBuilder.addField("Current Balance", BeanBot.getServeHandler().roundDouble(currentBalance).toString(), true);
        embedBuilder.addField("Time Until Next Serving", BeanBot.getServeHandler().getMinutesUntilCanServe().toString(), true);
        return embedBuilder.build();
    }

    private MessageEmbed serveSomeoneEmbed(@NotNull String word, @NotNull Double tipFromWord, @NotNull Double currentBalance, @NotNull User server, @NotNull User userServed) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Successfully Served!");
        embedBuilder.setDescription(server.getAsMention() + " has successfully served a `" + word + "` to " + userServed.getAsMention() + "!");
        embedBuilder.addField("Tip From Serving", BeanBot.getServeHandler().roundDouble(tipFromWord).toString(), true);
        embedBuilder.addField("Current Balance", BeanBot.getServeHandler().roundDouble(currentBalance).toString(), true);
        embedBuilder.addField("Time Until Next Serving", BeanBot.getServeHandler().getMinutesUntilCanServe().toString(), true);
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
