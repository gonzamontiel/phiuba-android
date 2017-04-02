package mont.gonzalo.phiuba.api;

import java.util.List;

import mont.gonzalo.phiuba.model.Plan;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Gonzalo Montiel on 10/10/16.
 */
public interface PlansAPI {
    @GET("/api/plans")
    public void get(Callback<List<Plan>> response);
}
