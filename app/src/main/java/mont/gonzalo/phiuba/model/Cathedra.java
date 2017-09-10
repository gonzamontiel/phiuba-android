package mont.gonzalo.phiuba.model;

import android.content.res.Resources;

import com.alamkanak.weekview.WeekViewEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.layout.ActivityContext;
import mont.gonzalo.phiuba.layout.MaterialColors;

/**
 * Created by Gonzalo Montiel on 1/25/17.
 */

public class Cathedra implements Serializable {
    private String courseCode;
    private String teachers;
    private int seats;
    private String availablePlans;
    private Integer color;
    private List<CathedraSchedule> schedule;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getTeachers() {
        return teachers.replace("-", ", ");
    }

    public String getTeachersKey() {
        return teachers;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String[] getAvailablePlans() {
        return availablePlans.split(",");
    }

    public void setAvailablePlans(String availablePlans) {
        this.availablePlans = availablePlans;
    }

    public List<CathedraSchedule> getSchedules() {
        return schedule;
    }

    public void setSchedule(List<CathedraSchedule> schedule) {
        this.schedule = schedule;
    }

    public List<WeekViewEvent> toWeekEvents(String name) {
        List<WeekViewEvent> eventList = new ArrayList<WeekViewEvent>();
        for (CathedraSchedule cs: getSchedules()) {
            ScheduleSlot sslot = new ScheduleSlot();
            sslot.setName(name);
            sslot.setColor(ActivityContext.get().getResources().getString(getColor()));
            sslot.setDayOfWeek(cs.getDayOfWeek());
            sslot.setStartTime(cs.getFrom());
            sslot.setEndTime(cs.getTo());
            eventList.add(sslot.toWeekViewEvent());
        }
        return eventList;
    }

    public String getSchedulesAsMultilineText(Resources res) {
        String text = "";
        if (schedule != null) {
            for (CathedraSchedule cs: this.getSchedules()) {
                text += cs.getDay() + " " +
                        cs.getFrom() + " a " + cs.getTo() + ". " +
                        res.getString(R.string.classroom)+ " " + cs.getClassroomCode() +
                        ". " + cs.getShortType() + "\n";
            }
        }
        return text;
    }

    public Integer getColor() {
        if (color == null) {
            color = MaterialColors.getRandom();
        }
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Cathedra) obj).getTeachersKey() == this.getTeachersKey();
    }
}
