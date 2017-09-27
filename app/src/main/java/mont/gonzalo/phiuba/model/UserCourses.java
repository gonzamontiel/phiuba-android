package mont.gonzalo.phiuba.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
    public static final int MAX_STUDYING_SIZE = 6;
    private HashMap<String, Double> approvedCourses;
    private HashMap<String, ArrayList<String>> studyingCourses;
    private HashMap<String, Course> loadedCourses;
    private HashMap<String, Course> savedloadedCourses;

    private static List<Course> loadedCoursesArray;
    private static UserCourses _instance = null;
    private static boolean _ready = false;

    public static UserCourses getInstanceSync() {
        loadedCoursesArray = DataFetcher.getInstance().getCoursesSync(User.get().getPlanCode());
        _ready = true;
        return getInstance();
    }

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

    private UserCourses(String jsonApproved, String jsonStudying) {
        clearChanged();
        approvedCourses = new HashMap<>();
        savedloadedCourses = new HashMap<>();
        studyingCourses = new HashMap<>();
        studyingCourses.put(Plan.getFromSharedPrefs(), new ArrayList<String>());
        loadCourses(jsonApproved, jsonStudying);
    }

    private UserCourses() {
        this("", "");
        approvedCourses.put("CBC", 0.0);
    }

    private void loadCourses(final String jsonApproved, final String jsonStudying) {
        if (loadedCourses != null && !loadedCourses.isEmpty()) {
            savedloadedCourses.putAll(loadedCourses);
        }
        loadedCourses = new HashMap<>();
        if (_ready && !loadedCoursesArray.isEmpty()) {
            initialize(loadedCoursesArray, jsonApproved, jsonStudying);
        } else {
            DataFetcher.getInstance().getCourses(User.get().getPlanCode(),new Callback<List<Course>>() {
                @Override
                public void success(List<Course> courses, Response response) {
                    initialize(courses, jsonApproved, jsonStudying);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, error.getMessage());
                }
            });
        }
    }

    private void initialize(List<Course> courses, String jsonApproved, String jsonStudying) {
        setCourses(courses);
        Gson gson = new Gson();
        if (!jsonApproved.isEmpty()) {
            approvedCourses = gson.fromJson(jsonApproved, approvedCourses.getClass());
        }
        if (!jsonStudying.isEmpty()) {
            studyingCourses = gson.fromJson(jsonStudying, studyingCourses.getClass());
        }
        approvedCourses.put("CBC", 0.0);
        loadedCourses.put("CBC", new CourseCBC());
        _ready = true;
        doNotifyObservers();
    }

    private void addCourse(String code, Course c) {
        this.loadedCourses.put(code, c);
    }

    public void updateCourses() {
        _ready = false;
        this.loadCourses("", "");
    }

    public void addApproved(Course c, Double calification, boolean shouldNotify) {
        doAddApproved(c, calification);
        if (shouldNotify) {
            doNotifyObservers();
        }
        saveToSharedPrefs();
    }

    public void addApproved(Course c, Double calification) {
        addApproved(c, calification, true);
    }

    private void doAddApproved(Course c, Double calification) {
        this.getStudyingCoursesCodes().remove(c.getCode());
        this.approvedCourses.put(c.getCode(), calification);
        this.loadedCourses.put(c.getCode(), c);
    }

    private void doNotifyObservers() {
        setChanged();
        notifyObservers();
        clearChanged();
    }

    public boolean addStudying(Course c) {
        if (getStudyingCoursesCodes().size() <= MAX_STUDYING_SIZE) {
            this.approvedCourses.remove(c.getCode());
            addOrInitializeStudyingCourses(c.getCode());
            this.loadedCourses.put(c.getCode(), c);
            c.loadCathedrasAsync();
            doNotifyObservers();
            return true;
        }
        saveToSharedPrefs();
        return false;
    }

    private void addOrInitializeStudyingCourses(String code) {
        String plan = Plan.getFromSharedPrefs();
        ArrayList st = studyingCourses.get(plan);
        if (st == null) {
            st = new ArrayList();
        }
        st.add(code);
        studyingCourses.put(plan, st);
    }

    public void removeCourse(Course c) {
        this.getStudyingCoursesCodes().remove(c.getCode());
        this.approvedCourses.remove(c.getCode());
        saveToSharedPrefs();
        doNotifyObservers();
    }

    public double getAverageCalification() {
        double sum = 0;
        for (Course c: filterApproved(this.getAll())) {
            if (!c.getCode().equals("CBC")) {
                Double calif = this.approvedCourses.get(c.getCode());
                if (calif != null) {
                    sum += calif;
                }
            }
        }
        if (getApprovedCount() > 0) {
            return sum / (double) getApprovedCount();
        }
        return 0;
    }

    public int getApprovedCount() {
        return filterApproved(this.getAll()).size() - 1;
    }

    public int  getCredits() {
        int sum = 0;
        for (Course c: filterApproved(this.getAll())) {
            sum += c.getCredits();
        }
        return sum;
    }

    public boolean isAvailable(Course c) {
        for (String corr: c.getCorrelatives()) {
            if (corr.isEmpty()) {
                continue;
            }
            boolean userHasCorrelative = new CorrelativeCondition(corr).isMetBy(User.get());
            if (!userHasCorrelative) {
                return false;
            }
        }
        return true;
    }

    public void printSummary() {
        Log.d("Studying", this.studyingCourses.toString());
        Log.d("Approved", this.approvedCourses.keySet().toString());
        Log.d("Average", String.valueOf(getAverageCalification()));
        Log.d("Total approved", String.valueOf(getApprovedCount()));
    }

    public static List<Course> filterApproved(List<Course> mCourses) {
        List<Course> filtered = new ArrayList<>();
        for (Course c: mCourses) {
            if (UserCourses.getInstance().approvedCourses.containsKey(c.getCode())) {
                filtered.add(c);
            }
        }
        Collections.sort(filtered, new Course.ComparatorByName());
        return filtered;
    }

    public static List<Course> filterStudying(List<Course> mCourses) {
        List<Course> filtered = new ArrayList<>();
        for (Course c: mCourses) {
            if (UserCourses.getInstance().getStudyingCoursesCodes().contains(c.getCode())) {
                filtered.add(c);
            }
        }
        Collections.sort(filtered, new Course.ComparatorByName());
        return filtered;
    }

    public static List<Course> filterNotCoursed(List<Course> mCourses) {
        UserCourses ucs = UserCourses.getInstance();
        List<Course> filteredAv = new ArrayList<>();
        List<Course> filteredNotAv = new ArrayList<>();
        Collections.sort(mCourses, new Course.ComparatorByName());
        for (Course c: mCourses) {
            if (!ucs.approvedCourses.containsKey(c.getCode()) &&
                    !ucs.getStudyingCoursesCodes().contains(c.getCode())) {
                if (getInstance().isAvailable(c)) {
                    filteredAv.add(c);
                } else {
                    filteredNotAv.add(c);
                }
            }
        }
        filteredAv.addAll(filteredNotAv);
        return filteredAv;
    }

    public static List<Course> filterAvailable(List<Course> mCourses) {
        UserCourses ucs = UserCourses.getInstance();
        List<Course> filteredAv = new ArrayList<>();
        Collections.sort(mCourses, new Course.ComparatorByName());
        for (Course c: mCourses) {
            if (!ucs.approvedCourses.containsKey(c.getCode()) &&
                    !ucs.getStudyingCoursesCodes().contains(c.getCode())) {
                if (getInstance().isAvailable(c)) {
                    filteredAv.add(c);
                }
            }
        }
        return filteredAv;
    }

    public static List<Course> filterRequired(ArrayList<Course> mCourses) {
        List<Course> filtered= new ArrayList<>();
        for (Course c: mCourses) {
            if (c.isRequired()) {
                filtered.add(c);
            }
        }
        return filtered;
    }

    public static List<Course> filterOptional(ArrayList<Course> mCourses) {
        List<Course> filtered= new ArrayList<>();
        for (Course c: mCourses) {
            if (!c.isRequired()) {
                filtered.add(c);
            }
        }
        return filtered;
    }

    public static List<Course> filterFromBranch(ArrayList<Course> mCourses) {
        List<Course> filtered= new ArrayList<>();
        String branchCode = Branch.getFromSharedPrefs();
        for (Course c: mCourses) {
            if (c.isFromCurrentBranch(branchCode)) {
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

    public List<Course> getAll() {
        return new ArrayList<Course>(loadedCourses.values());
    }

    public boolean isReady() {
        return _ready;
    }

    public Course getCourse(String courseCode) {
        Course c = loadedCourses.get(courseCode);
        return c != null ? c : savedloadedCourses.get(courseCode);
    }

    public String getCourseName(String courseCode) {
        return getCourse(courseCode).getName();
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
        Log.d("Saving approved", gson.toJson(approvedCourses));
        Log.d("Saving studying", gson.toJson(studyingCourses));
        prefsEditor.commit();
    }

    public static UserCourses getFromSharedPrefs() {
        Context context = ActivityContext.get();
        if (context != null) {
            SharedPreferences mPrefs = context.getSharedPreferences(
                    context.getResources().getString(R.string.shared_prefs), MODE_PRIVATE);
            String key = context.getResources().getString(R.string.pref_user_courses);
            String jsonApproved = mPrefs.getString(key + "_approved", "");
            String jsonStudying = mPrefs.getString(key + "_studying", "");
            return new UserCourses(jsonApproved, jsonStudying);
        } else {
            return null;
        }
    }

    public List<Course> getLoadedCourses() {
        return loadedCoursesArray;
    }

    public void setCourses(List<Course> courses) {
        loadedCoursesArray = courses;
        for (Course c: courses) {
            addCourse(c.getCode(), c);
        }
    }

    public ArrayList<Course> getCoursesByCodes(List<String> correlatives) {
        ArrayList<Course> res = new ArrayList<>();
        for (String code: correlatives) {
            Course c = getCourse(code);
            if (c != null) {
                res.add(c);
            }
        }
        return res ;
    }

    public boolean isApproved(Course course) {
        return isApproved(course.getCode());
    }

    public boolean isApproved(String courseCode) {
        return approvedCourses.containsKey(courseCode)
                && approvedCourses.get(courseCode) != null
                && approvedCourses.get(courseCode) >= 0;
    }

    public boolean isFinalExamPending(Course course) {
        return approvedCourses.containsKey(course.getCode()) &&
                (approvedCourses.get(course.getCode()) == null ||
                approvedCourses.get(course.getCode()) < 0);
    }

    public boolean isStudying(Course course) {
        return getStudyingCoursesCodes().contains(course.getCode());
    }

    public List<String> getStudyingCoursesCodes() {
        String plan = Plan.getFromSharedPrefs();
        if (studyingCourses.get(plan) == null) {
            studyingCourses.put(plan, new ArrayList<String>());
        }
        return studyingCourses.get(plan);
    }

    public List<Course> getAvailableCourses() {
        return filterAvailable(getAll());
    }

    public List<Course> getStudyingCourses() {
        return filterStudying(getAll());
    }

    public List<Course> getAvailableAndStudyingCourses() {
        List<Course> result = getAvailableCourses();
        result.addAll(getStudyingCourses());
        Collections.sort(result, new Course.ComparatorByName());
        return result;
    }

}
