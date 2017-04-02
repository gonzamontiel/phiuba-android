package mont.gonzalo.phiuba.model;

import com.alamkanak.weekview.DateTimeInterpreter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Gonzalo Montiel on 3/26/17.
 */

public class CustomDateTimeInterpreter implements DateTimeInterpreter {
    @Override
    public String interpretDate(Calendar date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());
            return sdf.format(date.getTime()).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String interpretTime(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }}
