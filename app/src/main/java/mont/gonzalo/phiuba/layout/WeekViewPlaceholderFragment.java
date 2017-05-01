package mont.gonzalo.phiuba.layout;

import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
    private List<WeekViewEvent> events = new ArrayList<>();
    private TextView title;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void toggleLabels(View view) {
        if (view.isShown()) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int position = getArguments().getInt(ARG_SECTION_NUMBER);
        View rootView = inflater.inflate(R.layout.fragment_week_view, container, false);
        final TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        String message = loadEventsForPosition(position);
        textView.setText(LayoutHelper.fromHtml(message));

        title = (TextView) rootView.findViewById(R.id.section_title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            toggleLabels(textView);
            }
        });

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

    private String loadEventsForPosition(int position) {
        String message = "<html>";
        for (Cathedra c: CathedrasCombination.getInstance().getAtPosition(position)) {
            if (c != null) {
                message += buildHeaderMessage(c);
                events.addAll(c.toWeekEvents(""));
            }
        }
        message += "</html>";
        return message;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    private String buildHeaderMessage( Cathedra c) {
        int color = getResources().getColor(c.getColor(), null);
        String hexColor = "#" + Integer.toHexString(color).substring(2);
        String name = UserCourses.getInstance().getCourseName(c.getCourseCode());
        String colorStr = "<span style='color: " + hexColor + ";'> •••• </span>";
        return colorStr + name + " " + getString(R.string.weekview_with) + " <strong>" + c.getTeachers() + "</strong><br>";
    }
}
