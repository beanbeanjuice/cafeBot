package com.beanbeanjuice.cafebot.commands.generic.calendar;

import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
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
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        User user = Optional.ofNullable(event.getOption("user")).map(OptionMapping::getAsUser).orElse(event.getUser());

        bot.getCafeAPI().getCalendarApi().getUserCalendars(user.getId()).thenAccept(calendars -> {
            String title = ctx.getUserI18n().getString("command.calendar.subcommands.list.success.title");
            String footer = ctx.getUserI18n().getString("command.calendar.subcommands.list.success.footer");
            String empty = ctx.getUserI18n().getString("command.calendar.subcommands.list.success.empty");

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(title);
            eb.setFooter(footer);
            eb.setColor(Helper.getRandomColor());
            eb.setDescription(calendars.stream().map((calendar) -> String.format("**%s** (ID: %s)", calendar.getName(), calendar.getId())).collect(Collectors.joining("\n")));

            if (calendars.isEmpty()) eb.appendDescription(empty);

            event.getHook().sendMessageEmbeds(eb.build()).queue();
        }).exceptionally((ex) -> {
            event.getHook().sendMessageEmbeds(Helper.defaultErrorEmbed(ctx.getUserI18n())).queue();
            throw new CompletionException(ex.getCause());
        });
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescriptionPath() {
        return "command.calendar.subcommands.list.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.USER, "user", "command.calendar.subcommands.list.arguments.user.description", false)
        };
    }

}
