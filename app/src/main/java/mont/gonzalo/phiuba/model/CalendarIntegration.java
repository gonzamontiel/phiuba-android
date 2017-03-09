package mont.gonzalo.phiuba.model;

import android.content.Intent;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Gonzalo Montiel on 2/6/17.
 */
public class CalendarIntegration {
    private static final HashMap<String, String> daysHash = createDaysHash();
    private static final String TAG = "CalendarIntegration";
    private static final int Jan = 1;

    private static HashMap<String, String> createDaysHash() {
        HashMap<String, String> hash = new HashMap<String, String>();
        hash.put("Lunes", "MO");
        hash.put("Martes", "TU");
        hash.put("Miércoles", "WE");
        hash.put("Jueves", "TH");
        hash.put("Viernes", "FR");
        return hash;
    }

    public CalendarIntegration() {

    }

    public static String getRruleDay(String spanishDay) {
        return daysHash.get(spanishDay);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Intent getCathedraScheduleIntent(CathedraSchedule cs, String courseName, String teachers) {
        Calendar cal = Calendar.getInstance();
        String year = String.valueOf(cal.get(Calendar.YEAR));

        int currentMonth = cal.get(Calendar.MONTH);
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);

        // Classes start in march and end in december
        // Add events aproximately according to these dates
        // FIXME Add an object which provides valid begin/end classes dates according to country
        String fromDay = String.valueOf(currentDay);
        String fromMonth = String.valueOf(currentMonth <= Calendar.JUNE ? Calendar.MARCH : Calendar.AUGUST);
        String untilDay = String.valueOf(currentMonth <= Calendar.JUNE ? 30 : 31);
        String untilMonth = String.valueOf(currentMonth <= Calendar.JUNE ? Calendar.JUNE : Calendar.DECEMBER);
        String until = year + untilMonth + untilDay;
        String rrule = "FREQ=WEEKLY;BYDAY=" + CalendarIntegration.getRruleDay(cs.getDay()) + ";WKST=SU;UNTIL=" + until;
        Log.d(TAG, rrule);

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cs.getFromAsLong(year, fromMonth, fromDay))
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cs.getToAsLong(year, untilMonth, untilDay))
                .putExtra(CalendarContract.Events.TITLE, courseName)
                .putExtra(CalendarContract.Events.DESCRIPTION, "Cátedra: " + teachers)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Facultad de Ingeniería de la Universidad de Buenos Aires (Sede PC)")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(CalendarContract.Events.RRULE, rrule);
        return intent;
    }
}
