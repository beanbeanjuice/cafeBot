package com.beanbeanjuice.cafebot.commands.generic.calendar;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.OwnerType;
import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.api.wrapper.type.calendar.PartialCalendar;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.handlers.calendar.CalendarHandler;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import tools.jackson.databind.JsonNode;

import java.util.concurrent.CompletionException;

public class CalendarAddSubCommand extends Command implements ISubCommand {

    public CalendarAddSubCommand(CafeBot bot) {
        super(bot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        OwnerType type = OwnerType.valueOf(event.getOption("type").getAsString());
        String name = event.getOption("name").getAsString();
        String url = event.getOption("url").getAsString();

        if (type == OwnerType.GUILD && !event.isFromGuild()) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Invalid Command!",
                    "In order to set a server calendar, you need to use this command in a Discord server!"
            )).queue();
            return;
        }

        if (event.isFromGuild() && event.getMember() != null && !event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed("No Permission", "What are you doing back here?? Get **out**!")).queue();
            return;
        }

        String ownerId = (type == OwnerType.GUILD) ? event.getGuild().getId() : event.getUser().getId();
        PartialCalendar partialCalendar = new PartialCalendar(type, ownerId, name, url);

        bot.getCafeAPI().getCalendarApi().createCalendar(partialCalendar).thenAccept((calendar) -> {
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Calendar Added!",
                    "Successfully added your calendar! Use `/calender get` to preview your calendar!"
            )).queue();
        }).exceptionally((ex) -> {
            handleError(ex, event);
            throw new CompletionException(ex.getCause());
        });
    }

    private void handleError(Throwable ex, SlashCommandInteractionEvent event) {
        if (ex.getCause() instanceof ApiRequestException apiRequestException) {
            JsonNode errorBody = apiRequestException.getBody().get("error");

            if (errorBody.has("calendars")) {
                event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                        "Too Many Calendars!",
                        "You have added too many calendars... You can only have 3 active ones!"
                )).queue();
                return;
            }

            if (errorBody.has("url")) {
                event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                        "Invalid URL",
                        "You must use a *valid* calendar URL!"
                )).queue();
                return;
            }
        }

        event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                "Error Adding Calendar",
                "I'm sorry... I don't know *what* went wrong..."
        )).queue();
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "Add a calendar!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "type", "The type of calendar you want to add.", true)
                        .addChoice("User (The calendar belongs to you)", "DISCORD_USER")
                        .addChoice("Server (The calendar belongs to the server)", "GUILD"),
                new OptionData(OptionType.STRING, "name", "The calendar name!", true),
                new OptionData(OptionType.STRING, "url", "The calendar url! Make sure it ends in \".ics\"!", true),
        };
    }

}
