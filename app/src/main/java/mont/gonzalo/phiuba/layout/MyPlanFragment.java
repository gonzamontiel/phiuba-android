package mont.gonzalo.phiuba.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.UserCourses;

import static mont.gonzalo.phiuba.layout.MyPlanActivity.TAB_APPROVED_ID;
import static mont.gonzalo.phiuba.layout.MyPlanActivity.TAB_COMPLETE_ID;
import static mont.gonzalo.phiuba.layout.MyPlanActivity.TAB_NOT_COURSED_ID;
import static mont.gonzalo.phiuba.layout.MyPlanActivity.TAB_STUDYING_ID;

/**
 * Created by Gonzalo Montiel on 4/2/17.
 */
public class MyPlanFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<Course> mCourses;
    public MyPlanFragment() {}

    public static MyPlanFragment newInstance(int sectionNumber, List<Course> mCourses) {
        MyPlanFragment fragment = new MyPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setCourses(mCourses);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_plan, container, false);
        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.course_rv);
        final TextView  placeHolder = (TextView) rootView.findViewById(R.id.placeHolder);
        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        List<Course> tabCourses = getCoursesByState(sectionNumber);
        if (tabCourses.size() > 0) {
            rv.setAdapter(new CourseRecyclerViewAdapter(tabCourses,
                    (CoursesFragment.OnListFragmentInteractionListener) getActivity()));
            rv.setVisibility(View.VISIBLE);
            placeHolder.setVisibility(View.GONE);
        }

        return rootView;
    }

    public void setCourses(List<Course> mCourses) {
        this.mCourses = mCourses;
    }

    private List<Course> getCoursesByState(int state) {
        switch (state) {
            case TAB_APPROVED_ID:
                return UserCourses.filterApproved(mCourses);
            case TAB_STUDYING_ID:
                return UserCourses.filterStudying(mCourses);
            case TAB_NOT_COURSED_ID:
                return UserCourses.filterNotCoursed(mCourses);
            case TAB_COMPLETE_ID:
                return mCourses;
        }
        return new ArrayList<Course>();
    }
}
