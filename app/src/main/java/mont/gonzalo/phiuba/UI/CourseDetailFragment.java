package mont.gonzalo.phiuba.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class CourseDetailFragment extends Fragment {
    private static final String TAG = "CourseDetailFragment";
    private OnFragmentInteractionListener mListener;
    private OnListFragmentInteractionListener mListListener;
    private Course mCourse;
    private DataFetcher mFetcher;
    private TextView deptoTextView;
    private TextView schedulesTextView;
    private TextView nameTextView;
    private TextView teachersTextView;
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
        mFetcher = new DataFetcher();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_course_detail, container, false);
        nameTextView = (TextView) view.findViewById(R.id.coursed_name);
        deptoTextView = (TextView) view.findViewById(R.id.coursed_depto);
        teachersTextView = (TextView) view.findViewById(R.id.coursed_teachers);
        deptoIcon = (ImageView) view.findViewById(R.id.coursed_depto_con);
        schedulesTextView = (TextView) view.findViewById(R.id.coursed_schedule);

        deptoTextView.setText(getResources().getString(R.string.course_detail_department) + " " + mCourse.getDepto());
        deptoIcon.setImageResource(mCourse.getImageResource());

        AutofitHelper.create(nameTextView);
        nameTextView.setText(mCourse.getName());

        cathedrasView = (RecyclerView) view.findViewById(R.id.cathedra_rv);

        mFetcher.getCathedras(mCourse.getCode(), new Callback<List<Cathedra>>() {
            @Override
            public void success(List<Cathedra> cathedras, Response response) {
                Log.d(TAG, String.valueOf(cathedras));
                if (cathedras.size() == 1) {
                    teachersTextView.setText(cathedras.get(0).getTeachers());
                    teachersTextView.setVisibility(View.VISIBLE);
                } else if (cathedras.size() > 1) {
                    schedulesTextView.setVisibility(View.VISIBLE);
                    cathedrasView.setAdapter(new CathedrasRecyclerViewAdapter(cathedras, mListListener));
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Course course);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Cathedra item);
    }
}
