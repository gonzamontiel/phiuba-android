package mont.gonzalo.phiuba.layout;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.SettingsActivity;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.Plan;
import mont.gonzalo.phiuba.model.User;
import mont.gonzalo.phiuba.model.UserCourses;

public class MyPlanActivity extends AppCompatActivity
        implements CoursesFragment.OnListFragmentInteractionListener, SharedPreferences.OnSharedPreferenceChangeListener, Observer {
    private transient SectionsPagerAdapter mSectionsPagerAdapter;
    private transient ViewPager mViewPager;
    private transient TabLayout tabs;

    public static final int TAB_APPROVED_ID = 0;
    public static final int TAB_STUDYING_ID = 1;
    public static final int TAB_NOT_COURSED_ID = 2;
    public static final int TAB_REQ_ID= 3;
    public static final int TAB_OPT_ID = 4;
    public static final int TAB_BRANCH_ID = 5;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plan);
        ActivityContext.set(this);
        initializaToolbar();
        loadFloatingButton();
        UserCourses.getInstance().addObserver(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        init();
    }

    private void init() {
        setTitle(Plan.byCode(Plan.getFromSharedPrefs()).getShortName());
        if (UserCourses.getInstance().isReady()) {
            initializeCourses();
            loadHeader();
            loadHeaderText();
        }
    }

    private void loadHeaderText() {
        TextView header = (TextView) findViewById(R.id.header);
        header.setText(getString(R.string.my_plan_title));
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

    private void loadHeader() {
        LinearLayout statistics = (LinearLayout) findViewById(R.id.statistics);
        double avg = UserCourses.getInstance().getAverageCalification();
        int count = UserCourses.getInstance().getApprovedCount();
        int totalCredits = UserCourses.getInstance().getCredits();
        TextView awardNumber = (TextView) findViewById(R.id.average);
        TextView countTv = (TextView) findViewById(R.id.count);
        TextView creditsTv = (TextView) findViewById(R.id.credits);
        countTv.setText(String.valueOf(count));
        creditsTv.setText(String.valueOf(totalCredits));
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
        circleDisplay.setStepSize(0.5f);
        circleDisplay.setTouchEnabled(false);
        circleDisplay.setUnit("%");
        circleDisplay.showValue(Math.round(percent * 100), 100f, true);
        circleDisplay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Dialog d = new MyPlanStatisticsDialog(v.getContext());
                d.show();
                return false;
            }
        });
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
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.MyPlanPreferenceFragment.class.getName() );
            intent.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_stats) {
            Dialog d = new MyPlanStatisticsDialog(this);
            d.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Course course) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.SPECIFIC_FRAGMENT, CourseDetailFragment.class);
        intent.putExtra(MainActivity.SPECIFIC_FRAGMENT_DATA, course);
        startActivity(intent);
    }

    @Override
    public void update(Observable o, Object arg) {
        hideProgressBar();
        initializeCourses();
        loadHeader();
        if (mSectionsPagerAdapter != null) {
            mSectionsPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("pref_plan") || key.equals("pref_branch") || key.equals("pref_tesis")) {
            UserCourses.getInstance().updateCourses();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<Course> mCourses;

        public SectionsPagerAdapter(FragmentManager fm, List<Course> courses) {
            super(fm);
            mCourses = courses;
        }

        @Override
        public Fragment getItem(int position) {
            return MyPlanFragment.newInstance(position, mCourses);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 6;
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
                case TAB_REQ_ID:
                    return getString(R.string.myplan_tab_required);
                case TAB_OPT_ID:
                    return getString(R.string.myplan_tab_optative);
                case TAB_BRANCH_ID:
                    return getString(R.string.myplan_tab_branch);
            }
            return null;
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            try{
                super.finishUpdate(container);
            } catch (NullPointerException nullPointerException){
                System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
            }
        }
    }
}
