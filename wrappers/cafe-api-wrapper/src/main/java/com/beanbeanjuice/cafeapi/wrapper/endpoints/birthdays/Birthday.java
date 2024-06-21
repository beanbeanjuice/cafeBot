package com.beanbeanjuice.cafeapi.wrapper.endpoints.birthdays;

import com.beanbeanjuice.cafeapi.wrapper.exception.program.BirthdayOverfillException;
import com.beanbeanjuice.cafeapi.wrapper.exception.program.InvalidTimeZoneException;
import com.beanbeanjuice.cafeapi.wrapper.utility.Time;
import lombok.Getter;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * A class used to house {@link Birthday}.
 *
 * @author beanbeanjuice
 */
public class Birthday {

    @Getter private final BirthdayMonth month;
    @Getter private final int day;
    @Getter private final TimeZone timeZone;
    @Getter private final boolean isAlreadyMentioned;

    /**
     * Creates a new {@link Birthday} object.
     * @param month The {@link BirthdayMonth month} of the {@link Birthday}.
     * @param day The {@link Integer day} of the {@link Birthday}.
     * @param isAlreadyMentioned False, if the user's birthday has not been mentioned by cafeBot.
     */
    public Birthday(final BirthdayMonth month, final int day, final String timeZone,
                    final boolean isAlreadyMentioned) throws InvalidTimeZoneException, BirthdayOverfillException {
        this.month = month;
        this.day = day;

        if (month.getDaysInMonth() < day)
            throw new BirthdayOverfillException("You specified more days than there are in this month!");

        if (!Time.isValidTimeZone(timeZone))
            throw new InvalidTimeZoneException("The timezone specified is not allowed!");

        this.timeZone = TimeZone.getTimeZone(timeZone);
        this.isAlreadyMentioned = isAlreadyMentioned;
    }

    /**
     * @return The {@link Date} of the {@link Birthday} in {@link TimeZone UTC} time.
     * @throws ParseException Thrown when the {@link Birthday} was unable to be parsed.
     */
    public Date getUTCDate() throws ParseException {
        return Time.getFullDate(month.getMonthNumber() + "-" + day + "-2020", timeZone);
    }

}
