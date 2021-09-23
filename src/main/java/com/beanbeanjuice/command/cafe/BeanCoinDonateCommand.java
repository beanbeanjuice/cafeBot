package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.logger.LogLevel;
import io.github.beanbeanjuice.cafeapi.cafebot.cafe.CafeType;
import io.github.beanbeanjuice.cafeapi.cafebot.cafe.CafeUser;
import io.github.beanbeanjuice.cafeapi.exception.CafeException;
import io.github.beanbeanjuice.cafeapi.generic.CafeGeneric;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * An {@link ICommand} used to donate beanCoins to a {@link User}.
 *
 * @author beanbeanjuice
 */
public class BeanCoinDonateCommand implements ICommand {

    private final int HIGHEST_AMOUNT = 25;

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {
        int amountToDonate = Integer.parseInt(args.get(0));

        // Making sure they can't donate their entire balance.
        if (amountToDonate > HIGHEST_AMOUNT) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Amount Too High",
                    "The maximum amount of `beanCoins` you can donate to someone at a given time is only `25bC`."
            )).queue();
            return;
        }

        // Checking if they CAN donate.
        User donateeUser = CafeBot.getGeneralHelper().getUser(args.get(1));
        long minutesToDonate = CafeBot.getBeanCoinDonationHandler().timeUntilDonate(donateeUser.getId());

        // If they can...
        if (minutesToDonate <= -1) {

            CafeUser donator = CafeBot.getServeHandler().getCafeUser(user);
            CafeUser donatee = CafeBot.getServeHandler().getCafeUser(CafeBot.getGeneralHelper().getUser(args.get(1)));

            if (donatee == null) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                        "Error Getting User Information",
                        "There has been an error retrieving the specified user's information in the database."
                )).queue();
                return;
            }

            if (donatee.getUserID().equals(user.getId())) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                        "Cannot Donate to Self",
                        "You cannot donate to yourself!"
                )).queue();
                return;
            }

            if (donator == null) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                        "Error Getting User Information",
                        "There has been an error retrieving your user information in the database."
                )).queue();
                return;
            }

            // Making sure they can only donate the amount that is in their balance.
            if (donator.getBeanCoins() < amountToDonate) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                        "Missing Balance",
                        "You don't have enough balance to donate that amount. You only have `" + donator.getBeanCoins() + "bC`."
                )).queue();
                return;
            }

            // Updating the Donator
            try {
                CafeBot.getCafeAPI().cafeUsers().updateCafeUser(donator.getUserID(), CafeType.BEAN_COINS, donator.getBeanCoins() - amountToDonate);
            } catch (CafeException e) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError(
                        "Error updating donation sender. There has been an error contacting the Cafe API."
                )).queue();
                CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Updating Donation Sender: " + e.getMessage(), e);
                return;
            }

            // Updating the Donatee
            try {
                CafeBot.getCafeAPI().cafeUsers().updateCafeUser(donatee.getUserID(), CafeType.BEAN_COINS, donatee.getBeanCoins() + amountToDonate);
            } catch (CafeException e) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError(
                        "Error updating donation receiver. There has been an error contacting the Cafe API."
                )).queue();
                CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Updating Donation Receiver: " + e.getMessage(), e);
                return;
            }

            // Updating the Donatee's Cooldown as well as in the cache
            try {
                Timestamp endingTime = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis() + CafeBot.getBeanCoinDonationHandler().getCooldown()).toString());
                CafeBot.getCafeAPI().donationUsers().addDonationUser(donatee.getUserID(), endingTime);
                CafeBot.getBeanCoinDonationHandler().addUser(donatee.getUserID(), endingTime);
            } catch (CafeException e) {
                CafeBot.getLogManager().log(this.getClass(), LogLevel.WARN, "Cooldown Not Created for User `" + donatee.getUserID() + "`: " + e.getMessage(), e);
                return;
            }

            event.getChannel().sendMessageEmbeds(moneyEmbed(donator, donatee, amountToDonate)).queue();
            return;
        } else {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Donation Error",
                    "That user has a donation cooldown. They cannot be donated to for `" + (minutesToDonate + 1) + "` minute(s)."
            )).queue();
            return;
        }
    }

    /**
     * Creates the money {@link MessageEmbed}.
     * @param donator The {@link CafeUser donator}.
     * @param donatee The {@link CafeUser donatee}.
     * @param amount The {@link Integer amount} donated.
     * @return The created {@link MessageEmbed}.
     */
    private MessageEmbed moneyEmbed(@NotNull CafeUser donator, @NotNull CafeUser donatee, @NotNull Integer amount) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("beanCoin Donation");
        User donator1 = CafeBot.getGeneralHelper().getUser(donator.getUserID());
        User donatee1 = CafeBot.getGeneralHelper().getUser(donatee.getUserID());
        embedBuilder.setDescription(donator1.getAsMention() + " has donated `" + amount + "bC` to " + donatee1.getAsMention() + "!\n\n" +
                donator1.getAsMention() + "'s new balance is `" + CafeBot.getGeneralHelper().roundDouble(donator.getBeanCoins()-amount) + "bC` and " + donatee1.getAsMention() + "'s " +
                "new balance is `" + CafeBot.getGeneralHelper().roundDouble(donatee.getBeanCoins()+amount) + "bC`!");
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
        embedBuilder.setFooter("You can donate to this user again in 1 hour!");
        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "bean-coin-donate";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("beancoindonate");
        arrayList.add("bean-coin-donation");
        arrayList.add("beancoindonation");
        arrayList.add("bc-donate");
        arrayList.add("bcdonate");
        arrayList.add("bean-coin-donate");
        arrayList.add("beancoindonate");
        arrayList.add("beancoin-donate");
        arrayList.add("beancoindonate");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Donate up to 25bC to someone!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "bc-donate 25 @beanbeanjuice`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.NUMBER, "Amount of bC to Donate", true);
        usage.addUsage(CommandType.USER, "Discord Mention to Donate To", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.CAFE;
    }
}
