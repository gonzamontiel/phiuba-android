package mont.gonzalo.phiuba.api;

import java.util.List;

import mont.gonzalo.phiuba.model.Department;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Gonzalo Montiel on 10/10/16.
 */
public interface DepartmentAPI {
    @GET("/api/departments")
    void get(Callback<List<Department>> c);

    @GET("/api/department")
    void getOne(@Query("code") String code, Callback<Department> c);
}
