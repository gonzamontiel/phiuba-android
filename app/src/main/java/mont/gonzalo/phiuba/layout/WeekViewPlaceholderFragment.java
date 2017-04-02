package mont.gonzalo.phiuba.layout;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.List;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.CustomDateTimeInterpreter;
import mont.gonzalo.phiuba.model.ScheduleSlot;


/**
 * A placeholder fragment containing a simple view.
 */
class WeekViewPlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public WeekViewPlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WeekViewPlaceholderFragment newInstance(int sectionNumber) {
        WeekViewPlaceholderFragment fragment = new WeekViewPlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_week_view, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        WeekView weekView = (WeekView) rootView.findViewById(R.id.weekView);
        weekView.setDateTimeInterpreter(new CustomDateTimeInterpreter());

        weekView.goToDate(ScheduleSlot.getUniqueWeekDate());
        weekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
            }
        });

        weekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
                events.addAll(ScheduleSlot.getSamples());
                return events;
            }
        });

        weekView.setEventLongPressListener(new WeekView.EventLongPressListener() {
            @Override
            public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

            }
        });

        return rootView;
    }
}
