package com.beanbeanjuice.cafebot.commands.cafe;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.beancoins.users.DonationUsersEndpoint;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe.CafeType;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe.CafeUser;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.cafe.CafeUsersEndpoint;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class DonateCommand extends Command implements ICommand {

    private final int WAIT_TIME = 15;

    public DonateCommand(final CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Optional<OptionMapping> receiverMapping = Optional.ofNullable(event.getOption("user"));
        User receiver = receiverMapping.map(OptionMapping::getAsUser).orElseThrow();  // Shouldn't be null.
        User sender = event.getUser();

        if (receiver == sender) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed("Self Donation", "You can't donate to yourself!")).queue();
            return;
        }

        Optional<OptionMapping> amountMapping = Optional.ofNullable(event.getOption("amount"));
        double amount = amountMapping.map(OptionMapping::getAsDouble).orElseThrow();  // Shouldn't be null.

        CafeUsersEndpoint usersEndpoint = cafeBot.getCafeAPI().getCafeUsersEndpoint();
        DonationUsersEndpoint donationEndpoint = cafeBot.getCafeAPI().getDonationUsersEndpoint();

        CompletableFuture<CafeUser> getSenderFuture = usersEndpoint.getAndCreateCafeUser(sender.getId());
        CompletableFuture<CafeUser> getReceiverFuture = usersEndpoint.getAndCreateCafeUser(receiver.getId());
        CompletableFuture<Pair<CafeUser, CafeUser>> donationPairFuture = getSenderFuture.thenCombineAsync(getReceiverFuture, Pair::of);
        CompletableFuture<Optional<Timestamp>> getSenderDonationTimestampFuture = donationEndpoint.getUserDonationTime(receiver.getId());

        getSenderDonationTimestampFuture
                .exceptionallyComposeAsync((e) -> CompletableFuture.supplyAsync(Optional::empty))
                .thenCombineAsync(donationPairFuture, (timestampOptional, donationPair) -> {
                    CafeUser senderCafeUser = donationPair.getLeft();
                    CafeUser receiverCafeUser = donationPair.getRight();

                    if (senderCafeUser.getBeanCoins() < amount) {
                        sendNotEnoughCoinsEmbed(event, senderCafeUser, amount);
                        return false;
                    }

                    boolean canReceiveDonations = timestampOptional.map(this::canReceiveDonation).orElse(true);
                    if (!canReceiveDonations) {
                        sendNeedToWaitEmbed(event, timestampOptional.get());
                        return false;
                    }

                    double newSenderBalance = senderCafeUser.getBeanCoins() - amount;
                    double newReceiverBalance = receiverCafeUser.getBeanCoins() + amount;
                    Timestamp newReceiverDonationTime = new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(WAIT_TIME));

                    usersEndpoint.updateCafeUser(sender.getId(), CafeType.BEAN_COINS, newSenderBalance);
                    usersEndpoint.updateCafeUser(receiver.getId(), CafeType.BEAN_COINS, newReceiverBalance);
                    donationEndpoint
                            .deleteDonationUser(receiver.getId())
                            .thenRunAsync(() -> donationEndpoint.addDonationUser(receiver.getId(), newReceiverDonationTime));

                    sendSuccessToSender(event, receiver);
                    sendDonationEmbed(event, sender, receiver, amount);

                    return true;
                })
                .exceptionallyAsync((e) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Donating",
                            "There was an error donating to the user: " + e.getMessage()
                    )).queue();

                    cafeBot.getLogger().log(DonateCommand.class, LogLevel.WARN, "There was an error donating to a user: " + e.getMessage(), e.getCause());
                    return null;
                });
    }

    private void sendNotEnoughCoinsEmbed(final SlashCommandInteractionEvent event, final CafeUser cafeUser, final double amount) {
        event.getHook().sendMessageEmbeds(notEnoughCoinsEmbed(cafeUser.getBeanCoins(), amount)).queue();
    }

    private void sendNeedToWaitEmbed(final SlashCommandInteractionEvent event, final Timestamp timestamp) {
        event.getHook().sendMessageEmbeds(needToWaitEmbed(timestamp)).queue();
    }

    private void sendSuccessToSender(final SlashCommandInteractionEvent event, final User receiver) {
        event.getHook().sendMessageEmbeds(successEmbed(receiver)).queue();
    }

    private void sendDonationEmbed(final SlashCommandInteractionEvent event, final User sender, final User receiver, final double amount) {
        event.getChannel().sendMessageEmbeds(donationEmbed(sender, receiver, amount)).mention(receiver).queue();
    }

    private boolean canReceiveDonation(final Timestamp timestamp) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return timestamp.before(now);
    }

    private MessageEmbed notEnoughCoinsEmbed(final double balance, final double amount) {
        return Helper.errorEmbed(
                "Not Enough Coins",
                String.format("""
                        Sorry, you don't have enough bC to donate to that person. \
                        You have **%.2f bC**, which is *not* enough to donate **%.2f bC** to that person. \
                        Maybe try working for a bit? You can use `/serve` to work.
                        """, balance, amount)
        );
    }

    private MessageEmbed needToWaitEmbed(final Timestamp timestamp) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        int secondsToWait = (int) TimeUnit.MILLISECONDS.toSeconds(timestamp.getTime() - now.getTime());
        String secondsString = (secondsToWait == 1) ? "second" : "seconds";

        return Helper.errorEmbed(
                "That User Can't Receive Donations",
                String.format("""
                        That user needs to wait **%d** %s before they are able to \
                        receive another donation.
                        """, secondsToWait, secondsString)
        );
    }

    private MessageEmbed successEmbed(final User receiver) {
        return Helper.successEmbed(
                "Donation Received",
                String.format("%s has received your donation!", receiver.getAsMention())
        );
    }

    private MessageEmbed donationEmbed(final User sender, final User receiver, final double amount) {
        return Helper.successEmbed(
                "Donation!",
                String.format("""
                        Aww!~ <a:wowowow:886217210010431508>
                        
                        %s just donated **%.2f bC** to %s!
                        """, sender.getAsMention(), amount, receiver.getAsMention())
        );
    }

    @Override
    public String getName() {
        return "donate";
    }

    @Override
    public String getDescription() {
        return "Donate your beanCoins to another user!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CAFE;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to donate to.", true),
                new OptionData(OptionType.NUMBER, "amount", "The amount of beanCoins you want to donate.", true)
        };
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[0];
    }

    @Override
    public boolean isEphemeral() {
        return true;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public boolean allowDM() {
        return false;
    }

}
