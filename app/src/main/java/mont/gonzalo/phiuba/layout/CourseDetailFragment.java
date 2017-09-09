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

import java.io.Serializable;
import java.util.List;

import me.grantland.widget.AutofitHelper;
import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.api.DataFetcher;
import mont.gonzalo.phiuba.model.Cathedra;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.UserCourses;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CourseDetailFragment extends SearchableFragment implements Serializable {
    private static final String TAG = "CourseDetailFragment";
    private transient OnFragmentInteractionListener mListener;
    private transient OnListFragmentInteractionListener mListListener;
    private transient TextView nameTextView;
    private transient TextView credits;
    private transient ImageView deptoIcon;
    private transient RecyclerView cathedrasView;
    private Course mCourse;

    public CourseDetailFragment() {
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
    public void loadFromSerializedData(Serializable data) {
        if (data == null) return;
        this.mCourse = (Course) data;
        this.setListener((OnFragmentInteractionListener) getActivity());
        this.setListListener((OnListFragmentInteractionListener) getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_course_detail, container, false);
        final TextView schedulesTextView = (TextView) view.findViewById(R.id.schedulesText);
        final RecyclerView requirements = (RecyclerView) view.findViewById(R.id.requirements);
        requirements.setAdapter(new MiniCoursesAdapter(
                UserCourses.getInstance().getCoursesByCodes(mCourse.getCorrelatives()), (MiniCoursesAdapter.OnCorrelativeInteractionListener) getActivity()));

        credits = (TextView) view.findViewById(R.id.course_credits);
        nameTextView = (TextView) view.findViewById(R.id.course_name);
        deptoIcon = (ImageView) view.findViewById(R.id.coursed_depto_icon);
        deptoIcon.setImageResource(mCourse.getImageResource());

        AutofitHelper.create(nameTextView);
        nameTextView.setText(mCourse.getName());
        credits.setText("Créditos: " + mCourse.getCredits());
        cathedrasView = (RecyclerView) view.findViewById(R.id.cathedra_rv);

        DataFetcher.getInstance().getCathedras(mCourse.getCode(), new Callback<List<Cathedra>>() {
            @Override
            public void success(List<Cathedra> cathedras, Response response) {
                if (cathedras.size() > 0) {
                    cathedrasView.setAdapter(new CathedrasAdapter(cathedras, mCourse, mListListener));
                    mListListener = (OnListFragmentInteractionListener) getActivity();
                    registerForContextMenu(cathedrasView);
                } else {
                    schedulesTextView.setText(getActivity().getResources().getString(R.string.NO_SCHEDULES));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.getMessage());
            }
        });

        return view;
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
