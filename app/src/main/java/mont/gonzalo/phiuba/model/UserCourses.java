package mont.gonzalo.phiuba.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.api.DataFetcher;
import mont.gonzalo.phiuba.layout.ActivityContext;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Gonzalo Montiel on 3/20/17.
 */
public class UserCourses extends Observable implements Serializable {
    private HashMap<String, Double> approvedCourses;
    private HashMap<String, CathedraSchedule> studyingCourses;
    private HashMap<String, CourseStatus> favouriteCourses;
    private HashMap<String, Course> loadedCourses;

    private static UserCourses _instance = null;
    private boolean _ready;

    public static UserCourses getInstance() {
        if (_instance == null) {
            UserCourses saved = getFromSharedPrefs();
            if (saved != null) {
                _instance = saved;
            } else {
                _instance = new UserCourses();
            }
        }
        return _instance;
    }

    private UserCourses(String jsonApproved, String jsonStudying, String jsonFavorite) {
        clearChanged();
        _ready = false;
        approvedCourses = new HashMap<>();
        studyingCourses = new HashMap<>();
        favouriteCourses = new HashMap<>();
        loadCourses(jsonApproved, jsonStudying, jsonFavorite);
    }

    private UserCourses() {
        this("", "", "");
    }

    private void loadCourses(final String jsonApproved, final String jsonStudying, final String jsonFavorite) {
        loadedCourses = new HashMap<>();
        DataFetcher.getInstance().getCourses(User.get().getPlanCode(),new Callback<List<Course>>() {
            @Override
            public void success(List<Course> courses, Response response) {
                // Load courses
                for (Course c: courses) {
                    addCourse(c.getCode(), c);
                }
                Gson gson = new Gson();
                if (!jsonApproved.isEmpty()) {
                    approvedCourses = gson.fromJson(jsonApproved, approvedCourses.getClass());
                }
                if (!jsonStudying.isEmpty()) {
                    studyingCourses = gson.fromJson(jsonStudying, studyingCourses.getClass());
                }
                if (!jsonFavorite.isEmpty()) {
                    favouriteCourses = gson.fromJson(jsonFavorite, favouriteCourses.getClass());
                }
                _ready = true;
                doNotifyObservers();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.getMessage());
            }
        });
    }

    private void addCourse(String code, Course c) {
        this.loadedCourses.put(code, c);
    }

    public void updateCourses() {
        this.loadCourses("", "", "");
    }

    public void addApproved(Course c, Double calification, boolean shouldNotify) {
        Log.d(c.getCode(), String.valueOf(calification));
        doAddApproved(c, calification);
        if (shouldNotify) {
            doNotifyObservers();
        }
    }

    public void addApproved(Course c, Double calification) {
        addApproved(c, calification, true);
    }

    private void doAddApproved(Course c, Double calification) {
        this.studyingCourses.remove(c.getCode());
        this.favouriteCourses.remove(c.getCode());
        this.approvedCourses.put(c.getCode(), calification);
        this.loadedCourses.put(c.getCode(), c);
    }

    private void doNotifyObservers() {
        setChanged();
        notifyObservers();
        clearChanged();
    }

    public void addStudying(Course c) {
        this.approvedCourses.remove(c.getCode());
        this.favouriteCourses.remove(c.getCode());
        this.studyingCourses.put(c.getCode(), null);
        this.loadedCourses.put(c.getCode(), c);
        doNotifyObservers();
    }

    public void addFavourite(Course c) {
        this.studyingCourses.remove(c.getCode());
        this.approvedCourses.remove(c.getCode());
        this.favouriteCourses.put(c.getCode(), CourseStatus.FAVOURITE);
        this.loadedCourses.put(c.getCode(), c);
        doNotifyObservers();
    }

    public void removeCourse(Course c) {
        this.studyingCourses.remove(c.getCode());
        this.approvedCourses.remove(c.getCode());
        this.favouriteCourses.remove(c.getCode());
        doNotifyObservers();
    }

    public HashMap<String, Double> getApprovedCourses() {
        return approvedCourses;
    }

    public double getAverageCalification() {
        double sum = 0;
        for (Double calif: this.approvedCourses.values()) {
            if (calif != null) {
                sum += calif;
            }
        }
        return sum / (double) getApprovedCount();
    }

    public int getApprovedCount() {
        return this.approvedCourses.keySet().size();
    }

    public double getCredits() {
        int sum = 0;
        for (Course c: filterApproved(this.getAll())) {
            sum += c.getCredits();
        }
        return sum;
    }

    public boolean isAvailable(Course c) {
        boolean available = true;
        for (String corr: c.getCorrelatives()) {
            if(!this.approvedCourses.containsKey(corr)) {
                available = false;
                break;
            }
        }
        return available;
    }

    public void printSummary() {
        Log.d("Studying", this.studyingCourses.keySet().toString());
        Log.d("Approved", this.approvedCourses.keySet().toString());
        Log.d("Favourites", this.favouriteCourses.keySet().toString());
        Log.d("Average", String.valueOf(getAverageCalification()));
        Log.d("Total approved", String.valueOf(getApprovedCount()));
    }

    public HashMap<String, CathedraSchedule> getStudyingCourses() {
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

    public double getCalification(Course c) {
        Double calif =  approvedCourses.get(c.getCode());
        if (calif != null) {
            return calif;
        }
        return -1;
    }

    public static UserCourses getFromSharedPrefs() {
        Context context = ActivityContext.get();
        if (context != null) {
            SharedPreferences mPrefs = context.getSharedPreferences(
                    context.getResources().getString(R.string.shared_prefs), MODE_PRIVATE);
            String key = context.getResources().getString(R.string.pref_user_courses);
            String jsonApproved = mPrefs.getString(key + "_approved", "");
            String jsonStudying = mPrefs.getString(key + "_studying", "");
            String jsonFavorite = mPrefs.getString(key + "_favorite", "");
            return new UserCourses(jsonApproved, jsonStudying, jsonFavorite);
        } else {
            return null;
        }
    }

    public List<Course> getAll() {
        return new ArrayList<Course>(loadedCourses.values());
    }

    public boolean isReady() {
        return _ready;
    }

    public void resetPrefs() {
        Context context = ActivityContext.get();
        SharedPreferences mPrefs = context.getSharedPreferences(
                context.getResources().getString(R.string.shared_prefs), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String key = context.getResources().getString(R.string.pref_user_courses);
        prefsEditor.remove(key + "_approved");
        prefsEditor.remove(key + "_studying");
        prefsEditor.remove(key + "_favorite");
    }

    public void saveToSharedPrefs() {
        Context context = ActivityContext.get();
        SharedPreferences mPrefs = context.getSharedPreferences(
                context.getResources().getString(R.string.shared_prefs), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String key = context.getResources().getString(R.string.pref_user_courses);
        prefsEditor.putString(key + "_approved" , gson.toJson(approvedCourses));
        prefsEditor.putString(key + "_studying" , gson.toJson(studyingCourses));
        prefsEditor.putString(key + "_favorite" , gson.toJson(favouriteCourses));
        Log.d("Saving approved", gson.toJson(approvedCourses));
        Log.d("Saving studying", gson.toJson(studyingCourses));
        Log.d("Saving favorite", gson.toJson(favouriteCourses));
        prefsEditor.commit();
    }

    public Course getCourse(String courseCode) {
        return loadedCourses.get(courseCode);
    }

    public void setCourses(List<Course> courses) {
        for (Course c: courses) {
            addCourse(c.getCode(), c);
        }
    }

}
