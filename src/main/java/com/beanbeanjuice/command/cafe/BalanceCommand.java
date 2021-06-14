package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.sections.cafe.object.CafeCustomer;
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

        if (args.size() == 0) {
            CafeCustomer cafeCustomer = CafeBot.getServeHandler().getCafeCustomer(user);

            if (cafeCustomer == null) {
                event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
                return;
            }

            event.getChannel().sendMessage(selfBalanceEmbed(cafeCustomer)).queue();
            return;
        }

        User person = CafeBot.getGeneralHelper().getUser(args.get(0));
        CafeCustomer cafeCustomer = CafeBot.getServeHandler().getCafeCustomer(person);

        if (cafeCustomer == null) {
            event.getChannel().sendMessage(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        event.getChannel().sendMessage(otherBalanceEmbed(person, cafeCustomer)).queue();
    }

    public MessageEmbed selfBalanceEmbed(@NotNull CafeCustomer cafeCustomer) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("beanCoin Balance");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.addField("Orders Bought", cafeCustomer.getOrdersBought().toString(), true);
        embedBuilder.addField("Orders Received", cafeCustomer.getOrdersReceived().toString(), true);
        embedBuilder.setDescription("Your current balance is `" + CafeBot.getServeHandler().roundDouble(cafeCustomer.getBeanCoinAmount()) + "` bC (beanCoins)!");
        return embedBuilder.build();
    }

    public MessageEmbed otherBalanceEmbed(@NotNull User user, @NotNull CafeCustomer cafeCustomer) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("beanCoin Balance");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.addField("Orders Bought", cafeCustomer.getOrdersBought().toString(), true);
        embedBuilder.addField("Orders Received", cafeCustomer.getOrdersReceived().toString(), true);
        embedBuilder.setDescription(user.getAsMention() + " has a current balance of `$" + CafeBot.getServeHandler().roundDouble(cafeCustomer.getBeanCoinAmount()) + "` beanCoins!");
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
