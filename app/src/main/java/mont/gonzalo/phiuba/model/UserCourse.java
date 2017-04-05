package mont.gonzalo.phiuba.model;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Range;

import java.io.Serializable;

public class UserCourse implements Serializable  {
    private Course course;
    private CourseStatus status;
    private CathedraSchedule schedule;
    private Integer calification = 0;

    public UserCourse(Course course, CourseStatus status, Integer calif) {
        this.course = course;
        this.status = status;
        this.calification = calif;
    }

    public UserCourse(Course course, CourseStatus status) {
        this(course, status, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean inRange(Integer i) {
        return getCalificationRange().contains(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static protected Range getCalificationRange() {
        return new Range<Integer>(1, 10);
    }

    public Integer getCalification() {
        return calification;
    }

    public void setCalification(Integer calification) {
        this.calification = calification;
    }

    public CathedraSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(CathedraSchedule schedule) {
        this.schedule = schedule;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
