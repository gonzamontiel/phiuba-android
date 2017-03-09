package mont.gonzalo.phiuba.layout;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.api.DataFetcher;
import mont.gonzalo.phiuba.model.Department;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.ContentValues.TAG;

public class DepartmentsFragment extends SearchableFragment implements Serializable {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private transient RecyclerView.Adapter mAdapter;
    private transient DataFetcher mFetcher;
    private transient RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DepartmentsFragment() {
    }

    public static DepartmentsFragment newInstance(int columnCount, OnListFragmentInteractionListener listener) {
        DepartmentsFragment fragment = new DepartmentsFragment();
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
        View view = inflater.inflate(R.layout.fragment_department_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            mListener = (OnListFragmentInteractionListener) getActivity();

            DataFetcher.getInstance().getDepartments(new Callback<List<Department>>() {
                @Override
                public void success(List<Department> departments, Response response) {
                    if (departments.size() > 0) {
                        mAdapter = new DepartmentRecyclerViewAdapter(departments, mListener);
                        recyclerView.setAdapter(mAdapter);
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, error.getMessage());
                }
            });
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setListener(OnListFragmentInteractionListener listener) {
        this.mListener = listener;
    }

    @Override
    public void updateResults(String query) {
        DataFetcher.getInstance().getDepartments(new Callback<List<Department>>() {
            @Override
            public void success(List<Department> departments, Response response) {
                if (departments.size() > 0) {
                    mAdapter = new DepartmentRecyclerViewAdapter(departments, mListener);
                    recyclerView.setAdapter(mAdapter);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.getMessage());
            }
        });
    }

    @Override
    public void reset() {
        DataFetcher.getInstance().getDepartments(new Callback<List<Department>>() {
            @Override
            public void success(List<Department> departments, Response response) {
                mAdapter = new DepartmentRecyclerViewAdapter(departments, mListener);
                recyclerView.setAdapter(mAdapter);
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.getMessage());
            }
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
        void onListFragmentInteraction(Department item);
    }
}
