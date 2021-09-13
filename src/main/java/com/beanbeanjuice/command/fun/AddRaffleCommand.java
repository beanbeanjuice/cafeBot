package com.beanbeanjuice.command.fun;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.sections.fun.raffle.Raffle;
import io.github.beanbeanjuice.cafeapi.exception.CafeException;
import io.github.beanbeanjuice.cafeapi.generic.CafeGeneric;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A command used for {@link com.beanbeanjuice.utility.sections.fun.raffle.Raffle Raffle}s.
 *
 * @author beanbeanjuice
 */
public class AddRaffleCommand implements ICommand {

    private final int MAX_RAFFLES = 3;

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        // Checking if the user is a moderator.
        if (!CafeBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        // Parsing the arguments.
        HashMap<String, String> parsedMap = CafeBot.getGeneralHelper().createCommandTermMap(getCommandTerms(), args);

        // Checking the title of the raffle.
        String title = parsedMap.get("title");
        if (title == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Missing Argument",
                    "You are missing the `title` argument."
            )).queue();
            return;
        }

        // Checking the description of the raffle.
        String description = parsedMap.get("description");
        if (description == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Missing Argument",
                    "You are missing the `description` argument."
            )).queue();
            return;
        }

        // Checking the amount of minutes.
        Integer minutes;
        try {
            minutes = Integer.parseInt(parsedMap.get("time"));
        } catch (NumberFormatException e) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Invalid Time Amount",
                    "The amount you have entered for time is not valid. Please enter a whole number."
            )).queue();
            return;
        }

        // Checking the amount of winners.
        Integer winnerAmount;
        try {
            winnerAmount = Integer.parseInt(parsedMap.get("winners"));
        } catch (NumberFormatException e) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Invalid Time Amount",
                    "The amount you have entered for winners is not valid. Please enter a whole number."
            )).queue();
            return;
        }

        // Check if the amount of raffles the server has is more than 3
        try {
            if (CafeBot.getCafeAPI().raffles().getGuildRaffles(event.getGuild().getId()).size()+1 > MAX_RAFFLES) {
                event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                        "Too Many Raffles",
                        "Your guild currently has 3 raffles. This is a limitation due to server costs."
                )).queue();
                return;
            }
        } catch (CafeException e) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Error Adding Raffle",
                    "There was an error checking how many raffles this Discord server has. Please try again later."
            )).queue();
            CafeBot.getLogManager().log(this.getClass(), LogLevel.ERROR, "Error Checking Raffle Amount: " + e.getMessage(), e);
            return;
        }

        TextChannel raffleChannel = ctx.getCustomGuild().getRaffleChannel();

        if (raffleChannel == null) {
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().errorEmbed(
                    "Raffle Channel Not Set",
                    "You currently do not have a raffle channel set."
            )).queue();
            return;
        }

        if (parsedMap.get("message") != null) {
            raffleChannel.sendMessage(parsedMap.get("message")).setEmbeds(creatingRaffle()).queue(message -> {
                editMessage(message, event, title, description, minutes, winnerAmount);
            });
        } else {
            raffleChannel.sendMessageEmbeds(creatingRaffle()).queue(message -> {
                editMessage(message, event, title, description, minutes, winnerAmount);
            });
        }
    }

    /**
     * Edits the specified {@link Message}.
     * @param message The specified {@link Message}.
     * @param event The {@link GuildMessageReceivedEvent event} that triggered the {@link Message}.
     * @param title The {@link String title} of the {@link Raffle}.
     * @param description The {@link String description} of the {@link Raffle}.
     * @param minutes The amount of {@link Integer minutes} for the {@link Raffle}.
     * @param winnerAmount The amount of {@link Integer winners} for the {@link Raffle}.
     */
    private void editMessage(Message message, GuildMessageReceivedEvent event,
                             String title, String description, Integer minutes, Integer winnerAmount) {

        // Converts the ending time to UTC time.
        Timestamp endingTime = CafeGeneric.parseTimestamp(new Timestamp(System.currentTimeMillis() + (minutes*60000)).toString());

        Raffle raffle = new Raffle(message.getId(), endingTime, winnerAmount);

        if (!CafeBot.getRaffleHandler().addRaffle(event.getGuild().getId(), raffle)) {
            message.delete().queue();
            event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().sqlServerError()).queue();
            return;
        }

        message.editMessageEmbeds(raffleEmbed(title, description, minutes, winnerAmount)).queue();
        message.addReaction("U+2705").queue();
        event.getChannel().sendMessageEmbeds(CafeBot.getGeneralHelper().successEmbed(
                "Raffle Created",
                "A raffle has been successfully created! Check the " + message.getTextChannel().getAsMention() + " channel."
        )).queue();
    }

    @NotNull
    private ArrayList<String> getCommandTerms() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("title");
        arrayList.add("description");
        arrayList.add("message");
        arrayList.add("winners");
        arrayList.add("time");
        return arrayList;
    }

    @NotNull
    private MessageEmbed raffleEmbed(@NotNull String title, @NotNull String description,
                                     @NotNull Integer minutes, @NotNull Integer winnerAmount) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.addField("Raffle Details", description, false);
        embedBuilder.addField("Winner Amount", winnerAmount.toString(), false);
        embedBuilder.setColor(CafeBot.getGeneralHelper().getRandomColor());
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

    @Override
    public String getName() {
        return "add-raffle";
    }

    @Override
    public ArrayList<String> getAliases() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("addraffle");
        arrayList.add("raffle");
        arrayList.add("create-raffle");
        arrayList.add("createraffle");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Create a raffle for your server!";
    }

    @Override
    public String exampleUsage(String prefix) {
        return "`" + prefix + "addraffle message:@fakeRole, check this out! title:RTX 3080 Giveaway description:Do you want to win an rtx 3080? winners:2 time:30`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.SENTENCE, "Raffle Arguments", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.FUN;
    }
}
