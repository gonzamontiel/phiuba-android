package mont.gonzalo.phiuba.model;

import android.content.res.Resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mont.gonzalo.phiuba.R;

/**
 * Created by gonzalo on 1/25/17.
 */

public class Cathedra implements Serializable {
    private String courseCode;
    private String teachers;
    private int seats;
    private String availablePlans;
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

    public List<CathedraSchedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<CathedraSchedule> schedule) {
        this.schedule = schedule;
    }


    public String getSchedulesAsMultilineText(Resources res) {
        String text = "";
        if (schedule != null) {
            for (CathedraSchedule cs: this.getSchedule()) {
                text += cs.getDay() + " " +
                        cs.getFrom() + " a " + cs.getTo() + ". " +
                        res.getString(R.string.classroom)+ " " + cs.getClassroomCode() +
                        ". " + cs.getShortType() + "\n";
            }
        }
        return text;
    }

    public static List<Cathedra> getSampleCathedras() {
        Cathedra cat1 = new Cathedra();
        cat1.setCourseCode("75.12");
        cat1.setTeachers("Sassano-Payva-Bello-Sarris");
        cat1.setSeats(70);
        cat1.setAvailablePlans("Civil, Industrial, Naval, Mecánica, Electricista, Electrónica, Química, Sistemas, Informática");

        CathedraSchedule sch1 = new CathedraSchedule();
        sch1.setDay("Miércoles");
        sch1.setFrom("14:00");
        sch1.setTo("17:00");
        sch1.setType("Teórica");
        sch1.setClassroomCode("PC-A ASIG");

        CathedraSchedule sch2 = new CathedraSchedule();
        sch2.setDay("Jueves");
        sch2.setFrom("14:00");
        sch2.setTo("17:00");
        sch2.setType("Teórico");
        sch2.setClassroomCode("PC-A ASIG");

        cat1.setSchedule(new ArrayList<CathedraSchedule>(Arrays.asList(sch1, sch2)));

        Cathedra cat2 = new Cathedra();
        cat2.setCourseCode("75.12");
        cat2.setTeachers("Tarela-Poltarak-Ezcurra-Vitell");
        cat2.setSeats(60);
        cat2.setAvailablePlans("Civil, Industrial, Naval, Mecánica, Electricista, Electrónica, Química, Sistemas, Informática");

        CathedraSchedule sch3 = new CathedraSchedule();
        sch3.setDay("Miércoles");
        sch3.setFrom("19:00");
        sch3.setTo("22:00");
        sch3.setType("Práctica");
        sch3.setClassroomCode("PC-A ASIG");

        CathedraSchedule sch4 = new CathedraSchedule();
        sch4.setDay("Lunes");
        sch4.setFrom("19:00");
        sch4.setTo("22:00");
        sch4.setType("Teórica");
        sch4.setClassroomCode("PC-414");
        cat2.setSchedule(new ArrayList<CathedraSchedule>(Arrays.asList(sch3, sch4)));

        return new ArrayList<Cathedra>(Arrays.asList(cat1,cat2));
    }
}