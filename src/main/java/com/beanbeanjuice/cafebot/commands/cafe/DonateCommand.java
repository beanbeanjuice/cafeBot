package com.beanbeanjuice.cafebot.commands.cafe;

import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandCategory;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ICommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.EmbedBuilder;
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
import java.util.ResourceBundle;
import java.util.concurrent.CompletionException;

public class DonateCommand extends Command implements ICommand {

    public DonateCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        Optional<OptionMapping> receiverMapping = Optional.ofNullable(event.getOption("user"));
        User receiver = receiverMapping.map(OptionMapping::getAsUser).orElseThrow();  // Shouldn't be null.
        User sender = event.getUser();

        if (receiver == sender) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    ctx.getUserI18n().getString("command.donate.embed.self.title"),
                    ctx.getUserI18n().getString("command.donate.embed.self.description")
            )).queue();
            return;
        }

        Optional<OptionMapping> amountMapping = Optional.ofNullable(event.getOption("amount"));
        double amount = amountMapping.map(OptionMapping::getAsDouble).orElseThrow();  // Shouldn't be null.

        bot.getCafeAPI().getDonationApi().createDonation(sender.getId(), receiver.getId(), amount)
                .thenAccept((result) -> {
                    sendSuccessToSender(event, receiver);
                    sendDonationEmbed(event, sender, receiver, amount, ctx.getGuildI18n());
                })
                .exceptionally((e) -> {
                    handleError(e, event, ctx, sender, receiver, amount);

                    throw new CompletionException(e.getCause());
                });
    }

    private void handleError(Throwable e, SlashCommandInteractionEvent event, CommandContext ctx, User sender, User receiver, double amount) {
        if (e.getCause() instanceof ApiRequestException requestException) {
            JsonNode error = requestException.getBody().get("error");

            if (error.get("from") != null && error.get("from").get(0).asString().equals("Insufficient balance")) {
                sendNotEnoughCoinsEmbed(event, sender.getId(), amount, ctx.getUserI18n());
                return;
            }

            if (error.get("to") != null && error.get("to").get(0).asString().equals("That user can only be donated to once per hour")) {
                sendNeedToWaitEmbed(event, receiver.getId(), ctx.getUserI18n());
                return;
            }
        }

        event.getHook().sendMessageEmbeds(Helper.uncaughtErrorEmbed(
                ctx.getUserI18n(),
                e.getMessage()
        )).queue();

        bot.getLogger().log(DonateCommand.class, LogLevel.WARN, "There was an error donating to a user: " + e.getMessage(), e.getCause());
    }

    private void sendNotEnoughCoinsEmbed(SlashCommandInteractionEvent event, String userId, double amount, ResourceBundle i18n) {
        bot.getCafeAPI().getUserApi().getUser(userId).thenAccept((user) -> {
            event.getHook().sendMessageEmbeds(notEnoughCoinsEmbed(user.getBalance(), amount, i18n)).queue();
        });
    }

    private void sendNeedToWaitEmbed(SlashCommandInteractionEvent event, String userId, ResourceBundle i18n) {
        bot.getCafeAPI().getUserApi().getUser(userId).thenAccept((user) -> {
            event.getHook().sendMessageEmbeds(needToWaitEmbed(user.getLastDonationTime().orElse(Instant.now()), i18n)).queue();
        });
    }

    private void sendSuccessToSender(SlashCommandInteractionEvent event, User receiver) {
        event.getHook().sendMessageEmbeds(successEmbed(receiver)).queue();
    }

    private void sendDonationEmbed(SlashCommandInteractionEvent event, User sender, User receiver, double amount, ResourceBundle i18n) {
        event.getChannel().sendMessageEmbeds(donationEmbed(sender, receiver, amount, i18n)).mention(receiver).queue();
    }

    private MessageEmbed notEnoughCoinsEmbed(double balance, double amount, ResourceBundle i18n) {
        String description = i18n.getString("command.donate.embed.balance.description")
                .replace("{balance}", String.format("%.2f", balance))
                .replace("{payment}", String.format("%.2f", amount));

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(i18n.getString("command.donate.embed.balance.title"));
        embedBuilder.setDescription(description);
        embedBuilder.setFooter(i18n.getString("command.donate.embed.balance.footer"));
        embedBuilder.setColor(Helper.getRandomColor());

        return embedBuilder.build();
    }

    private MessageEmbed needToWaitEmbed(Instant lastDonationTime, ResourceBundle i18n) {
        String description = i18n.getString("command.donate.embed.cooldown.description")
                .replace("{timestamp}", String.valueOf(lastDonationTime.getEpochSecond()));

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(i18n.getString("command.donate.embed.cooldown.title"));
        embedBuilder.setDescription(description);
        embedBuilder.setFooter(i18n.getString("command.donate.embed.cooldown.footer"));
        embedBuilder.setColor(Helper.getRandomColor());

        return embedBuilder.build();
    }

    private MessageEmbed successEmbed(User receiver) {
        return Helper.successEmbed(
                "Donation Received",
                String.format("%s has received your donation!", receiver.getAsMention())
        );
    }

    private MessageEmbed donationEmbed(User sender, User receiver, double amount, ResourceBundle i18n) {
        String description = i18n.getString("command.donate.embed.donation.description")
                .replace("{donator}", sender.getAsMention())
                .replace("{donatee}", receiver.getAsMention())
                .replace("{amount}", String.format("%.2f", amount));

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(i18n.getString("command.donate.embed.donation.title"));
        embedBuilder.setDescription(description);
        embedBuilder.setFooter(i18n.getString("command.donate.embed.donation.footer"));
        embedBuilder.setColor(Helper.getRandomColor());

        return embedBuilder.build();
    }

    @Override
    public String getName() {
        return "donate";
    }

    @Override
    public String getDescriptionPath() {
        return "command.donate.description";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CAFE;
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "command.donate.arguments.user.description", true),
                new OptionData(OptionType.NUMBER, "amount", "command.donate.arguments.amount.description", true)
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
