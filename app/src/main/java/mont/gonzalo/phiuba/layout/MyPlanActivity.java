package mont.gonzalo.phiuba.layout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.Cathedra;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.User;
import mont.gonzalo.phiuba.model.UserCourses;

public class MyPlanActivity extends AppCompatActivity
        implements CoursesFragment.OnListFragmentInteractionListener,
        CourseDetailFragment.OnFragmentInteractionListener, CourseDetailFragment.OnListFragmentInteractionListener, CircleDisplay.SelectionListener, Observer {
    private transient SectionsPagerAdapter mSectionsPagerAdapter;
    private transient ViewPager mViewPager;
    private transient TabLayout tabs;

    public static final int TAB_APPROVED_ID = 0;
    public static final int TAB_STUDYING_ID = 1;
    public static final int TAB_NOT_COURSED_ID = 2;
    public static final int TAB_COMPLETE_ID = 3;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plan);
        setTitle(getString(R.string.my_plan_title));
        ActivityContext.set(this);
        initializaToolbar();
        loadFloatingButton();
        UserCourses.getInstance().addObserver(this);
        if (UserCourses.getInstance().isReady()) {
            initializeCourses();
            loadStatistics();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        UserCourses.getInstance().saveToSharedPrefs();
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void initializeCourses() {
        if (mSectionsPagerAdapter == null) {
            mSectionsPagerAdapter = new SectionsPagerAdapter(
                    getSupportFragmentManager(),
                    UserCourses.getInstance().getAll());
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            tabs = (TabLayout) findViewById(R.id.tabs);
            tabs.setupWithViewPager(mViewPager);
        }
    }

    private void initializaToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void loadFloatingButton() {
        final Activity app = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(app)
                        .setMessage(getString(R.string.myplan_confirm_import_text))
                        .setTitle(getString(R.string.myplan_confirm_import_title))
                        .setPositiveButton("Importar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(app, ImportSIUActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No, gracias!", null)
                        .show();
            }
        });
    }

    private void loadStatistics() {
        LinearLayout statistics = (LinearLayout) findViewById(R.id.statistics);
        double avg = UserCourses.getInstance().getAverageCalification();
        double count = UserCourses.getInstance().getApprovedCount();
        TextView awardNumber = (TextView) findViewById(R.id.average);
        TextView countTv = (TextView) findViewById(R.id.count);
        countTv.setText(String.valueOf(count));
        awardNumber.setText(String.valueOf(Math.round(avg * 100.0) / 100.0));
        initializeCircleOfCompletion();
        statistics.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initializeCircleOfCompletion() {
        double percent = UserCourses.getInstance().getCredits() / User.get().getPlan().getCredits();
        CircleDisplay circleDisplay = (CircleDisplay) findViewById(R.id.circleDisplay);
        circleDisplay.setAnimDuration(1000);
        circleDisplay.setValueWidthPercent(55f);
        circleDisplay.setTextSize(14f);
        circleDisplay.setDrawText(true);
        circleDisplay.setColor(getResources().getColor(R.color.accent, null));
        circleDisplay.setDrawInnerCircle(true);
        circleDisplay.setFormatDigits(1);
        circleDisplay.setTouchEnabled(true);
        circleDisplay.setSelectionListener(this);
        circleDisplay.setStepSize(0.5f);
        circleDisplay.setUnit("%");
        circleDisplay.showValue(Math.round(percent * 100), 100f, true);
    }

    public void hideProgressBar() {
        ProgressBar p = (ProgressBar) findViewById(R.id.progress);
        p.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Course course) {
    }

    @Override
    public void onFragmentInteraction(Course course) {

    }

    @Override
    public void onListFragmentInteraction(Cathedra item, String courseName, String teachers) {

    }

    @Override
    public void onSelectionUpdate(float val, float maxval) {

    }

    @Override
    public void onValueSelected(float val, float maxval) {

    }

    @Override
    public void update(Observable o, Object arg) {
        hideProgressBar();
        initializeCourses();
        loadStatistics();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<Course> mCourses;

        public SectionsPagerAdapter(FragmentManager fm, List<Course> courses) {
            super(fm);
            mCourses = courses;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("Creating fragment. courses SIZE", String.valueOf(mCourses.size()));
            return MyPlanFragment.newInstance(position, mCourses);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case TAB_APPROVED_ID:
                    return getString(R.string.myplan_tab_approved);
                case TAB_STUDYING_ID:
                    return getString(R.string.myplan_tab_studying);
                case TAB_NOT_COURSED_ID:
                    return getString(R.string.myplan_tab_not_coursed);
                case TAB_COMPLETE_ID:
                    return getString(R.string.myplan_tab_complete);
            }
            return null;
        }

        public void setCourses(List<Course> courses) {
            this.mCourses= courses;
        }
    }
}
