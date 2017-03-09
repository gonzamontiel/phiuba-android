package mont.gonzalo.phiuba.api;

import java.util.List;

import mont.gonzalo.phiuba.model.Cathedra;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Gonzalo Montiel on 10/10/16.
 */
public interface CathedraAPI {
    @GET("/api/cathedras")
    void get(@Query("courseCode") String courseCode, Callback<List<Cathedra>> c);
}
