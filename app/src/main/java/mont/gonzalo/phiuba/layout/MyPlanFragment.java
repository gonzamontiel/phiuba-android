package mont.gonzalo.phiuba.layout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.UserCourses;

import static mont.gonzalo.phiuba.layout.MyPlanActivity.TAB_APPROVED_ID;
import static mont.gonzalo.phiuba.layout.MyPlanActivity.TAB_BRANCH_ID;
import static mont.gonzalo.phiuba.layout.MyPlanActivity.TAB_NOT_COURSED_ID;
import static mont.gonzalo.phiuba.layout.MyPlanActivity.TAB_OPT_ID;
import static mont.gonzalo.phiuba.layout.MyPlanActivity.TAB_REQ_ID;
import static mont.gonzalo.phiuba.layout.MyPlanActivity.TAB_STUDYING_ID;

/**
 * Created by Gonzalo Montiel on 4/2/17.
 */
public class MyPlanFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<Course> mCourses;
    private boolean isAccessible;

    public MyPlanFragment() {}

    public static MyPlanFragment newInstance(int sectionNumber, List<Course> mCourses) {
        MyPlanFragment fragment = new MyPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setCourses((ArrayList<Course>) mCourses);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mcourses", (Serializable) mCourses);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ActivityContext.get());
        isAccessible = prefs.getBoolean("accessibility_text", false);
        if (savedInstanceState != null) {
            mCourses = (ArrayList<Course>) savedInstanceState.getSerializable("mcourses");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_plan, container, false);
        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.course_rv);
        final TextView  placeHolderTitle = (TextView) rootView.findViewById(R.id.placeHolder_title);
        final TextView  placeHolder = (TextView) rootView.findViewById(R.id.placeHolder);
        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        List<Course> tabCourses = getCoursesByState(sectionNumber);
        if (tabCourses.size() > 0) {
            rv.setAdapter(new CoursesAdapter(tabCourses,
                    (CoursesFragment.OnListFragmentInteractionListener) getActivity(), isAccessible));
            rv.setVisibility(View.VISIBLE);
            placeHolder.setVisibility(View.GONE);
            placeHolderTitle.setVisibility(View.GONE);
        }

        return rootView;
    }

    public void setCourses(ArrayList<Course> mCourses) {
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
            case TAB_REQ_ID:
                return UserCourses.filterRequired(mCourses);
            case TAB_OPT_ID:
                return UserCourses.filterOptional(mCourses);
            case TAB_BRANCH_ID:
                return UserCourses.filterFromBranch(mCourses);
            default:
                return mCourses;
        }
    }
}
