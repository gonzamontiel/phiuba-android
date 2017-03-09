package mont.gonzalo.phiuba.api;

import java.util.List;

import mont.gonzalo.phiuba.model.News;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Gonzalo Montiel on 10/10/16.
 */
public interface NewsAPI {
    @GET("/api/news")
    public void get(Callback<List<News>> response);

    @GET("/api/news")
    public void search(@Query("search") String query, Callback<List<News>> response);
}
