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
    public void get(@Query("planCode") String plan, Callback<List<Course>> response);

    @GET("/api/courses")
    public void search(@Query("search") String query, @Query("planCode") String plan, Callback<List<Course>> response);
}
