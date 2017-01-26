package mont.gonzalo.phiuba.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.Course;

public class CourseDetailFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public CourseDetailFragment() {
        // Required empty public constructor
    }

    public static CourseDetailFragment newInstance(OnFragmentInteractionListener listener) {
        CourseDetailFragment fragment = new CourseDetailFragment();
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_detail, container, false);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Course course);
    }
}
