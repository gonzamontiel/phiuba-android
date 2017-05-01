package mont.gonzalo.phiuba.api;

import java.util.List;

import mont.gonzalo.phiuba.model.Course;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Gonzalo Montiel on 10/10/16.
 */
public interface CourseAPI {
    @GET("/api/courses")
    void get(@Query("planCode") String plan, Callback<List<Course>> response);

    @GET("/api/courses")
    List<Course> getSync(@Query("planCode") String plan);

    @GET("/api/courses")
    void search(@Query("search") String query, @Query("planCode") String plan, Callback<List<Course>> response);

    @GET("/api/courses")
    void byDepartment(@Query("depCode") String depCode, Callback<List<Course>> response);
}
