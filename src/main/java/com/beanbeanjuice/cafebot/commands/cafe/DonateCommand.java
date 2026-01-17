package com.beanbeanjuice.cafebot.commands.cafe;

import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
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
import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.Optional;

public class DonateCommand extends Command implements ICommand {

    public DonateCommand(CafeBot cafeBot) {
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

        bot.getCafeAPI().getDonationApi().createDonation(sender.getId(), receiver.getId(), amount)
                .thenAccept((result) -> {
                    sendSuccessToSender(event, receiver);
                    sendDonationEmbed(event, sender, receiver, amount);
                })
                .exceptionally((e) -> {
                    if (e.getCause() instanceof ApiRequestException requestException) {
                        JsonNode error = requestException.getBody();

                        if (error.get("from") != null && error.get("from").get(0).asString().equals("Insufficient balance")) {
                            sendNotEnoughCoinsEmbed(event, sender.getId(), amount);
                            return null;
                        }

                        if (error.get("to") != null && error.get("to").get(0).asString().equals("That user can only be donated to once per hour")) {
                            sendNeedToWaitEmbed(event, receiver.getId());
                            return null;
                        }
                    }

                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Donating",
                            "There was an error donating to the user: " + e.getMessage()
                    )).queue();

                    bot.getLogger().log(DonateCommand.class, LogLevel.WARN, "There was an error donating to a user: " + e.getMessage(), e.getCause());
                    return null;
                });
    }

    private void sendNotEnoughCoinsEmbed(SlashCommandInteractionEvent event, String userId, double amount) {
        bot.getCafeAPI().getUserApi().getUser(userId).thenAccept((user) -> {
            event.getHook().sendMessageEmbeds(notEnoughCoinsEmbed(user.getBalance(), amount)).queue();
        });
    }

    private void sendNeedToWaitEmbed(SlashCommandInteractionEvent event, String userId) {
        bot.getCafeAPI().getUserApi().getUser(userId).thenAccept((user) -> {
            event.getHook().sendMessageEmbeds(needToWaitEmbed(user.getLastDonationTime().orElse(Instant.now()))).queue();
        });
    }

    private void sendSuccessToSender(SlashCommandInteractionEvent event, User receiver) {
        event.getHook().sendMessageEmbeds(successEmbed(receiver)).queue();
    }

    private void sendDonationEmbed(SlashCommandInteractionEvent event, User sender, User receiver, double amount) {
        event.getChannel().sendMessageEmbeds(donationEmbed(sender, receiver, amount)).mention(receiver).queue();
    }

    private MessageEmbed notEnoughCoinsEmbed(double balance, double amount) {
        return Helper.errorEmbed(
                "Not Enough Coins",
                String.format("""
                        Sorry, you don't have enough bC to donate to that person. \
                        You have **%.2f bC**, which is *not* enough to donate **%.2f bC** to that person. \
                        Maybe try working for a bit? You can use `/serve` to work.
                        """, balance, amount)
        );
    }

    private MessageEmbed needToWaitEmbed(Instant lastDonationTime) {
        return Helper.errorEmbed(
                "That User Can't Receive Donations",
                String.format("""
                        That user was last donated to at <t:%s>. You need to wait before they are able to \
                        receive another donation.
                        """, lastDonationTime.getEpochSecond())
        );
    }

    private MessageEmbed successEmbed(User receiver) {
        return Helper.successEmbed(
                "Donation Received",
                String.format("%s has received your donation!", receiver.getAsMention())
        );
    }

    private MessageEmbed donationEmbed(User sender, User receiver, double amount) {
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
