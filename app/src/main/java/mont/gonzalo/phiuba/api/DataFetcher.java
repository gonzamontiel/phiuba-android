package mont.gonzalo.phiuba.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import mont.gonzalo.phiuba.model.Cathedra;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.Department;
import mont.gonzalo.phiuba.model.Event;
import mont.gonzalo.phiuba.model.News;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Gonzalo Montiel on 11/28/16.
 */

public class DataFetcher {
    // Tag for logging
    private static final String TAG = "DataFetcher";

    //Root URL of our web service

    public static final String ROOT_URL = "http://10.0.2.2:3030";
//    public static final String ROOT_URL = "http://192.168.10.132:3030";
    private RestAdapter adapter;
    private final CourseAPI courseApi;
    private final CathedraAPI cathedraAPI;
    private final DepartmentAPI departmentAPI;
    private final NewsAPI newsApi;
    private final EventAPI eventApi;

    private static DataFetcher instance = null;

    public static DataFetcher getInstance() {
        if (instance == null) {
            instance = new DataFetcher();
        }
        return instance;
    }

    private DataFetcher() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        this.adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
        this.courseApi = this.adapter.create(CourseAPI.class);
        this.cathedraAPI = this.adapter.create(CathedraAPI.class);
        this.departmentAPI = this.adapter.create(DepartmentAPI.class);
        this.newsApi = this.adapter.create(NewsAPI.class);
        this.eventApi = this.adapter.create(EventAPI.class);
    }

    public void getCourses(String planCode, Callback<List<Course>> c) {
        this.courseApi.get(planCode, c);
    }

    public void searchCourses(String planCode, String plan, Callback<List<Course>> c) {
        this.courseApi.search(planCode, plan, c);
    }

    public void getEvents(Callback<List<Event>> c) {
        this.eventApi.get(c);
    }

    public void searchEvents(String query, Callback<List<Event>> c) {
        this.eventApi.search(query, c);
    }

    public void getCathedras(String courseCode, Callback<List<Cathedra>> c) {
        this.cathedraAPI.get(courseCode, c);
    }

    public void getDepartments(Callback<List<Department>> c) {
        this.departmentAPI.get(c);
    }

    public void getNews(Callback<List<News>> callback) {
        this.newsApi.get(callback);
    }

    public void searchNews(String query, Callback<List<News>> callback) {
        this.newsApi.search(query, callback);
    }
}
