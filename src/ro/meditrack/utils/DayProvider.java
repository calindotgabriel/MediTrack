package ro.meditrack.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Helper class using a calendar to provide time information.
 */
public class DayProvider {

    /**
     * Gets the current day of the week.
     * @return day of week, formated as a String.
     */
    public static String getCurrentDay () {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        String weekDay = dayFormat.format(calendar.getTime());

        return weekDay;
    }

    /**
     * Gets the configuration for days
     * Logic used in our details fragments
     */
    public static int getConfigForDays() {
        String weekDay = getCurrentDay();

        if (weekDay.equals("Saturday"))
            return 2;
        if (weekDay.equals("Sunday"))
            return 3;
        return 1;
    }
}
