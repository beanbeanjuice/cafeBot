package com.beanbeanjuice.cafeapi.cafebot.birthdays;

import com.beanbeanjuice.cafeapi.exception.program.BirthdayOverfillException;
import com.beanbeanjuice.cafeapi.exception.program.InvalidTimeZoneException;
import com.beanbeanjuice.cafeapi.utility.Time;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * A class used to house {@link Birthday}.
 *
 * @author beanbeanjuice
 */
public class Birthday {

    private final BirthdayMonth month;
    private final int day;
    private final TimeZone timeZone;
    private final boolean alreadyMentioned;

    /**
     * Creates a new {@link Birthday} object.
     * @param month The {@link BirthdayMonth month} of the {@link Birthday}.
     * @param day The {@link Integer day} of the {@link Birthday}.
     * @param alreadyMentioned False, if the user's birthday has not been mentioned by cafeBot.
     */
    public Birthday(BirthdayMonth month, int day, String timeZone,
                    boolean alreadyMentioned) throws InvalidTimeZoneException, BirthdayOverfillException {
        this.month = month;
        this.day = day;

        if (month.getDaysInMonth() < day)
            throw new BirthdayOverfillException("You specified more days than there are in this month!");

        if (!Time.isValidTimeZone(timeZone))
            throw new InvalidTimeZoneException("The timezone specified is not allowed!");

        this.timeZone = TimeZone.getTimeZone(timeZone);
        this.alreadyMentioned = alreadyMentioned;
    }

    /**
     * @return The {@link BirthdayMonth month} of the {@link Birthday}.
     */
    public BirthdayMonth getMonth() {
        return month;
    }

    /**
     * @return The {@link Integer day} of the {@link Birthday}.
     */
    public int getDay() {
        return day;
    }

    /**
     * @return False, if the user's birthday has not been mentioned by cafeBot.
     */
    public boolean alreadyMentioned() {
        return alreadyMentioned;
    }

    /**
     * @return The {@link Date} of the {@link Birthday} in {@link TimeZone UTC} time.
     * @throws ParseException Thrown when the {@link Birthday} was unable to be parsed.
     */
    public Date getUTCDate() throws ParseException {
        return Time.getFullDate(month.getMonthNumber() + "-" + day + "-2020", timeZone);
    }

    /**
     * @return The {@link TimeZone} for the {@link Birthday}.
     */
    public TimeZone getTimeZone() {
        return timeZone;
    }

}
