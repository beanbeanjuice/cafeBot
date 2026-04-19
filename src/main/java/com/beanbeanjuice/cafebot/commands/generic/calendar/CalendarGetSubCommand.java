package com.beanbeanjuice.cafebot.commands.generic.calendar;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.api.wrapper.type.calendar.Calendar;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.handlers.calendar.CalendarHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class CalendarGetSubCommand extends Command implements ISubCommand {

    public CalendarGetSubCommand(CafeBot bot) {
        super(bot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        String[] split = event.getOption("id").getAsString().split("ID: ");
        String calendarId = (split.length == 2) ? split[1] : split[0];
        ZoneId zoneId = TimeZone.getTimeZone(event.getOption("timezone").getAsString()).toZoneId();

        bot.getCafeAPI().getCalendarApi().getCalendar(calendarId).thenAccept(calendar -> {
            String message = CalendarHandler.getCalendarMessage(calendar.getUrl(), zoneId);
            event.getHook().sendMessage(message).queue();
        }).exceptionally((ex) -> {
            handleError(ex, event, ctx.getUserI18n());
            throw new CompletionException(ex.getCause());
        });
    }

    private void handleError(Throwable ex, SlashCommandInteractionEvent event, I18N userBundle) {
        String title = userBundle.getString("command.calendar.subcommands.get.error.title");
        String description = userBundle.getString("command.calendar.subcommands.get.error.description");

        event.getHook().sendMessageEmbeds(Helper.errorEmbed(title, description)).queue();
        bot.getLogger().log(this.getClass(), LogLevel.WARN, ex.getMessage(), true, false, ex);
    }

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescriptionPath() {
        return "command.calendar.subcommands.get.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "id", "command.calendar.subcommands.get.arguments.id.description", true, true),
                new OptionData(OptionType.STRING, "timezone", "command.calendar.subcommands.get.arguments.timezone.description", true, true)
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
