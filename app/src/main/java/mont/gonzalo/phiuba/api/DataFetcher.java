package mont.gonzalo.phiuba.api;

import java.util.List;

import mont.gonzalo.phiuba.model.Cathedra;
import mont.gonzalo.phiuba.model.Course;
import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by gonzalo on 11/28/16.
 */

public class DataFetcher {
    // Tag for logging
    private static final String TAG = "DataFetcher";

    //Root URL of our web service
    public static final String ROOT_URL = "http://192.168.0.5:3030";

    private RestAdapter adapter;
    private CourseAPI courseApi;
    private CathedraAPI cathedraAPI;

    public DataFetcher() {
        this.adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        this.courseApi = this.adapter.create(CourseAPI.class);
        this.cathedraAPI = this.adapter.create(CathedraAPI.class);
    }

    public void getCourses(String planCode, Callback<List<Course>> c) {
        this.courseApi.getCourses(planCode, c);
    }

    public void searchCourses(String planCode, String plan, Callback<List<Course>> c) {
        this.courseApi.searchCourses(planCode, plan, c);
    }

    public void getCathedras(String courseCode, Callback<List<Cathedra>> c) {
        this.cathedraAPI.get(courseCode, c);
    }
}
