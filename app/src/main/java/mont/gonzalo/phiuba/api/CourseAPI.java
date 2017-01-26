package mont.gonzalo.phiuba.api;

import java.util.List;

import mont.gonzalo.phiuba.model.Course;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by gonzalo on 10/10/16.
 */
public interface CourseAPI {
    @GET("/api/get_courses")
    public void getCourses(@Query("planCode") String plan, Callback<List<Course>> response);

    @GET("/api/search_courses")
    public void searchCourses(@Query("search") String query, @Query("planCode") String plan, Callback<List<Course>> response);

    @POST("/api/find_courses")
    public void findCoursesByCode(@Body List<String> codes, Callback<List<Course>> response);
}
