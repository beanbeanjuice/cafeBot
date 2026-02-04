package com.beanbeanjuice.cafebot.commands.generic.calendar;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public class CalendarListSubCommand extends Command implements ISubCommand {

    public CalendarListSubCommand(CafeBot bot) {
        super(bot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        User user = Optional.ofNullable(event.getOption("user")).map(OptionMapping::getAsUser).orElse(event.getUser());

        bot.getCafeAPI().getCalendarApi().getUserCalendars(user.getId()).thenAccept(calendars -> {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Calendar List");
            eb.setFooter("You can view guild calendars by typing /calendar get!");
            eb.setColor(Helper.getRandomColor());
            eb.setDescription(calendars.stream().map((calendar) -> String.format("**%s** (ID: %s)", calendar.getName(), calendar.getId())).collect(Collectors.joining("\n")));

            if (calendars.isEmpty()) eb.appendDescription("No calendars found!");

            event.getHook().sendMessageEmbeds(eb.build()).queue();
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed("Error Getting Calendars", "I.. don't know what went wrong...")).queue();
            throw new CompletionException(ex.getCause());
        });
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "List all of the calendars for a specific user!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "The user you want to see the calendar of!", false)
        };
    }

}
