package com.beanbeanjuice.cafebot.commands.generic.calendar;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.api.wrapper.api.enums.OwnerType;
import com.beanbeanjuice.cafebot.api.wrapper.api.exception.ApiRequestException;
import com.beanbeanjuice.cafebot.api.wrapper.type.calendar.PartialCalendar;
import com.beanbeanjuice.cafebot.i18n.I18N;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
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
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        OwnerType type = OwnerType.valueOf(event.getOption("type").getAsString());
        String name = event.getOption("name").getAsString();
        String url = event.getOption("url").getAsString();

        if (type == OwnerType.GUILD && !event.isFromGuild()) {
            String title = ctx.getUserI18n().getString("command.calendar.subcommands.add.error.server.title");
            String description = ctx.getUserI18n().getString("command.calendar.subcommands.add.error.server.description");

            event.getHook().sendMessageEmbeds(Helper.errorEmbed(title, description)).queue();
            return;
        }

        if (type == OwnerType.GUILD && event.isFromGuild() && event.getMember() != null && !event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.getHook().sendMessageEmbeds(Helper.noPermissionEmbed(ctx.getUserI18n())).queue();
            return;
        }

        String ownerId = (type == OwnerType.GUILD) ? event.getGuild().getId() : event.getUser().getId();
        PartialCalendar partialCalendar = new PartialCalendar(type, ownerId, name, url);

        bot.getCafeAPI().getCalendarApi().createCalendar(partialCalendar).thenAccept((calendar) -> {
            String title = ctx.getUserI18n().getString("command.calendar.subcommands.add.success.title");
            String description = ctx.getUserI18n().getString("command.calendar.subcommands.add.success.description");

            event.getHook().sendMessageEmbeds(Helper.successEmbed(title, description)).queue();
        }).exceptionally((ex) -> {
            handleError(ex, event, ctx.getUserI18n());
            throw new CompletionException(ex.getCause());
        });
    }

    private void handleError(Throwable ex, SlashCommandInteractionEvent event, I18N bundle) {
        if (ex.getCause() instanceof ApiRequestException apiRequestException) {
            JsonNode errorBody = apiRequestException.getBody().get("error");

            if (errorBody.has("calendars")) {
                String title = bundle.getString("command.calendar.subcommands.add.error.count.title");
                String description = bundle.getString("command.calendar.subcommands.add.error.count.description");

                event.getHook().sendMessageEmbeds(Helper.errorEmbed(title, description)).queue();
                return;
            }

            if (errorBody.has("url")) {
                String title = bundle.getString("command.calendar.subcommands.add.error.count.title");
                String description = bundle.getString("command.calendar.subcommands.add.error.count.description");

                event.getHook().sendMessageEmbeds(Helper.errorEmbed(title, description)).queue();
                return;
            }
        }

        String title = bundle.getString("command.calendar.subcommands.add.error.generic.title");
        String description = bundle.getString("command.calendar.subcommands.add.error.generic.description");

        event.getHook().sendMessageEmbeds(Helper.errorEmbed(title, description)).queue();

        bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Adding Calendar", ex);
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescriptionPath() {
        return "command.calendar.subcommands.add.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "type", "command.calendar.subcommands.add.arguments.type.description", true)
                        .addChoice("User (The calendar belongs to you)", "DISCORD_USER") // TODO: Handle I18N for choices too.
                        .addChoice("Server (The calendar belongs to the server)", "GUILD"),
                new OptionData(OptionType.STRING, "name", "command.calendar.subcommands.add.arguments.name.description", true),
                new OptionData(OptionType.STRING, "url", "command.calendar.subcommands.add.arguments.url.description", true),
        };
    }

}
