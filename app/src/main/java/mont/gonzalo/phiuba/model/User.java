package mont.gonzalo.phiuba.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mont.gonzalo.phiuba.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Gonzalo Montiel on 11/20/16.
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
        this.coursesMap = new HashMap<CourseStatus, List<String>>();
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

    public static User getMock() {
        if (mock_instance == null) {
            mock_instance = new User("Harry", "Potter");
            mock_instance.selectPlan(Plan.getDefault());
        }
        return mock_instance;
    }

    public static User getFromSharedPrefs(Context context, User user) {
        SharedPreferences mPrefs = context.getSharedPreferences(
                context.getResources().getString(R.string.shared_prefs), MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(context.getResources().getString(R.string.pref_logged_user), "");
        return gson.fromJson(json, User.class);
    }

    public static void saveToSharedPrefs(Context context, User user) {
        SharedPreferences mPrefs = context.getSharedPreferences(
                context.getResources().getString(R.string.shared_prefs), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString(context.getResources().getString(R.string.pref_logged_user), json);
        prefsEditor.commit();
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public static User get() {
        return getMock();
    }
}
