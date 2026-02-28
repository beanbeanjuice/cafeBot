package com.beanbeanjuice.cafebot.commands.generic.calendar;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.api.wrapper.type.calendar.Calendar;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.handlers.calendar.CalendarHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class CalendarGetSubCommand extends Command implements ISubCommand {

    public CalendarGetSubCommand(CafeBot bot) {
        super(bot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String[] split = event.getOption("id").getAsString().split("ID: ");
        String calendarId = (split.length == 2) ? split[1] : split[0];
        ZoneId zoneId = TimeZone.getTimeZone(event.getOption("timezone").getAsString()).toZoneId();

        bot.getCafeAPI().getCalendarApi().getCalendar(calendarId).thenAccept(calendar -> {
            String message = CalendarHandler.getCalendarMessage(calendar.getUrl(), zoneId);
            event.getHook().sendMessage(message).queue();
        }).exceptionally((ex) -> {
            handleError(ex, event);
            throw new CompletionException(ex.getCause());
        });
    }

    private void handleError(Throwable ex, SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(Helper.errorEmbed("Error Getting Calendar", "I... couldn't find the calendar... is this an error??")).queue();
        bot.getLogger().log(this.getClass(), LogLevel.WARN, ex.getMessage(), true, false, ex);
    }

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescription() {
        return "Get your calendars!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "id", "The ID of the calendar you want to view!", true, true),
                new OptionData(OptionType.STRING, "timezone", "The timezone you want the calendar in.", true, true)
        };
    }

    @Override
    public CompletableFuture<HashMap<String, List<String>>> getAutoComplete(CommandAutoCompleteInteractionEvent event) {
        HashMap<String, List<String>> autoCompleteMap = new HashMap<>();
        autoCompleteMap.put("timezone", new ArrayList<>());

        for (String timezone : TimeZone.getAvailableIDs()) autoCompleteMap.get("timezone").add(timezone);

        var userCalendarFutures = bot.getCafeAPI().getCalendarApi().getUserCalendars(event.getUser().getId());
        var guildCalenderFutures = (!event.isFromGuild()) ? CompletableFuture.completedFuture(new ArrayList<Calendar>()) : bot.getCafeAPI().getCalendarApi().getGuildCalendars(event.getGuild().getId());

        return userCalendarFutures.thenCombine(guildCalenderFutures, (userCalendars, guildCalendars) -> {
            userCalendars.addAll(guildCalendars);
            autoCompleteMap.put("id", userCalendars.stream().map((calendar) -> String.format("%s - ID: %s", calendar.getName(), calendar.getId())).toList());
            return autoCompleteMap;
        });
    }

}
