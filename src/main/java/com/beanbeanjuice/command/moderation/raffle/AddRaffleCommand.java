package com.beanbeanjuice.command.moderation.raffle;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.handler.guild.GuildHandler;
import com.beanbeanjuice.utility.helper.Helper;
import com.beanbeanjuice.utility.logging.LogLevel;
import com.beanbeanjuice.utility.section.moderation.raffle.Raffle;
import com.beanbeanjuice.utility.section.moderation.raffle.RaffleHandler;
import io.github.beanbeanjuice.cafeapi.exception.CafeException;
import io.github.beanbeanjuice.cafeapi.generic.CafeGeneric;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
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
public class AddRaffleCommand implements ICommand {

    private final int MAX_RAFFLES = 3;

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        // Check if the amount of raffles the server has is more than 3
        try {
            if (Bot.getCafeAPI().RAFFLE.getGuildRaffles(event.getGuild().getId()).size() >= MAX_RAFFLES) {
                event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                        "Too Many Raffles",
                        "Your guild currently has 3 raffles. This is a limitation due to server costs."
                )).queue();
                return;
            }
        } catch (CafeException e) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Error Adding Raffle",
                    "There was an error checking how many raffles this Discord server has. Please try again later."
            )).queue();
            Bot.getLogger().log(this.getClass(), LogLevel.ERROR, "Error Checking Raffle Amount: " + e.getMessage(), e);
            return;
        }

        // Checking if the raffle channel still exists.
        TextChannel raffleChannel = GuildHandler.getCustomGuild(event.getGuild()).getRaffleChannel();

        if (raffleChannel == null) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Raffle Channel Not Set",
                    "You currently do not have a raffle channel set."
            )).queue();
            return;
        }

        // Required items.
        Integer minutes = event.getOption("time").getAsInt();
        Integer winnerAmount = event.getOption("winner_amount").getAsInt();
        String title = event.getOption("title").getAsString();
        String description = event.getOption("description").getAsString();

        event.getHook().sendMessageEmbeds(Helper.successEmbed(
                "Creating Raffle",
                "Currently creating the raffle..."
        )).queue();

        // Finally send the message.
        if (event.getOption("message") != null) {
            raffleChannel.sendMessage(event.getOption("message").getAsString()).setEmbeds(creatingRaffle()).queue(message -> {
                editMessage(message, event, title, description, minutes, winnerAmount);
            });
        } else {
            raffleChannel.sendMessageEmbeds(creatingRaffle()).queue(message -> {
                editMessage(message, event, title, description, minutes, winnerAmount);
            });
        }
    }

    private void editMessage(Message message, SlashCommandInteractionEvent event,
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
                "A raffle has been successfully created! Check the " + message.getTextChannel().getAsMention() + " channel."
        )).queue();
    }

    @NotNull
    private MessageEmbed raffleEmbed(@NotNull String title, @NotNull String description,
                                     @NotNull Integer minutes, @NotNull Integer winnerAmount,
                                     @NotNull SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(title)
                .addField("Raffle Details", description, false)
                .addField("Winner Amount", winnerAmount.toString(), false);

        // Author
        if (event.getOption("author") != null)
            embedBuilder.setAuthor(event.getOption("author").getAsString());

        // Thumbnail
        if (event.getOption("thumbnail") != null && event.getOption("thumbnail").getAsAttachment().isImage())
            embedBuilder.setThumbnail(event.getOption("thumbnail").getAsAttachment().getUrl());

        // Image
        if (event.getOption("image") != null && event.getOption("image").getAsAttachment().isImage())
            embedBuilder.setImage(event.getOption("image").getAsAttachment().getUrl());

        // Color
        if (event.getOption("color") != null) {
            try {
                embedBuilder.setColor(Color.decode(event.getOption("color").getAsString()));
            } catch (NumberFormatException ignored) {}
        }

        if (minutes == 1) {
            embedBuilder.setFooter("This raffle will end in " + minutes + " minute from when the message was posted.");
        } else {
            embedBuilder.setFooter("This raffle will end in " + minutes + " minutes from when the message was posted.");
        }
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
    public String getDescription() {
        return "Create a raffle for your server!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/add-raffle`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "time", "The amount of time this raffle will last, in minutes.", true, false)
                .setMinValue(1));
        options.add(new OptionData(OptionType.INTEGER, "winner_amount", "The amount of winners for this raffle.", true, false)
                .setMinValue(1));
        options.add(new OptionData(OptionType.STRING, "title", "Title for the raffle.", true, false));
        options.add(new OptionData(OptionType.STRING, "description", "The message that goes INSIDE the raffle.", true, false));
        options.add(new OptionData(OptionType.STRING, "author", "The author of the raffle.", false, false));
        options.add(new OptionData(OptionType.STRING, "message", "The message that goes outside of the raffle.", false, false));
        options.add(new OptionData(OptionType.ATTACHMENT, "thumbnail", "A thumbnail url to add to the raffle.", false, false));
        options.add(new OptionData(OptionType.ATTACHMENT, "image", "An image url to add to the raffle.", false, false));
        options.add(new OptionData(OptionType.STRING, "color", "Color hex code. Example: #FFC0CB", false, false));
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.MODERATION;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return false;
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
