package mont.gonzalo.phiuba.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mont.gonzalo.phiuba.api.DataFetcher;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by gonzalo on 11/20/16.
 */

public class User {
    private Plan plan;
    private String firstName;
    private String lastName;
    private HashMap<CourseStatus, List<String>> coursesMap;
    private List<CathedraSchedule> schedulesCache;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.initializeCoursesWithMockData();
    }

    private void initializeCoursesWithMockData() {
        List<String> l = new ArrayList<String>();
        l.add("63.01");
        l.add("75.40");
        l.add("61.08");
        l.add("62.03");
        this.coursesMap.put(CourseStatus.APPROVED, l);
    }

    public void selectPlan(String planCode) {
        this.plan = Plan.byCode(planCode);
    }

    public List<Course> getApprovedCourses() {
        DataFetcher df = new DataFetcher();
        df.findSpecificCourses(this.coursesMap.get(CourseStatus.APPROVED), new Callback<List<Course>>() {
            @Override
            public void success(List<Course> courses, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        // TODO return real courses list
        return new ArrayList<Course>();
    }

    public String getPlanCode() {
        return this.plan.getCode();
    }

    public void addSchedule(CathedraSchedule cs) {
        this.schedulesCache.add(cs);
    }

    public void removeSchedule(CathedraSchedule cs) {
        this.schedulesCache.remove(cs);
    }

    private static User mock_instance = null;

    static User getMock() {
        if (mock_instance == null) {
            mock_instance = new User("Harry", "Potter");
            mock_instance.selectPlan(Plan.getDefault());
        }
        return mock_instance;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }
}
