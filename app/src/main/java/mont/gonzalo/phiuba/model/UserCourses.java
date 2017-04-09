package mont.gonzalo.phiuba.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

/**
 * Created by Gonzalo Montiel on 3/20/17.
 */
public class UserCourses extends Observable implements Serializable {
    private HashMap<String, UserCourse> approvedCourses;
    private HashMap<String, UserCourse> studyingCourses;
    private HashMap<String, UserCourse> favouriteCourses;

    private static UserCourses _instance = null;

    public static UserCourses getInstance() {
        if (_instance == null) {
            _instance = new UserCourses();
        }
        return _instance;
    }

    private UserCourses() {
        approvedCourses = new HashMap<String, UserCourse>();
        studyingCourses = new HashMap<String, UserCourse>();
        favouriteCourses = new HashMap<String, UserCourse>();
    }

    public void addApproved(Course c, Integer calification) {
        Log.d(c.getCode(), String.valueOf(calification));
        this.studyingCourses.remove(c.getCode());
        this.favouriteCourses.remove(c.getCode());
        this.approvedCourses.put(c.getCode(), new UserCourse(c, CourseStatus.APPROVED, calification));
        this.notifyObservers();
    }

    public void addStudying(Course c) {
        this.approvedCourses.remove(c.getCode());
        this.favouriteCourses.remove(c.getCode());
        this.studyingCourses.put(c.getCode(), new UserCourse(c, CourseStatus.STUDYING));
        this.notifyObservers();
    }

    public void addFavourite(Course c) {
        this.studyingCourses.remove(c.getCode());
        this.approvedCourses.remove(c.getCode());
        this.favouriteCourses.put(c.getCode(), new UserCourse(c, CourseStatus.FAVOURITE));
        this.notifyObservers();
    }

    public void removeCourse(Course c) {
        this.studyingCourses.remove(c.getCode());
        this.approvedCourses.remove(c.getCode());
        this.favouriteCourses.remove(c.getCode());
        this.notifyObservers();
    }

    public HashMap<String, UserCourse> getApprovedCourses() {
        return approvedCourses;
    }

    public double getAverageCalification() {
        double sum = 0;
        for (UserCourse uc: this.approvedCourses.values()) {
            sum += uc.getCalification();
        }
        return sum / getApprovedCount();
    }

    public int getApprovedCount() {
        return this.approvedCourses.keySet().size();
    }

    public double getCredits() {
        int sum = 0;
        for (UserCourse uc: this.approvedCourses.values()) {
            sum += uc.getCourse().getCredits();
        }
        return sum;
    }

    public void printSummary() {
        Log.d("Studying", this.studyingCourses.keySet().toString());
        Log.d("Approved", this.approvedCourses.keySet().toString());
        Log.d("Favourites", this.favouriteCourses.keySet().toString());
        Log.d("Average", String.valueOf(getAverageCalification()));
        Log.d("Total approved", String.valueOf(getApprovedCount()));
    }

    public HashMap<String, UserCourse> getStudyingCourses() {
        return studyingCourses;
    }

    public static List<Course> filterApproved(List<Course> mCourses) {
        List<Course> filtered = new ArrayList<>();
        for (Course c: mCourses) {
            if (UserCourses.getInstance().approvedCourses.containsKey(c.getCode())) {
                filtered.add(c);
            }
        }
        return filtered;
    }

    public static List<Course> filterStudying(List<Course> mCourses) {
        List<Course> filtered = new ArrayList<>();
        for (Course c: mCourses) {
            if (UserCourses.getInstance().studyingCourses.containsKey(c.getCode())) {
                filtered.add(c);
            }
        }
        return filtered;

    }

    public static List<Course> filterNotCoursed(List<Course> mCourses) {
        UserCourses ucs = UserCourses.getInstance();
        List<Course> filtered = new ArrayList<>();
        for (Course c: mCourses) {
            if (!ucs.approvedCourses.containsKey(c.getCode()) &&
                    !ucs.studyingCourses.containsKey(c.getCode())) {
                filtered.add(c);
            }
        }
        return filtered;
    }

    public int getCalification(Course c) {
        UserCourse uc = approvedCourses.get(c.getCode());
        if (uc != null) {
            return uc.getCalification();
        }
        return -1;
    }
}
