package mont.gonzalo.phiuba.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gonzalo Montiel on 1/25/17.
 */

public class CathedraSchedule implements Serializable, Calendable {
    private String day;
    private String from;
    private String to;
    private String type;
    private String classroomCode;

    private static String[] daysOfWeek = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
    CathedraSchedule() { }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassroomCode() {
        return classroomCode;
    }

    public void setClassroomCode(String classroomCode) {
        this.classroomCode = classroomCode;
    }

    public String getShortType() {
        String initials;
        String[] s = type.split(" ");
        if (s.length == 2) {
            return s[0].substring(0,1).toUpperCase() +  s[1].substring(0,1).toUpperCase();
        } else if (s.length == 1) {
            return s[0].substring(0,1).toUpperCase() + s[0].substring(s[0].length() - 1).toLowerCase();
        }
        return "";
    }

    @Override
    public String toString() {
        return this.getDay() + " " + getTimeToString();
    }

    public String getTimeToString() {
        return this.getFrom() + " to " + this.getTo();
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Date getStart() {
        return null;
    }

    @Override
    public Date getEnd() {
        return null;
    }

    @Override
    public String getLocation() {
        return null;
    }

    // Converts HH:MM in milliseconds
    public long getHourToLong(String hour, String year, String month, String day) {
        Calendar cal = Calendar.getInstance();
        day = day.length() == 1? ('0' + day) : day;
        month = month.length() == 1? ('0' + month) : month;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH:mm");
        Date date = new Date();
        try {
            date = sdf.parse(year + month + day + '-' + hour);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public long getToAsLong(String endYear, String endMonth, String endDate) {
        return getHourToLong(this.getTo(), endYear, endMonth, endDate);
    }

    public long getFromAsLong(String beginYear, String beginMonth, String beginDate) {
        return getHourToLong(this.getFrom(), beginYear, beginMonth, beginDate);
    }

    public int getDayOfWeek() {
        return Arrays.asList(daysOfWeek).indexOf(getDay()) + 1;
    }
}
