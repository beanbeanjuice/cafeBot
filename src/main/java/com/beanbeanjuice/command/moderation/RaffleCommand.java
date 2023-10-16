package com.beanbeanjuice.command.moderation;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.CommandType;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.logging.LogLevel;
import com.beanbeanjuice.utility.section.moderation.raffle.Raffle;
import com.beanbeanjuice.utility.section.moderation.raffle.RaffleHandler;
import com.beanbeanjuice.cafeapi.exception.api.CafeException;
import com.beanbeanjuice.cafeapi.generic.CafeGeneric;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * An {@link ICommand} to create a {@link com.beanbeanjuice.utility.section.moderation.raffle.Raffle Raffle}.
 *
 * @author beanbeanjuice
 */
public class RaffleCommand implements ICommand {

    private final int MAX_RAFFLES = 3;

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        // Check if the amount of raffles the server has is more than 3
        if (Bot.getCafeAPI().RAFFLE.getGuildRaffles(event.getGuild().getId()).size() >= MAX_RAFFLES) {
            event.replyEmbeds(Helper.errorEmbed(
                    "Too Many Raffles",
                    "Your guild currently has 3 raffles. This is a limitation due to server costs."
            )).setEphemeral(true).queue();
            return;
        }

        // Checking if the raffle channel still exists.
        if (GuildHandler.getCustomGuild(event.getGuild()).getRaffleChannel() == null) {
            event.replyEmbeds(Helper.errorEmbed(
                    "Raffle Channel Not Set",
                    "You currently do not have a raffle channel set. You must have a dedicated raffle channel. " +
                            "Do `/raffle-channel set`."
            )).setEphemeral(true).queue();
            return;
        }

        Modal.Builder modalBuilder = Modal.create("raffle-modal", "Create a Raffle");
        getModalOptions().forEach((option) -> modalBuilder.addComponents(ActionRow.of(option)));
        event.replyModal(modalBuilder.build()).queue();
    }

    @Override
    public void handleModal(@NotNull ModalInteractionEvent event) {
        TextChannel raffleChannel = GuildHandler.getCustomGuild(event.getGuild()).getRaffleChannel();

        // Required items.
        String title = event.getValue("raffle-title").getAsString();
        String description = event.getValue("raffle-description").getAsString();
        int winnerAmount = Helper.stringToPositiveInteger(event.getValue("raffle-winners").getAsString());
        int minutes = Helper.stringToPositiveInteger(event.getValue("raffle-time").getAsString());

        // Checking integer values.
        if (winnerAmount < 1) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Must Have At Least 1 Winner",
                    "Raffles must have at least one winner."
            )).queue();
            return;
        }

        if (minutes < 1) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Must Be At Least 1 Minute",
                    "Raffles must last at least 1 minute."
            )).queue();
            return;
        }

        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Creating Raffle",
                "Currently creating the raffle..."
        )).queue();

        // Finally send the message.
        if (event.getValue("raffle-message") != null) {
            raffleChannel.sendMessage(event.getValue("raffle-message").getAsString()).setEmbeds(creatingRaffle()).queue(message -> {
                editMessage(message, event, title, description, minutes, winnerAmount);
            });
        } else {
            raffleChannel.sendMessageEmbeds(creatingRaffle()).queue(message -> {
                editMessage(message, event, title, description, minutes, winnerAmount);
            });
        }
    }

    private void editMessage(Message message, ModalInteractionEvent event,
                             String title, String description, Integer minutes, Integer winnerAmount) {

        // Converts the ending time to UTC time.
        Timestamp endingTime = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis() + (minutes*60000)).toString());

        Raffle raffle = new Raffle(message.getId(), endingTime, winnerAmount);

        if (!RaffleHandler.addRaffle(event.getGuild().getId(), raffle)) {
            message.delete().queue();
            event.getHook().editOriginalEmbeds(Helper.sqlServerError()).queue();
            return;
        }

        message.editMessageEmbeds(raffleEmbed(title, description, minutes, winnerAmount, event)).queue();
        message.addReaction(Emoji.fromUnicode("U+2705")).queue();
        event.getHook().editOriginalEmbeds(Helper.successEmbed(
                "Raffle Created",
                "A raffle has been successfully created! Check the " + message.getChannel().asTextChannel().getAsMention() + " channel."
        )).queue();
    }

    @NotNull
    private MessageEmbed raffleEmbed(@NotNull String title, @NotNull String description,
                                     @NotNull Integer minutes, @NotNull Integer winnerAmount,
                                     @NotNull ModalInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(title)
                .addField("Raffle Details", description, false)
                .addField("Winner Amount", winnerAmount.toString(), false)
                .setColor(Helper.getRandomColor());

        if (minutes == 1)
            embedBuilder.setFooter("This raffle will end in " + minutes + " minute from when the message was posted.");
        else
            embedBuilder.setFooter("This raffle will end in " + minutes + " minutes from when the message was posted.");
        
        return embedBuilder.build();
    }

    private MessageEmbed creatingRaffle() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Creating Raffle...");
        embedBuilder.setDescription("Please wait while the raffle is created.");
        embedBuilder.setColor(Color.orange);
        return embedBuilder.build();
    }

    @NotNull
    @Override
    public CommandType getType() {
        return CommandType.MODAL;
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Create a raffle for your server!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/add-raffle`";
    }

    private ArrayList<TextInput> getModalOptions() {
        ArrayList<TextInput> options = new ArrayList<>();

        options.add(
                TextInput.create("raffle-title", "Title", TextInputStyle.SHORT)
                        .setPlaceholder("The title of the raffle.")
                        .build()
        );

        options.add(
                TextInput.create("raffle-description", "Description", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("The description of the raffle.")
                        .build()
        );

        options.add(
                TextInput.create("raffle-winners", "Winner Amount (Minimum of 1)", TextInputStyle.SHORT)
                        .setPlaceholder("The amount of winners for this raffle.")
                        .build()
        );

        options.add(
                TextInput.create("raffle-time", "Raffle Time (Minimum of 1 Minute)", TextInputStyle.SHORT)
                        .setPlaceholder("The amount of time the raffle should run. (In Minutes)")
                        .build()
        );

        options.add(
                TextInput.create("raffle-message", "Message", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("An additional message that should be sent alongside the raffle.")
                        .setRequired(false)
                        .build()
        );

        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.MODERATION;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

    @Nullable
    @Override
    public ArrayList<Permission> getPermissions() {
        ArrayList<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MANAGE_SERVER);
        return permissions;
    }
}
