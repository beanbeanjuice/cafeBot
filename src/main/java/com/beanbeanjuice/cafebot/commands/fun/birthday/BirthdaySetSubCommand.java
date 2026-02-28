package com.beanbeanjuice.cafebot.commands.fun.birthday;

import com.beanbeanjuice.cafebot.api.wrapper.type.Birthday;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.CommandContext;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.logging.LogLevel;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class BirthdaySetSubCommand extends Command implements ISubCommand {

    public BirthdaySetSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, CommandContext ctx) {
        String userId = event.getUser().getId();
        int month = event.getOption("month").getAsInt();
        int day = event.getOption("day").getAsInt();
        Optional<Integer> yearOptional = Optional.ofNullable(event.getOption("year")).map(OptionMapping::getAsInt);
        ZoneId zoneId = TimeZone.getTimeZone(event.getOption("timezone").getAsString()).toZoneId();

        Birthday birthday = new Birthday(userId, yearOptional.orElse(2000), month, day, zoneId);

        bot.getCafeAPI().getBirthdayApi().setBirthday(userId, birthday)
                .thenAccept((newBirthday) -> {
                    String description = ctx.getUserI18n().getString("command.birthday.subcommand.set.embed.success.description")
                            .replace("{month}", Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                            .replace("{day}", String.valueOf(day))
                            .replace("{timezone}", zoneId.getId());
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            ctx.getUserI18n().getString("command.birthday.subcommand.set.embed.success.title"),
                            description
                    )).queue();
                })
                .exceptionally((ex) -> {
                    handleError(ex, event, ctx);
                    throw new CompletionException(ex.getCause());
                });
    }

    private void handleError(Throwable ex, SlashCommandInteractionEvent event, CommandContext ctx) {
        event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                ctx.getUserI18n().getString("command.birthday.subcommand.set.embed.error.title"),
                ctx.getUserI18n().getString("command.birthday.subcommand.set.embed.error.description")
        )).queue();

        bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Setting Birthday: " + ex.getMessage(), true, true);
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescriptionPath() {
        return "command.birthday.subcommand.set.description";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.INTEGER, "month", "command.birthday.subcommand.set.arguments.month.description", true)
                        .addChoice("1 - January", 1)
                        .addChoice("2 - February", 2)
                        .addChoice("3 - March", 3)
                        .addChoice("4 - April", 4)
                        .addChoice("5 - May", 5)
                        .addChoice("6 - June", 6)
                        .addChoice("7 - July", 7)
                        .addChoice("8 - August", 8)
                        .addChoice("9 - September", 9)
                        .addChoice("10 - October", 10)
                        .addChoice("11 - November", 11)
                        .addChoice("12 - December", 12),

                new OptionData(OptionType.INTEGER, "day", "command.birthday.subcommand.set.arguments.day.description", true)
                        .setRequiredRange(1, 31),

                new OptionData(OptionType.STRING, "timezone", "command.birthday.subcommand.set.arguments.timezone.description", true, true),

                new OptionData(OptionType.INTEGER, "year", "command.birthday.subcommand.set.arguments.year.description", false)
        };
    }

    @Override
    public CompletableFuture<HashMap<String, List<String>>> getAutoComplete(CommandAutoCompleteInteractionEvent event) {
        HashMap<String, List<String>> autoCompleteMap = new HashMap<>();
        autoCompleteMap.put("timezone", new ArrayList<>());

        for (String timezone : TimeZone.getAvailableIDs())
            autoCompleteMap.get("timezone").add(timezone);

        return CompletableFuture.completedFuture(autoCompleteMap);
    }

}
