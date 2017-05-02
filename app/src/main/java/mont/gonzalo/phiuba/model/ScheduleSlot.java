package mont.gonzalo.phiuba.model;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Gonzalo Montiel on 3/7/17.
 */
public class ScheduleSlot {
    private String mName;
    private int mDayOfWeek;
    private String mStartTime;
    private String mEndTime;
    private String mColor;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getDayOfWeek() {
        return mDayOfWeek;
    }

    public void setDayOfWeek(int dayOfMonth) {
        this.mDayOfWeek = dayOfMonth;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        this.mStartTime = startTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        this.mEndTime = endTime;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        this.mColor = color;
    }

    @SuppressLint("SimpleDateFormat")
    public WeekViewEvent toWeekViewEvent(){

        // Parse time.
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date start = new Date();
        Date end = new Date();
        try {
            start = sdf.parse(getStartTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            end = sdf.parse(getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Initialize start and end time.
        Calendar startTime = getUniqueWeekDate();
        startTime.setTimeInMillis(start.getTime());
        startTime.set(2007, Calendar.JANUARY, getDayOfWeek());

        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy G");

        Calendar endTime = getUniqueWeekDate();
        endTime.setTimeInMillis(end.getTime());
        endTime.set(2007, Calendar.JANUARY, getDayOfWeek());

        // Create an week view event.
        WeekViewEvent weekViewEvent = new WeekViewEvent();
        weekViewEvent.setName(getName());
        weekViewEvent.setStartTime(startTime);
        weekViewEvent.setEndTime(endTime);
        weekViewEvent.setColor(Color.parseColor(getColor()));

        return weekViewEvent;
    }

    public static Calendar getUniqueWeekDate() {
        Calendar date = (Calendar) Calendar.getInstance().clone();
        date.set(2007, Calendar.JANUARY, 1);
        return date;
    }

    public static List<WeekViewEvent> getSamples() {
        List<WeekViewEvent> slots = new ArrayList<WeekViewEvent>();

        ScheduleSlot sslot = new ScheduleSlot();
        sslot.setName(null);
        sslot.setColor("#64dd17");
        sslot.setDayOfWeek(1);
        sslot.setStartTime("19:00");
        sslot.setEndTime("22:00");
        slots.add(sslot.toWeekViewEvent());

        sslot = new ScheduleSlot();
        sslot.setName(null);
        sslot.setColor("#64dd17");
        sslot.setDayOfWeek(4);
        sslot.setStartTime("19:00");
        sslot.setEndTime("22:00");
        slots.add(sslot.toWeekViewEvent());

        sslot = new ScheduleSlot();
        sslot.setName(null);
        sslot.setColor("#64b5f6");
        sslot.setDayOfWeek(2);
        sslot.setStartTime("15:00");
        sslot.setEndTime("19:00");
        slots.add(sslot.toWeekViewEvent());

        sslot = new ScheduleSlot();
        sslot.setName(null);
        sslot.setColor("#64b5f6");
        sslot.setDayOfWeek(3);
        sslot.setStartTime("19:00");
        sslot.setEndTime("23:00");
        slots.add(sslot.toWeekViewEvent());

        sslot = new ScheduleSlot();
        sslot.setName(null);
        sslot.setColor("#e53935");
        sslot.setDayOfWeek(6);
        sslot.setStartTime("19:00");
        sslot.setEndTime("22:00");
        slots.add(sslot.toWeekViewEvent());

        sslot = new ScheduleSlot();
        sslot.setName(null);
        sslot.setColor("#e53935");
        sslot.setDayOfWeek(4);
        sslot.setStartTime("14:00");
        sslot.setEndTime("18:00");
        slots.add(sslot.toWeekViewEvent());

        return slots;
    }
}