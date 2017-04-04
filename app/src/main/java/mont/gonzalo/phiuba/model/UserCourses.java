package mont.gonzalo.phiuba.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mont.gonzalo.phiuba.api.DataFetcher;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Gonzalo Montiel on 3/20/17.
 */
public class UserCourses implements Serializable {
    private HashMap<String, UserCourse> approvedCourses;
    private HashMap<String, UserCourse> studyingCourses;
    private HashMap<String, UserCourse> favouriteCourses;

    private static HashMap<String, UserCourses> _instances = new HashMap<String, UserCourses>();

    public static UserCourses getInstance() {
        String planCode = User.get().getPlanCode();
        if (!_instances.containsKey(planCode)) {
            _instances.put(planCode, new UserCourses());
        }
        return _instances.get(planCode);
    }

    private UserCourses() {
        approvedCourses = new HashMap<String, UserCourse>();
        studyingCourses = new HashMap<String, UserCourse>();
        favouriteCourses = new HashMap<String, UserCourse>();
    }

    public void addApproved(Course c, Integer calification) {
        this.studyingCourses.remove(c.getCode());
        this.favouriteCourses.remove(c.getCode());
        this.approvedCourses.put(c.getCode(), new UserCourse(c, CourseStatus.APPROVED, calification));
    }

    public void addStudying(Course c) {
        this.approvedCourses.remove(c.getCode());
        this.favouriteCourses.remove(c.getCode());
        this.studyingCourses.put(c.getCode(), new UserCourse(c, CourseStatus.STUDYING));
    }

    public void addFavourite(Course c) {
        this.studyingCourses.remove(c.getCode());
        this.approvedCourses.remove(c.getCode());
        this.favouriteCourses.put(c.getCode(), new UserCourse(c, CourseStatus.FAVOURITE));
    }

    public void removeCourse(Course c) {
        this.studyingCourses.remove(c.getCode());
        this.approvedCourses.remove(c.getCode());
        this.favouriteCourses.remove(c.getCode());
    }

    public HashMap<String, UserCourse> getStudyingCourses() {
        return studyingCourses;
    }

    public static void egenerateMagicCourses(List<Course> courses) {
        final UserCourses ucs = getInstance();
        for (final Course c: courses) {
            if (c.getCode().equals("61.03") ||
                    c.getCode().equals("61.10") ||
                    c.getCode().equals("62.03") ||
                    c.getCode().equals("75.41")) {
                DataFetcher.getInstance().getCathedras(c.getCode(), new Callback<List<Cathedra>>() {
                    @Override
                    public void success(List<Cathedra> cathedras, Response response) {
                        if (cathedras.size() > 0) {
                            c.setCathedras(cathedras);
                            ucs.addStudying(c);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }
    }

    public static List<Course> filterApproved(List<Course> mCourses) {
        return mCourses;
    }

    public static List<Course> filterStudying(List<Course> mCourses) {
        return mCourses;
    }

    public static List<Course> filterNotCoursed(List<Course> mCourses) {
        return mCourses;
    }
}
