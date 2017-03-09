package mont.gonzalo.phiuba.layout;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.grantland.widget.AutofitHelper;
import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.api.DataFetcher;
import mont.gonzalo.phiuba.model.Cathedra;
import mont.gonzalo.phiuba.model.Course;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CourseDetailFragment extends SearchableFragment {
    private static final String TAG = "CourseDetailFragment";
    private OnFragmentInteractionListener mListener;
    private OnListFragmentInteractionListener mListListener;
    private Course mCourse;
    private TextView nameTextView;
    private ImageView deptoIcon;
    private RecyclerView cathedrasView;

    public CourseDetailFragment() {
        // Required empty public constructor
    }

    public static CourseDetailFragment newInstance(OnFragmentInteractionListener listener,
                                                   OnListFragmentInteractionListener mListListener,
                                                   Course course) {
        CourseDetailFragment fragment = new CourseDetailFragment();
        fragment.setListener(listener);
        fragment.setListListener(mListListener);
        fragment.setCourse(course);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_course_detail, container, false);
        nameTextView = (TextView) view.findViewById(R.id.course_name);
        deptoIcon = (ImageView) view.findViewById(R.id.coursed_depto_con);

        deptoIcon.setImageResource(mCourse.getImageResource());

        AutofitHelper.create(nameTextView);
        nameTextView.setText(mCourse.getName());

        cathedrasView = (RecyclerView) view.findViewById(R.id.cathedra_rv);

        DataFetcher.getInstance().getCathedras(mCourse.getCode(), new Callback<List<Cathedra>>() {
            @Override
            public void success(List<Cathedra> cathedras, Response response) {
                if (cathedras.size() > 0) {
                    cathedrasView.setAdapter(new CathedrasRecyclerViewAdapter(cathedras, mCourse, mListListener));
                    mListListener = (OnListFragmentInteractionListener) getActivity();
                    registerForContextMenu(cathedrasView);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.getMessage());
            }
        });

        return view;
    }

    public void onButtonPressed(Course course) {
        if (mListener != null) {
            mListener.onFragmentInteraction(course);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setListener(OnFragmentInteractionListener listener) {
        this.mListener = listener;
    }

    public void setListListener(OnListFragmentInteractionListener listListener) {
        this.mListListener = listListener;
    }

    public void setCourse(Course course) {
        this.mCourse = course;
    }

    @Override
    public void updateResults(String query) {}

    @Override
    public void reset() {}

    @Override
    public SearchableFragment getResultsFragment() {
        return CoursesFragment.newInstance(1, (CoursesFragment.OnListFragmentInteractionListener) getActivity());
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Course course);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Cathedra item, String courseName, String teachers);
    }
}
