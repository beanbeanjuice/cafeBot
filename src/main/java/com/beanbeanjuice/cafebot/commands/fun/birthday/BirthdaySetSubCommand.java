package com.beanbeanjuice.cafebot.commands.fun.birthday;

import com.beanbeanjuice.cafebot.api.wrapper.type.Birthday;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
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

public class BirthdaySetSubCommand extends Command implements ISubCommand {

    public BirthdaySetSubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        int month = event.getOption("month").getAsInt();
        int day = event.getOption("day").getAsInt();
        Optional<Integer> yearOptional = Optional.ofNullable(event.getOption("year")).map(OptionMapping::getAsInt);
        ZoneId zoneId = ZoneId.of(event.getOption("timezone").getAsString());

        Birthday birthday = new Birthday(userId, yearOptional.orElse(2000), month, day, zoneId);

        bot.getCafeAPI().getBirthdayApi().setBirthday(userId, birthday)
                .thenAccept((newBirthday) -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "🎂 Birthday Set",
                            String.format(
                                    "You have successfully set your birthday to **%s %d** (%s).",
                                    Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                                    day,
                                    zoneId.getId()
                            )
                    )).queue();
                })
                .exceptionally((ex) -> {
                    event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                            "Error Setting Birthday",
                            "I... I don't know what happened... the computer's not letting me put your birthday in!"
                    )).queue();

                    bot.getLogger().log(this.getClass(), LogLevel.WARN, "Error Setting Birthday: " + ex.getMessage(), true, true);
                    return null;
                });
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Edit your birthday!";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.INTEGER, "month", "The month you were born!", true)
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

                new OptionData(OptionType.INTEGER, "day", "The day you were born in the specified month!", true)
                        .setRequiredRange(1, 31),

                new OptionData(OptionType.STRING, "timezone", "Start typing to see available options!", true, true),

                new OptionData(OptionType.INTEGER, "year", "The year you were born in!", false)
        };
    }

    @Override
    public CompletableFuture<HashMap<String, ArrayList<String>>> getAutoComplete(CommandAutoCompleteInteractionEvent event) {
        HashMap<String, ArrayList<String>> autoCompleteMap = new HashMap<>();
        autoCompleteMap.put("timezone", new ArrayList<>());

        for (String timezone : TimeZone.getAvailableIDs())
            autoCompleteMap.get("timezone").add(timezone);

        return CompletableFuture.completedFuture(autoCompleteMap);
    }

}
