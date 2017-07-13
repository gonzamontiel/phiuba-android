package mont.gonzalo.phiuba.api;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Observable;

import mont.gonzalo.phiuba.layout.ActivityContext;
import mont.gonzalo.phiuba.model.Cathedra;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.Department;
import mont.gonzalo.phiuba.model.Event;
import mont.gonzalo.phiuba.model.News;
import mont.gonzalo.phiuba.model.Plan;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by Gonzalo Montiel on 11/28/16.
 */

public class DataFetcher extends Observable {
    private static final String TEST_ROOT_URL = "http://10.0.2.2:3030";
    private static final String PROD_ROOT_IP = "192.168.0.5";
    private static final String TAG = "DataFetcher";
    private TestConnectionAPI testConnectionApi;
    private RestAdapter adapter;
    private CourseAPI courseApi;
    private CathedraAPI cathedraAPI;
    private DepartmentAPI departmentAPI;
    private NewsAPI newsApi;
    private EventAPI eventApi;
    private PlansAPI plansApi;
    private boolean isRunning;

    private static DataFetcher instance = null;

    public static DataFetcher getInstance() {
        String serverUrl;
//        if (BuildConfig.DEBUG) {
//            serverUrl = TEST_ROOT_URL;
//        } else {
            serverUrl = getUrlFromPreferences();
//        }
        if (instance == null) {
            instance = new DataFetcher(serverUrl);
        }
        return instance;
    }

    private DataFetcher(String serverUrl) {
        createServerApis(serverUrl);
    }

    public void updateServer() {
        createServerApis(getUrlFromPreferences());
    }

    private void createServerApis(String serverUrl) {
        Log.d(TAG, "Connecting to " + serverUrl);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        this.adapter = new RestAdapter.Builder()
                .setEndpoint(serverUrl)
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setConverter(new GsonConverter(gson))
                .build();
        this.courseApi = this.adapter.create(CourseAPI.class);
        this.cathedraAPI = this.adapter.create(CathedraAPI.class);
        this.departmentAPI = this.adapter.create(DepartmentAPI.class);
        this.newsApi = this.adapter.create(NewsAPI.class);
        this.eventApi = this.adapter.create(EventAPI.class);
        this.plansApi = this.adapter.create(PlansAPI.class);
        this.testConnectionApi = this.adapter.create(TestConnectionAPI.class);
        test();
    }

    public static String getUrlFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ActivityContext.get());
        String ip = prefs.getString("server_ip", PROD_ROOT_IP);
        if (!Patterns.IP_ADDRESS.matcher(ip).matches()) {
            ip = PROD_ROOT_IP;
        }
        return urlFromIp(ip);
    }

    private static String urlFromIp(String ip) {
        return "http://" + ip + ":3030";
    }

    public void getCourses(String planCode, Callback<List<Course>> c) {
        this.courseApi.get(planCode, c);
    }

    public List<Course> getCoursesSync(String planCode) {
        return this.courseApi.getSync(planCode);
    }

    public void searchCourses(String planCode, String plan, Callback<List<Course>> c) {
        this.courseApi.search(planCode, plan, c);
    }

    public void getCoursesByDepartment(String code, Callback<List<Course>> c) {
        this.courseApi.byDepartment(code, c);
    }

    public void getEvents(Callback<List<Event>> c) {
        this.eventApi.get(c);
    }

    public List<Event> searchEventsSync(String query) {
        return this.eventApi.searchSync(query);
    }

    public void searchEvents(String query, Callback<List<Event>> c) {
        this.eventApi.search(query, c);
    }

    public void getCathedras(String courseCode, Callback<List<Cathedra>> c) {
        this.cathedraAPI.get(courseCode, c);
    }

    public List<Cathedra> getCathedrasSync(String courseCode) {
        return this.cathedraAPI.getSync(courseCode);
    }

    public void getDepartments(Callback<List<Department>> c) {
        this.departmentAPI.get(c);
    }

    public void getDepartment(String code, Callback<Department> c) {
        this.departmentAPI.getOne(code, c);
    }

    public void getPlans(Callback<List<Plan>> callback) {
        this.plansApi.get(callback);
    }

    public void getNews(Callback<List<News>> callback) {
        this.newsApi.get(callback);
    }

    public void searchNews(String query, Callback<List<News>> callback) {
        this.newsApi.search(query, callback);
    }

    public void test() {
        final DataFetcher me = this;
        this.testConnectionApi.ping(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                me.setRunning(true);
                me.setChanged();
                me.notifyObservers();
            }

            @Override
            public void failure(RetrofitError error) {
                me.setRunning(false);
                me.setChanged();
                me.notifyObservers();
            }
        });
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
