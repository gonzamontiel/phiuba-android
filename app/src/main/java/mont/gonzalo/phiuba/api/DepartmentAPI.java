package mont.gonzalo.phiuba.api;

import java.util.List;

import mont.gonzalo.phiuba.model.Department;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Gonzalo Montiel on 10/10/16.
 */
public interface DepartmentAPI {
    @GET("/api/departments")
    void get(Callback<List<Department>> c);
}
