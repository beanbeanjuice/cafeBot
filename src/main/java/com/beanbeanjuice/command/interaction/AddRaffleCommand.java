package com.beanbeanjuice.command.interaction;

import com.beanbeanjuice.main.BeanBot;
import com.beanbeanjuice.utility.command.CommandContext;
import com.beanbeanjuice.utility.command.ICommand;
import com.beanbeanjuice.utility.command.usage.Usage;
import com.beanbeanjuice.utility.command.usage.categories.CategoryType;
import com.beanbeanjuice.utility.command.usage.types.CommandType;
import com.beanbeanjuice.utility.raffle.Raffle;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * A command used for {@link com.beanbeanjuice.utility.raffle.Raffle Raffle}s.
 *
 * @author beanbeanjuice
 */
public class AddRaffleCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx, ArrayList<String> args, User user, GuildMessageReceivedEvent event) {

        if (!BeanBot.getGeneralHelper().isModerator(event.getMember(), event.getGuild(), event)) {
            return;
        }

        String title = BeanBot.getGeneralHelper().removeUnderscores(args.get(0));
        String description = BeanBot.getGeneralHelper().removeUnderscores(args.get(1));
        int minutes = Integer.parseInt(args.get(2));
        int winnerAmount = Integer.parseInt(args.get(3));

        // Check if the amount of raffles the server has is more than 3
        if (BeanBot.getRaffleHandler().getRafflesForGuild(event.getGuild()).size()+1 > 3) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                    "Too Many Raffles",
                    "Your guild currently has 3 raffles. This is a limitation due to server costs."
            )).queue();
            return;
        }

        TextChannel raffleChannel = BeanBot.getGuildHandler().getCustomGuild(event.getGuild()).getRaffleChannel();

        if (raffleChannel == null) {
            event.getChannel().sendMessage(BeanBot.getGeneralHelper().errorEmbed(
                    "Raffle Channel Not Set",
                    "You currently do not have a raffle channel set."
            )).queue();
            return;
        }

        raffleChannel.sendMessage(creatingRaffle()).queue(message -> {
            Raffle raffle = new Raffle(event.getGuild().getId(), message.getId(), new Timestamp(System.currentTimeMillis() + (minutes*60000)), winnerAmount);
            if (BeanBot.getRaffleHandler().addRaffle(raffle)) {
                message.editMessage(raffleEmbed(title, description, minutes, winnerAmount)).queue();
                message.addReaction("U+2705").queue();
            } else {
                message.editMessage(BeanBot.getGeneralHelper().sqlServerError()).queue();
            }
        });
    }

    private MessageEmbed raffleEmbed(@NotNull String title, @NotNull String description,
                                     @NotNull Integer minutes, @NotNull Integer winnerAmount) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(title);
        embedBuilder.addField("Raffle Details", description, false);
        embedBuilder.addField("Winner Amount", winnerAmount.toString(), false);
        embedBuilder.setColor(BeanBot.getGeneralHelper().getRandomColor());
        if (minutes == 1) {
            embedBuilder.setFooter("This raffle will end in " + minutes + " minute from when the message was posted.");
        } else {
            embedBuilder.setFooter("This raffle will end in " + minutes + " minutes from when the message was posted.");
        }
        return embedBuilder.build();
    }

    private MessageEmbed creatingRaffle() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Creating Raffle...");
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
        arrayList.add("create-raffle");
        arrayList.add("createraffle");
        return arrayList;
    }

    @Override
    public String getDescription() {
        return "Create a raffle for your server!";
    }

    @Override
    public String exampleUsage() {
        return "`!!addraffle RTX_3080_Giveaway Do_you_want_to_win_an_rtx_3080? 30 2`";
    }

    @Override
    public Usage getUsage() {
        Usage usage = new Usage();
        usage.addUsage(CommandType.TEXT, "Raffle Title", true);
        usage.addUsage(CommandType.TEXT, "Raffle Description", true);
        usage.addUsage(CommandType.NUMBER, "Raffle Timer (In Minutes)", true);
        usage.addUsage(CommandType.NUMBER, "Raffle Winner Amount", true);
        return usage;
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.INTERACTION;
    }
}
