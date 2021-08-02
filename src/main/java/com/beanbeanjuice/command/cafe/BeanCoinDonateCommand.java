package com.beanbeanjuice.command.cafe;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.sections.cafe.object.CafeCustomer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

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
        Long minutesToDonate = CafeBot.getBeanCoinDonationHandler().timeUntilDonate(CafeBot.getGeneralHelper().getUser(args.get(1)).getId());
        if (minutesToDonate <= -1) {
            CafeCustomer donator = CafeBot.getServeHandler().getCafeCustomer(user);
            CafeCustomer donatee = CafeBot.getServeHandler().getCafeCustomer(CafeBot.getGeneralHelper().getUser(args.get(1)));

            // Making sure they can only donate the amount that is in their balance.
            if (donator.getBeanCoinAmount() < amountToDonate) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                        "Missing Balance",
                        "You don't have enough balance to donate that amount. You only have `" + donator.getBeanCoinAmount() + "bC`."
                )).queue();
                return;
            }

            // Checking if the donator/donatee exists.
            if (donatee == null || donator == null) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError("Donator/Donatee Does Not Exist")).queue();
                return;
            }

            // Updating the Donator
            if (!CafeBot.getBeanCoinDonationHandler().updateCafeCustomer(donator, donator.getBeanCoinAmount() - amountToDonate)) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError("Unable to Update the Donator")).queue();
                return;
            }

            // Updating the Donatee
            if (!CafeBot.getBeanCoinDonationHandler().updateCafeCustomer(donatee, donatee.getBeanCoinAmount() + amountToDonate)) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError("Unable to Update the Donation Receiver")).queue();
                return;
            }

            // Updating the Donatee's Cooldown
            if (!CafeBot.getBeanCoinDonationHandler().addUser(donatee.getUserID())) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError("Unable to Update the Donation Cooldown")).queue();
                return;
            }

            event.getChannel().sendMessageEmbeds(moneyEmbed(donator, donatee, amountToDonate)).queue();
            return;
        } else {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Donation Error",
                    "That user has a donation cooldown. They cannot be donated to for `" + (minutesToDonate + 1) + "` minute(s)."
            )).queue();
        }
    }

    private MessageEmbed moneyEmbed(@NotNull CafeCustomer donator, @NotNull CafeCustomer donatee, @NotNull Integer amount) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("beanCoin Donation");
        User donator1 = CafeBot.getGeneralHelper().getUser(donator.getUserID());
        User donatee1 = CafeBot.getGeneralHelper().getUser(donatee.getUserID());
        embedBuilder.setDescription(donator1.getAsMention() + " has donated `" + amount + "bC` to " + donatee1.getAsMention() + "!\n\n" +
                donator1.getAsMention() + "'s new balance is `" + CafeBot.getGeneralHelper().roundDouble(donator.getBeanCoinAmount()-amount) + "bC` and " + donatee1.getAsMention() + "'s " +
                "new balance is `" + CafeBot.getGeneralHelper().roundDouble(donatee.getBeanCoinAmount()+amount) + "bC`!");
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
