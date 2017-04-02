package mont.gonzalo.phiuba.api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;

/**
 * Created by Gonzalo Montiel on 10/10/16.
 */
public interface TestConnectionAPI {
    @GET("/test")
    void ping(Callback<Response> callback);
}
