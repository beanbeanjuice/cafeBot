package com.beanbeanjuice.cafebot.commands.generic.calendar;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.OwnerType;
import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.api.wrapper.type.calendar.Calendar;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
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
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        String calendarId = event.getOption("id").getAsString().split("ID: ")[1];

        bot.getCafeAPI().getCalendarApi().getCalendar(calendarId).thenAccept(calendar -> {
            if (calendar.getOwnerType() == OwnerType.GUILD && event.isFromGuild() && event.getMember() != null && !event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                event.getHook().sendMessageEmbeds(Helper.noPermissionEmbed(ctx.getUserI18n())).queue();
                return;
            }

            String callerId = (calendar.getOwnerType() == OwnerType.GUILD && event.isFromGuild()) ? event.getGuild().getId() : event.getUser().getId();

            bot.getCafeAPI().getCalendarApi().deleteCalendar(calendarId, callerId).thenAccept(_ -> {
                String title = ctx.getUserI18n().getString("command.calendar.subcommands.delete.success.title");
                String description = ctx.getUserI18n().getString("command.calendar.subcommands.delete.success.description");

                event.getHook().sendMessageEmbeds(Helper.successEmbed(title, description)).queue();
            }).exceptionally((ex) -> {
                handleError(ex, event, ctx.getUserI18n());
                throw new CompletionException(ex.getCause());
            });
        }).exceptionally((ex) -> {
            handleError(ex, event, ctx.getUserI18n());
            throw new CompletionException(ex.getCause());
        });
    }

    private void handleError(Throwable ex, SlashCommandInteractionEvent event, I18N userBundle) {
        if (ex.getCause() instanceof ApiRequestException apiRequestException) {
            JsonNode errorNode = apiRequestException.getBody().get("error");

            if (errorNode.has("callerId")) {
                String title = userBundle.getString("command.calendar.subcommands.delete.error.exploit.title");
                String description = userBundle.getString("command.calendar.subcommands.delete.error.exploit.description");

                event.getHook().sendMessageEmbeds(Helper.errorEmbed(title, description)).queue();
                return;
            }
        }

        event.getHook().sendMessageEmbeds(Helper.defaultErrorEmbed(userBundle)).queue();
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescriptionPath() {
        return "command.calendar.subcommands.delete.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "id", "command.calendar.subcommands.delete.arguments.id.description", true, true),
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
