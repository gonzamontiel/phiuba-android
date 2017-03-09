package mont.gonzalo.phiuba.api;

import java.util.List;

import mont.gonzalo.phiuba.model.Event;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Gonzalo Montiel on 10/10/16.
 */
public interface EventAPI {
    @GET("/api/events")
    public void get(Callback<List<Event>> response);

    @GET("/api/events")
    public void search(@Query("search") String query, Callback<List<Event>> response);
}
