package com.beanbeanjuice.cafebot.commands.fun.birthday;

import com.beanbeanjuice.cafeapi.wrapper.endpoints.birthdays.Birthday;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.birthdays.BirthdayMonth;
import com.beanbeanjuice.cafeapi.wrapper.endpoints.birthdays.BirthdaysEndpoint;
import com.beanbeanjuice.cafebot.CafeBot;
import com.beanbeanjuice.cafebot.utility.commands.Command;
import com.beanbeanjuice.cafebot.utility.commands.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

public class SetBirthdaySubCommand extends Command implements ISubCommand {

    public SetBirthdaySubCommand(CafeBot cafeBot) {
        super(cafeBot);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        BirthdayMonth month = BirthdayMonth.valueOf(event.getOption("month").getAsString());
        int day = event.getOption("day").getAsInt();
        TimeZone timezone = TimeZone.getTimeZone(event.getOption("timezone").getAsString());

        if (month.getDaysInMonth() < day) {
            invalidMonthAndDayCombination(month, day, event);
            return;
        }

        User user = event.getUser();

        Birthday birthday = new Birthday(month, day, timezone.getID(), false);
        BirthdaysEndpoint birthdaysEndpoint = cafeBot.getCafeAPI().getBirthdaysEndpoint();
        birthdaysEndpoint.createUserBirthday(user.getId(), birthday)
                .exceptionallyAsync((ignored) -> {
                    birthdaysEndpoint.updateUserBirthday(user.getId(), birthday);
                    return null;
                })
                .thenRunAsync(() -> {
                    event.getHook().sendMessageEmbeds(Helper.successEmbed(
                            "Birthday Set",
                            String.format("You have successfully set your birthday to **%s %d** on *%s*.", month, day, timezone.getDisplayName())
                    )).queue();
                });
    }

    private void invalidMonthAndDayCombination(final BirthdayMonth month, final int day, final SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                "Invalid Month and Day Combo",
                String.format("There are not **%d** days in **%s**.", day, month.toString())
        )).queue();
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
                new OptionData(OptionType.STRING, "month", "The month you were born!", true)
                        .addChoice("1 - January", "JANUARY")
                        .addChoice("2 - February", "FEBRUARY")
                        .addChoice("3 - March", "MARCH")
                        .addChoice("4 - April", "APRIL")
                        .addChoice("5 - May", "MAY")
                        .addChoice("6 - June", "JUNE")
                        .addChoice("7 - July", "JULY")
                        .addChoice("8 - August", "AUGUST")
                        .addChoice("9 - September", "SEPTEMBER")
                        .addChoice("10 - October", "OCTOBER")
                        .addChoice("11 - November", "NOVEMBER")
                        .addChoice("12 - December", "DECEMBER"),

                new OptionData(OptionType.INTEGER, "day", "The day you were born in the specified month!", true)
                        .setRequiredRange(1, 31),

                new OptionData(OptionType.STRING, "timezone", "Start typing to see available options!", true, true)
        };
    }

    @Override
    public HashMap<String, ArrayList<String>> getAutoComplete() {
        HashMap<String, ArrayList<String>> autoCompleteMap = new HashMap<>();
        autoCompleteMap.put("timezone", new ArrayList<>());

        for (String timezone : TimeZone.getAvailableIDs())
            autoCompleteMap.get("timezone").add(timezone);

        return autoCompleteMap;
    }

}
