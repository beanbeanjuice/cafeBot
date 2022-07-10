package com.beanbeanjuice.command.fun.birthday;

import com.beanbeanjuice.utility.command.CommandCategory;
import com.beanbeanjuice.utility.command.ISubCommand;
import io.github.beanbeanjuice.cafeapi.cafebot.birthdays.BirthdayMonth;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * An {@link ISubCommand} to set your {@link io.github.beanbeanjuice.cafeapi.cafebot.birthdays.Birthday Birthday}.
 *
 * @author beanbeanjuice
 */
public class SetBirthdaySubCommand implements ISubCommand {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessage(BirthdayMonth.valueOf(event.getOption("month").getAsString()).toString()).queue();
    }

    /**
     * Gets the {@link BirthdayMonth} from a {@link String}.
     * @param month The {@link String} to parse.
     * @return The parsed {@link BirthdayMonth}. Null, if there is an error.
     */
    @Nullable
    private BirthdayMonth getBirthdayMonth(@NotNull String month) {
        for (BirthdayMonth birthdayMonth : BirthdayMonth.values()) {
            if (birthdayMonth.toString().equalsIgnoreCase(month))
                return birthdayMonth;
        }
        return null;
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
