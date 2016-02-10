package gq.vaccum121.testtask.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;


public class DateUtil {
    public static final String DATE_FORMAT = "E MMM dd HH:mm:ss Z yyyy";

    public static Date parse(String stringDate) {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        try {
            date = format.parse(stringDate);
        } catch (ParseException e) {
            Timber.e("Invalid string format!");
        }
        return date;
    }

    public static String toString(Date date) {
        DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return format.format(date);
    }

}
