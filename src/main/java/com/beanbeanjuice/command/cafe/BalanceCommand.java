package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.handler.cafe.ServeHandler;
import com.beanbeanjuice.utility.helper.Helper;
import io.github.beanbeanjuice.cafeapi.cafebot.cafe.CafeUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A command used for checking your balance.
 *
 * @author beanbeanjuice
 */
public class BalanceCommand implements ICommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        User user = event.getUser();
        boolean self = true;

        try {
            user = event.getOption("user").getAsUser();
            self = false;
        } catch (NullPointerException ignored) {}

        CafeUser cafeUser = ServeHandler.getCafeUser(user);

        // Checking if there was an error getting the user.
        if (cafeUser == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Getting User",
                    "There has been an error getting the Cafe User from the database. Please try again."
            )).queue();
            return;
        }

        if (self) { event.getHook().sendMessageEmbeds(selfBalanceEmbed(cafeUser)).queue(); }
        else { event.getHook().sendMessageEmbeds(otherBalanceEmbed(user, cafeUser)).queue(); }
    }

    /**
     * Creates the balance {@link MessageEmbed} for getting a self balance.
     * @param cafeUser The {@link CafeUser} to get the balance of.
     * @return The created {@link MessageEmbed}.
     */
    public MessageEmbed selfBalanceEmbed(@NotNull CafeUser cafeUser) {
        return new EmbedBuilder()
                .setTitle("beanCoin Balance")
                .setColor(Helper.getRandomColor())
                .addField("Orders Bought", cafeUser.getOrdersBought().toString(), true)
                .addField("Orders Received", cafeUser.getOrdersReceived().toString(), true)
                .setDescription("Your current balance is `" + Helper.roundDouble(cafeUser.getBeanCoins()) + "` bC (beanCoins)!")
                .setFooter("To learn how to make money do /help serve")
                .build();
    }

    /**
     * Creates the balance {@link MessageEmbed} for getting the balance of a {@link CafeUser}.
     * @param user The {@link User}.
     * @param cafeUser The {@link CafeUser} specified.
     * @return The created {@link MessageEmbed}.
     */
    public MessageEmbed otherBalanceEmbed(@NotNull User user, @NotNull CafeUser cafeUser) {
        return new EmbedBuilder()
                .setTitle("beanCoin Balance")
                .setColor(Helper.getRandomColor())
                .addField("Orders Bought", cafeUser.getOrdersBought().toString(), true)
                .addField("Orders Received", cafeUser.getOrdersReceived().toString(), true)
                .setDescription(user.getAsMention() + " has a current balance of `$" + Helper.roundDouble(cafeUser.getBeanCoins()) + "` bC (beanCoins)!")
                .setFooter("To learn how to make money do /help serve")
                .build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Check yours or someone's balance.";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/balance` or `/balance @beanbeanjuice`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "Person to check the balance of.", false, false));
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

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

}
