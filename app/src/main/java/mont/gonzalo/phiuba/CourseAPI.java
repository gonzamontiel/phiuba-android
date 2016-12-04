package mont.gonzalo.phiuba;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by gonzalo on 10/10/16.
 */
public interface CourseAPI {
    @GET("/api/get_courses")
    public void getCourses(@Query("planCode") String plan, Callback<List<Course>> response);

    @GET("/api/search_courses")
    public void searchCourses(@Query("search") String query, @Query("planCode") String plan, Callback<List<Course>> response);
}
