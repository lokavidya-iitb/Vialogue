package com.comp.iitb.vialogue.library;

import java.util.concurrent.TimeUnit;

/**
 * Created by shubh on 17-01-2017.
 */

public class TimeFormater {
    public static String getHoursMinutesAndSeconds(int milliseconds) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    public static String getMinutesAndSeconds(int milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    public static String getHours(int milliseconds) {
        return String.format("%02d", TimeUnit.MILLISECONDS.toHours(milliseconds));
    }

    public static String getMinutes(int milliseconds){
        return String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(milliseconds));
    }

    public static String getSeconds(int milliseconds){
        return String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(milliseconds));
    }
}
