package mont.gonzalo.phiuba.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mont.gonzalo.phiuba.R;

/**
 * Created by Gonzalo Montiel on 4/2/17.
 */
public class MyPlanFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public MyPlanFragment() {}

    public static mont.gonzalo.phiuba.layout.MyPlanFragment newInstance(int sectionNumber) {
        mont.gonzalo.phiuba.layout.MyPlanFragment fragment = new mont.gonzalo.phiuba.layout.MyPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_plan, container, false);


        return rootView;
    }
}
