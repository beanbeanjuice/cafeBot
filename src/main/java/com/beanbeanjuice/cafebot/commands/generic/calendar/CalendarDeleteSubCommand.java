package com.beanbeanjuice.cafebot.commands.generic.calendar;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.OwnerType;
import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.api.wrapper.type.calendar.Calendar;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class CalendarDeleteSubCommand extends Command implements ISubCommand {

    public CalendarDeleteSubCommand(CafeBot bot) {
        super(bot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String calendarId = event.getOption("id").getAsString().split("ID: ")[1];

        bot.getCafeAPI().getCalendarApi().getCalendar(calendarId).thenAccept(calendar -> {
            if (calendar.getOwnerType() == OwnerType.GUILD && event.isFromGuild() && event.getMember() != null && !event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                event.getHook().sendMessageEmbeds(Helper.errorEmbed("No Permission", "What are you doing back here?? Get **out**!")).queue();
                return;
            }

            String callerId = (calendar.getOwnerType() == OwnerType.GUILD && event.isFromGuild()) ? event.getGuild().getId() : event.getUser().getId();

            bot.getCafeAPI().getCalendarApi().deleteCalendar(calendarId, callerId).thenAccept(result -> {
                event.getHook().sendMessageEmbeds(Helper.successEmbed("Calendar Deleted!", "We won't see that pesky calendar any longer!")).queue();
            }).exceptionally((ex) -> {
                handleError(ex, event);
                throw new CompletionException(ex.getCause());
            });
        }).exceptionally((ex) -> {
            handleError(ex, event);
            throw new CompletionException(ex.getCause());
        });
    }

    private void handleError(Throwable ex, SlashCommandInteractionEvent event) {
        if (ex.getCause() instanceof ApiRequestException apiRequestException) {
            JsonNode errorNode = apiRequestException.getBody().get("error");

            if (errorNode.has("callerId")) {
                event.getHook().sendMessageEmbeds(Helper.errorEmbed("You're joking right?", "Oh well.. I thought you were smarter than that... since you tried to use an exploit I made sure to talk to my boss! <:cafeBot_angry:1171726164092518441>")).queue();
                return;
            }
        }

        event.getHook().sendMessageEmbeds(Helper.errorEmbed("Error", "Could not delete the calendar!")).queue();
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "Delete a calendar!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "id", "The ID of the calendar you want to delete!", true, true),
        };
    }

    @Override
    public CompletableFuture<HashMap<String, List<String>>> getAutoComplete(CommandAutoCompleteInteractionEvent event) {
        HashMap<String, List<String>> autoCompleteMap = new HashMap<>();

        var userCalendarFutures = bot.getCafeAPI().getCalendarApi().getUserCalendars(event.getUser().getId());
        var guildCalenderFutures = (!event.isFromGuild()) ? CompletableFuture.completedFuture(new ArrayList<Calendar>()) : bot.getCafeAPI().getCalendarApi().getGuildCalendars(event.getGuild().getId());

        return userCalendarFutures.thenCombine(guildCalenderFutures, (userCalendars, guildCalendars) -> {
            userCalendars.addAll(guildCalendars);
            autoCompleteMap.put("id", userCalendars.stream().map((calendar) -> String.format("%s - ID: %s", calendar.getName(), calendar.getId())).toList());
            return autoCompleteMap;
        });
    }

}
