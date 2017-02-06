package mont.gonzalo.phiuba.model;

/**
 * Created by gonzalo on 1/25/17.
 */

public class CathedraSchedule {
    private final String time;
    private String day;
    private String from;
    private String to;
    private String type;
    private String classroomCode;

    CathedraSchedule() {
        this.time = getTimeToString();
    }

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
            return type.substring(0,1).toUpperCase() + type.substring(-1).toLowerCase();
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
}
