package com.beanbeanjuice.cafebot.command.fun.birthday;

import com.beanbeanjuice.cafebot.utility.command.CommandCategory;
import com.beanbeanjuice.cafebot.utility.command.ISubCommand;
import com.beanbeanjuice.cafebot.utility.helper.Helper;
import com.beanbeanjuice.cafebot.utility.section.fun.BirthdayHandler;
import com.beanbeanjuice.cafeapi.wrapper.cafebot.birthdays.Birthday;
import com.beanbeanjuice.cafeapi.wrapper.cafebot.birthdays.BirthdayMonth;
import com.beanbeanjuice.cafeapi.wrapper.exception.api.TeaPotException;
import com.beanbeanjuice.cafeapi.wrapper.exception.program.BirthdayOverfillException;
import com.beanbeanjuice.cafeapi.wrapper.utility.Time;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} to set your {@link Birthday Birthday}.
 *
 * @author beanbeanjuice
 */
public class SetBirthdaySubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if (!Time.isValidTimeZone(event.getOption("timezone").getAsString())) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Invalid Timezone",
                    "The timezone specified is not valid! Try checking [this](https://garygregory.wordpress.com/2013/06/18/what-are-the-java-timezone-ids/) list."
            )).queue();
            return;
        }

        Birthday birthday;

        try {
            birthday = new Birthday(BirthdayMonth.valueOf(
                    event.getOption("month").getAsString()),
                    event.getOption("day").getAsInt(),
                    event.getOption("timezone").getAsString(),
                    false);
        } catch (BirthdayOverfillException e) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Invalid Day",
                    e.getMessage() + " If you think this is an error, please make a bug report."
            )).queue();
            return;
        }


        try {
            BirthdayHandler.updateBirthday(event.getUser().getId(), birthday);
            event.getHook().sendMessageEmbeds(Helper.successEmbed(
                    "Updated Birthday",
                    "Successfully updated your birthday to `" + birthday.getMonth() + ", " + birthday.getDay() + "` (Month, Day) on `" +
                            birthday.getTimeZone().getID() + "`.\n\n*By setting your birthday, " +
                            "you are agreeing to be notified in EVERY server that this bot is in and that you are in, granted that they have enabled " +
                            "birthday notifications. To opt out, do `/birthday remove`. Remember, the server has to have a valid birthday channel set by doing " +
                            "`/birthday-channel set`!*"
            )).queue();
        } catch (TeaPotException e) {
            event.getHook().sendMessageEmbeds(Helper.errorEmbed(
                    "Invalid Month/Day",
                    "The month/day specified is invalid. If you have chosen more days than there are in the month, it will cause an error."
            )).queue();
        }
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Set your birthday!";
    }

    @NotNull
    @Override
    public String exampleUsage() {
        return "`/set-birthday january 1`";
    }

    @NotNull
    @Override
    public ArrayList<OptionData> getOptions() {
        ArrayList<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "month", "The month you were born!", true)
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
                .addChoice("12 - December", "DECEMBER"));
        options.add(new OptionData(OptionType.INTEGER, "day", "The day you were born in the specified month!", true)
                .setRequiredRange(1, 31));

        OptionData timeZoneOptions = new OptionData(OptionType.STRING, "timezone", "Start typing to see available options!", true, true);

        options.add(timeZoneOptions);
        return options;
    }

    @NotNull
    @Override
    public CommandCategory getCategoryType() {
        return CommandCategory.FUN;
    }

    @NotNull
    @Override
    public Boolean allowDM() {
        return true;
    }

    @NotNull
    @Override
    public Boolean isHidden() {
        return true;
    }

    @NotNull
    @Override
    public String getName() {
        return "set";
    }
}
