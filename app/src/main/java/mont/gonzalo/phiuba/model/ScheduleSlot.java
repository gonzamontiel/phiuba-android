package mont.gonzalo.phiuba.model;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.alamkanak.weekview.WeekViewEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static ScheduleSlot getSample() {
        ScheduleSlot sslot = new ScheduleSlot();
        sslot.setName("Test");
        sslot.setColor("#64dd17");
        sslot.setDayOfWeek(2);
        sslot.setStartTime("19:00");
        sslot.setEndTime("22:00");
        return sslot;
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
        Calendar now = Calendar.getInstance();
        Calendar startTime = (Calendar) now.clone();
        startTime.setTimeInMillis(start.getTime());
        startTime.set(Calendar.YEAR, now.get(Calendar.YEAR));
        startTime.set(Calendar.MONTH, now.get(Calendar.MONTH));

        // Always get first week pf the month
        startTime.set(Calendar.WEEK_OF_MONTH, 1);
        startTime.set(Calendar.DAY_OF_WEEK, getDayOfWeek());

        Calendar endTime = (Calendar) startTime.clone();
        endTime.setTimeInMillis(end.getTime());
        endTime.set(Calendar.YEAR, startTime.get(Calendar.YEAR));
        endTime.set(Calendar.MONTH, startTime.get(Calendar.MONTH));
        endTime.set(Calendar.DAY_OF_MONTH, startTime.get(Calendar.DAY_OF_MONTH));

        // Create a week view event
        WeekViewEvent weekViewEvent = new WeekViewEvent();
        weekViewEvent.setName(getName());
        weekViewEvent.setStartTime(startTime);
        weekViewEvent.setEndTime(endTime);
        weekViewEvent.setColor(Color.parseColor(getColor()));

        return weekViewEvent;
    }
}