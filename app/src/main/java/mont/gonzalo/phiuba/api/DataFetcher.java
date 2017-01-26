package mont.gonzalo.phiuba.api;

import java.util.List;

import mont.gonzalo.phiuba.model.Course;
import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by gonzalo on 11/28/16.
 */

public class DataFetcher {
    // Tag for logging
    private static final String TAG = "Fetcher";

    //Root URL of our web service
    public static final String ROOT_URL = "http://10.0.2.2:3030";

    private RestAdapter adapter;
    private CourseAPI courseApi;

    public DataFetcher() {
        this.adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        this.courseApi = this.adapter.create(CourseAPI.class);
    }

    public void getCourses(String planCode, Callback<List<Course>> c) {
        this.courseApi.getCourses(planCode, c);
    }

    public void searchCourses(String planCode, String plan, Callback<List<Course>> c) {
        this.courseApi.searchCourses(planCode, plan, c);
    }

    public void findSpecificCourses(List<String> codes, Callback<List<Course>> c) {
        this.courseApi.findCoursesByCode(codes, c);
    }
}
