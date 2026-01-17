package com.beanbeanjuice.cafebot.commands.cafe;

import com.beanbeanjuice.cafebot.api.wrapper.type.DiscordUser;
import com.beanbeanjuice.cafebot.api.wrapper.type.MenuOrder;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class BalanceCommand extends Command implements ICommand {

    public BalanceCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Optional<OptionMapping> userMapping = Optional.ofNullable(event.getOption("user"));
        User user = userMapping.map(OptionMapping::getAsUser).orElse(event.getUser());

        CompletableFuture<DiscordUser> f1 = bot.getCafeAPI().getUserApi().getUser(user.getId());
        CompletableFuture<MenuOrder[]> f2 = bot.getCafeAPI().getOrderApi().getOrders(user.getId());

        f1.thenAcceptBoth(f2, (discordUser, orders) -> {
            event.getHook().sendMessageEmbeds(balanceEmbed(user, discordUser, orders)).queue();
        }).exceptionally((e) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Getting Balance",
                    "There was some sort of error getting your coin balance."
            )).queue();
            return null;
        });
    }

    public MessageEmbed balanceEmbed(User user, DiscordUser cafeUser, MenuOrder[] orders) {
        long ordersBought = Arrays.stream(orders).filter((order) -> order.getFromId().equals(user.getId())).count();
        long ordersReceived = Arrays.stream(orders).filter((order) -> order.getToId().equals(user.getId())).count();

        return new EmbedBuilder()
                .setTitle("cafeCoin Balance")
                .setColor(Helper.getRandomColor())
                .addField("Orders Bought", String.valueOf(ordersBought), true)
                .addField("Orders Received", String.valueOf(ordersReceived), true)
                .setDescription(user.getAsMention() + " has a current balance of `$" + Helper.roundFloat(cafeUser.getBalance()) + "` cC (cafeCoins)!")
                .setFooter("To learn how to make money do /help")
                .build();
    }

    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Get your balance!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CAFE;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to get the balance of.", false)
        };
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
    }

    @Override
    public boolean isEphemeral() {
        return false;
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
