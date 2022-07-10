package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.section.cafe.BeanCoinDonationHandler;
import com.beanbeanjuice.utility.section.cafe.ServeHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.logging.LogLevel;
import io.github.beanbeanjuice.cafeapi.cafebot.cafe.CafeType;
import io.github.beanbeanjuice.cafeapi.cafebot.cafe.CafeUser;
import io.github.beanbeanjuice.cafeapi.exception.api.CafeException;
import io.github.beanbeanjuice.cafeapi.generic.CafeGeneric;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * A command used to donate beanCoins.
 *
 * @author beanbeanjuice
 */
public class DonateBeanCoinsCommand implements ICommand {

    private final int HIGHEST_AMOUNT = 100;

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

        int amountToDonate = event.getOption("amount").getAsInt();

        // First, check if the user can donate.
        User donatorUser = event.getUser();
        User donateeUser = event.getOption("user").getAsUser();
        long minutesToDonate = BeanCoinDonationHandler.timeUntilDonate(donateeUser.getId());

        // If that can't donate
        if (minutesToDonate > -1) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Donation Error",
                    "That user has a donation cooldown. They cannot be donated to for `" + (minutesToDonate + 1) + "` minute(s)."
            )).queue();
            return;
        }

        // If that can donate
        CafeUser donator = ServeHandler.getCafeUser(donatorUser);
        CafeUser donatee = ServeHandler.getCafeUser(donateeUser);

        // Checking if the donatee was unable to be retrieved from the CafeAPI.
        if (donatee == null) {
            event.getChannel().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Getting User Information",
                    "There has been an error retrieving the specified user's information in the database."
            )).queue();
            return;
        }

        // Checking if the donator IS the donatee
        if (donatee.getUserID().equals(donatorUser.getId())) {
            event.getChannel().sendMessageEmbeds(Helper.errorEmbed(
                    "Cannot Donate to Self",
                    "You cannot donate to yourself!"
            )).queue();
            return;
        }

        // Checking if the donator was unable to be retrieved from the CafeAPI.
        if (donator == null) {
            event.getChannel().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Getting User Information",
                    "There has been an error retrieving your user information in the database."
            )).queue();
            return;
        }

        // Making sure they can only donate the amount that is in their balance.
        if (donator.getBeanCoins() < amountToDonate) {
            event.getChannel().sendMessageEmbeds(Helper.errorEmbed(
                    "Missing Balance",
                    "You don't have enough balance to donate that amount. You only have `" + donator.getBeanCoins() + "bC`."
            )).queue();
            return;
        }

        // Updating the Donator
        try {
            Bot.getCafeAPI().CAFE_USER.updateCafeUser(donator.getUserID(), CafeType.BEAN_COINS, donator.getBeanCoins() - amountToDonate);
        } catch (CafeException e) {
            event.getChannel().sendMessageEmbeds(Helper.sqlServerError(
                    "Error updating donation sender. There has been an error contacting the Cafe API."
            )).queue();
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Updating Donation Sender: " + e.getMessage(), e);
            return;
        }

        // Updating the Donatee
        try {
            Bot.getCafeAPI().CAFE_USER.updateCafeUser(donatee.getUserID(), CafeType.BEAN_COINS, donatee.getBeanCoins() + amountToDonate);
        } catch (CafeException e) {
            event.getChannel().sendMessageEmbeds(Helper.sqlServerError(
                    "Error updating donation receiver. There has been an error contacting the Cafe API."
            )).queue();
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Updating Donation Receiver: " + e.getMessage(), e);
            return;
        }

        // Updating the Donatee's Cooldown as well as in the cache
        try {
            Timestamp endingTime = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis() + BeanCoinDonationHandler.getCooldown()).toString());
            Bot.getCafeAPI().DONATION_USER.addDonationUser(donatee.getUserID(), endingTime);
            BeanCoinDonationHandler.addUser(donatee.getUserID(), endingTime);
        } catch (CafeException e) {
            Bot.getLogger().log(this.getClass(), LogLevel.WARN, "Cooldown Not Created for User `" + donatee.getUserID() + "`: " + e.getMessage(), e);
            return;
        }

        event.getChannel().sendMessageEmbeds(moneyEmbed(donatorUser, donateeUser, donator, donatee, amountToDonate)).queue();
    }

    /**
     * Creates the money {@link MessageEmbed}.
     * @param donatorUser {@link User donatorUser}.
     * @param donateeUser {@link User donateeUser}.
     * @param donator The {@link CafeUser donator}.
     * @param donatee The {@link CafeUser donatee}.
     * @param amount The {@link Integer amount} donated.
     * @return The created {@link MessageEmbed}.
     */
    private MessageEmbed moneyEmbed(@NotNull User donatorUser, @NotNull User donateeUser, @NotNull CafeUser donator,
                                    @NotNull CafeUser donatee, @NotNull Integer amount) {
        return new EmbedBuilder()
                .setTitle("beanCoin Donation")
                .setDescription(donatorUser.getAsMention() + " has donated `" + amount + "bC` to " + donateeUser.getAsMention() + "!\n\n" +
                        donatorUser.getAsMention() + "'s new balance is `" + Helper.roundDouble(donator.getBeanCoins()-amount)
                        + "bC` and " + donateeUser.getAsMention() + "'s " + "new balance is `" +
                        Helper.roundDouble(donatee.getBeanCoins()+amount) + "bC`!")
                .setColor(Helper.getRandomColor())
                .setFooter("You can donate to this user again in 1 hour!")
                .build();
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Donate up to 100 beanCoins to someone!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "donate-beancoins";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "The person to donate beanCoins™ to.", true, false));
        options.add(new OptionData(OptionType.INTEGER, "amount", "The amount of beanCoins™ to donate.", true, false)
                .setRequiredRange(1, HIGHEST_AMOUNT));
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
        return false;
    }

}
