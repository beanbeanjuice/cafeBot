package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import io.github.beanbeanjuice.cafeapi.cafebot.cafe.CafeUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
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
        CafeUser cafeUser;
        User person = null;

        // Checking if there are no arguments. (Self Balance)
        if (args.size() == 0) {
            cafeUser = CafeBot.getServeHandler().getCafeUser(user);
        } else {
            person = CafeBot.getGeneralHelper().getUser(args.get(0));
            cafeUser = CafeBot.getServeHandler().getCafeUser(person);
        }

        // Checking if there is an error getting the balance.
        if (cafeUser == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Getting User",
                    "There has been an error getting the Cafe User from the database. Please try again."
            )).queue();
            return;
        }

        if (person == null) {
            event.getChannel().sendMessageEmbeds(selfBalanceEmbed(cafeUser, event.getGuild())).queue();
        } else {
            event.getChannel().sendMessageEmbeds(otherBalanceEmbed(person, cafeUser, event.getGuild())).queue();
        }
    }

    /**
     * Creates the balance {@link MessageEmbed} for getting a self balance.
     * @param cafeUser The {@link CafeUser} to get the balance of.
     * @return The created {@link MessageEmbed}.
     */
    public MessageEmbed selfBalanceEmbed(@NotNull CafeUser cafeUser, @NotNull Guild guild) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("beanCoin Balance");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.addField("Orders Bought", cafeUser.getOrdersBought().toString(), true);
        embedBuilder.addField("Orders Received", cafeUser.getOrdersReceived().toString(), true);
        embedBuilder.setDescription("Your current balance is `" + CafeBot.getGeneralHelper().roundDouble(cafeUser.getBeanCoins()) + "` bC (beanCoins)!");
        embedBuilder.setFooter("To learn how to make money do " + CafeBot.getGuildHandler().getCustomGuild(guild).getPrefix() + "help serve");
        return embedBuilder.build();
    }

    /**
     * Creates the balance {@link MessageEmbed} for getting the balance of a {@link CafeUser}.
     * @param user The {@link User}.
     * @param cafeUser The {@link CafeUser} specified.
     * @return The created {@link MessageEmbed}.
     */
    public MessageEmbed otherBalanceEmbed(@NotNull User user, @NotNull CafeUser cafeUser, @NotNull Guild guild) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("beanCoin Balance");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.addField("Orders Bought", cafeUser.getOrdersBought().toString(), true);
        embedBuilder.addField("Orders Received", cafeUser.getOrdersReceived().toString(), true);
        embedBuilder.setDescription(user.getAsMention() + " has a current balance of `$" + CafeBot.getGeneralHelper().roundDouble(cafeUser.getBeanCoins()) + "` beanCoins!");
        embedBuilder.setFooter("To learn how to make money do " + CafeBot.getGuildHandler().getCustomGuild(guild).getPrefix() + "help serve");
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
