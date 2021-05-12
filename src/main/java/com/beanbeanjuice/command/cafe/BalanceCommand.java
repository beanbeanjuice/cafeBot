package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.cafe.CafeCustomer;
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
            CafeCustomer cafeCustomer = BeanBot.getServeHandler().getCafeCustomer(user);

            if (cafeCustomer == null) {
                event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
                return;
            }

            event.getChannel().sendMessage(selfBalanceEmbed(cafeCustomer.getBeanCoinAmount())).queue();
            return;
        }

        User person = BeanBot.getGeneralHelper().getUser(args.get(0));
        CafeCustomer cafeCustomer = BeanBot.getServeHandler().getCafeCustomer(person);

        if (cafeCustomer == null) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        event.getChannel().sendMessage(otherBalanceEmbed(person, cafeCustomer.getBeanCoinAmount())).queue();

    }

    public MessageEmbed selfBalanceEmbed(@NotNull Double beanCoinAmount) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("beanCoin Balance");
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        embedBuilder.setDescription("Your current balance is `$" + BeanBot.getServeHandler().roundDouble(beanCoinAmount) + "`!"); // TODO: Round to 2 decimal places.
        return embedBuilder.build();
    }

    public MessageEmbed otherBalanceEmbed(@NotNull User user, @NotNull Double beanCoinAmount) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("beanCoin Balance");
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        embedBuilder.setDescription(user.getAsMention() + " has a current balance of `$" + BeanBot.getServeHandler().roundDouble(beanCoinAmount) + "`!"); // TODO: Round to 2 decimal places.
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
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.USER, "A discord user", false);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.CAFE;
    }
}
