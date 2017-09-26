package mont.gonzalo.phiuba.layout;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.api.DataFetcher;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.Department;
import mont.gonzalo.phiuba.model.User;
import mont.gonzalo.phiuba.model.UserCourses;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CoursesFragment extends SearchableFragment implements Serializable {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private transient OnListFragmentInteractionListener mListListener;
    private transient CoursesAdapter mAdapter;
    private transient RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CoursesFragment() {
    }

    public static CoursesFragment newInstance(int columnCount, OnListFragmentInteractionListener listener) {
        CoursesFragment fragment = new CoursesFragment();
        fragment.setListener(listener);
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mListListener = (OnListFragmentInteractionListener) getActivity();
            if (!UserCourses.getInstance().isReady()) {
                DataFetcher.getInstance().getCourses(User.get().getPlanCode(),new Callback<List<Course>>() {
                    @Override
                    public void success(List<Course> courses, Response response) {
                        if (courses.size() > 0) {
                            UserCourses.getInstance().setCourses(courses);
                            loadCoursesToAdapter(courses);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(DataFetcher.TAG, error.getMessage());
                    }
                });
            } else {
                List<Course> courses = UserCourses.getInstance().getLoadedCourses();
                loadCoursesToAdapter(courses);
            }
        }
        return view;
    }

    private void loadCoursesToAdapter(List<Course> courses) {
        mAdapter = new CoursesAdapter(courses, mListListener);
        recyclerView.setAdapter(mAdapter);
        mListListener = (OnListFragmentInteractionListener) getActivity();
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListListener = null;
    }

    public void setListener(OnListFragmentInteractionListener listener) {
        this.mListListener = listener;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        Course course;
        int opcion = 0;
        try {
            position = ((CoursesAdapter)mAdapter).getPosition();
            course = mAdapter.getCourse(position);
        } catch (Exception e) {
            Log.d("CoursesFragment", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.string.course_context_department:
                DataFetcher.getInstance().getDepartment(course.getDepCode(), new Callback<Department>() {
                    @Override
                    public void success(Department department, Response response) {
                        MainActivity act = (MainActivity) getActivity();
                        act.showDepartment(department);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Snackbar.make(getView(), "Hubo un error al encontrar el departamento (no tenemos las llaves).", Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
                break;
            case R.string.course_context_schedule:
                break;
        }
        if (opcion != 0) {
            Snackbar.make(getView(), course.getName() + " fue agregada como " + getContext().getResources().getString(opcion), Snackbar.LENGTH_LONG)
                    .show();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void updateResults(String query) {
        DataFetcher.getInstance().searchCourses(query, User.get().getPlanCode(), new Callback<List<Course>>() {
            @Override
            public void success(List<Course> courses, Response response) {
                if (mAdapter != null) {
                    mAdapter.updateItems(courses);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d(DataFetcher.TAG, error.getMessage());
            }
        });
    }

    @Override
    public void reset() {
        DataFetcher.getInstance().getCourses(User.get().getPlanCode(), new Callback<List<Course>>() {
            @Override
            public void success(List<Course> courses, Response response) {
                recyclerView.setAdapter(mAdapter = new CoursesAdapter(courses, mListListener));
            }

            @Override
            public void failure(RetrofitError error) {Log.d(DataFetcher.TAG, error.getMessage());}
        });
    }

    @Override
    public SearchableFragment getResultsFragment() {
        return this;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Course item);
    }
}
