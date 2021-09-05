package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.cafeapi.cafebot.cafe.CafeUser;
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

import java.util.ArrayList;

/**
 * A command used for checking your balance.
 *
 * @author beanbeanjuice
 */
public class BalanceCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        // Checking if there is no specified user to check for.
        if (args.size() == 0) {
            CafeUser cafeUser = CafeBot.getServeHandler().getCafeUser(user);

            if (cafeUser == null) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                        "Error Getting User",
                        "There has been an error getting the Cafe User from the database. Please try again."
                )).queue();
                return;
            }

            event.getChannel().sendMessageEmbeds(selfBalanceEmbed(cafeUser)).queue();
            return;
        }

        // Getting the specified user.
        User person = CafeBot.getGeneralHelper().getUser(args.get(0));
        CafeUser cafeUser = CafeBot.getServeHandler().getCafeUser(person);

        if (cafeUser == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Getting User",
                    "There has been an error getting the Cafe User from the database. Please try again."
            )).queue();
            return;
        }

        event.getChannel().sendMessageEmbeds(otherBalanceEmbed(person, cafeUser)).queue();
    }

    public MessageEmbed selfBalanceEmbed(@NotNull CafeUser cafeUser) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("beanCoin Balance");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.addField("Orders Bought", cafeUser.getOrdersBought().toString(), true);
        embedBuilder.addField("Orders Received", cafeUser.getOrdersReceived().toString(), true);
        embedBuilder.setDescription("Your current balance is `" + CafeBot.getGeneralHelper().roundDouble(cafeUser.getBeanCoins()) + "` bC (beanCoins)!");
        return embedBuilder.build();
    }

    public MessageEmbed otherBalanceEmbed(@NotNull User user, @NotNull CafeUser cafeUser) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("beanCoin Balance");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.addField("Orders Bought", cafeUser.getOrdersBought().toString(), true);
        embedBuilder.addField("Orders Received", cafeUser.getOrdersReceived().toString(), true);
        embedBuilder.setDescription(user.getAsMention() + " has a current balance of `$" + CafeBot.getGeneralHelper().roundDouble(cafeUser.getBeanCoins()) + "` beanCoins!");
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("bal");
        arrayList.add("check-balance");
        arrayList.add("checkbalance");
        arrayList.add("checkbal");
        arrayList.add("check-bal");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Check your current beanCoin balance!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "bal` or `" + prefix + "bal @beanbeanjuice`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "Discord Mention", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.CAFE;
    }
}
