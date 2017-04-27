package mont.gonzalo.phiuba.layout;

import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import mont.gonzalo.phiuba.model.Cathedra;
import mont.gonzalo.phiuba.model.CathedrasCombination;
import mont.gonzalo.phiuba.model.CustomDateTimeInterpreter;
import mont.gonzalo.phiuba.model.ScheduleSlot;
import mont.gonzalo.phiuba.model.UserCourses;


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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int position = getArguments().getInt(ARG_SECTION_NUMBER);
        View rootView = inflater.inflate(R.layout.fragment_week_view, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        final List<WeekViewEvent> events = new ArrayList<>();
        String message = buildMultilineHeader(position, events);
        textView.setText(LayoutHelper.fromHtml(message));

        WeekView weekView = (WeekView) rootView.findViewById(R.id.weekView);
        weekView.setDateTimeInterpreter(new CustomDateTimeInterpreter());
        weekView.goToDate(ScheduleSlot.getUniqueWeekDate());
        weekView.goToHour(12);
        weekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {

            }
        });

        weekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
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

    @NonNull
    private String buildMultilineHeader(int position, List<WeekViewEvent> events) {
        String message = "<html>";
        for (Cathedra c: CathedrasCombination.getInstance().getAtPosition(position)) {
            if (c != null) {
                int color = getResources().getColor(c.getColor(), null);
                String hexColor = "#" + Integer.toHexString(color).substring(2);
                Log.d("asd", hexColor);
                String name = UserCourses.getInstance().getCourseName(c.getCourseCode());
                String colorStr = "<span style='color: " + hexColor + ";'> •••• </span>";
                message += colorStr;
                message +=  name + " con " + "<strong>" + c.getTeachers() + "</strong><br>";
                events.addAll(c.toWeekEvents(c.getCourseCode()));
            }
        }
        message += "</html>";
        return message;
    }
}
