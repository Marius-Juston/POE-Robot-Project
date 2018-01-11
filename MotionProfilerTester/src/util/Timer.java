package util;

import java.util.Calendar;
import java.util.TimeZone;

public class Timer {
    public static double getFPGATimestamp() {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(Calendar.SECOND);
    }
}
