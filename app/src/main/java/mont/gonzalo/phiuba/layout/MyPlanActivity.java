package mont.gonzalo.phiuba.layout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.api.DataFetcher;
import mont.gonzalo.phiuba.model.Cathedra;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.User;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.ContentValues.TAG;

public class MyPlanActivity extends AppCompatActivity
        implements CoursesFragment.OnListFragmentInteractionListener,
        CourseDetailFragment.OnFragmentInteractionListener, CourseDetailFragment.OnListFragmentInteractionListener {
    private transient SectionsPagerAdapter mSectionsPagerAdapter;
    private transient ViewPager mViewPager;
    private transient TabLayout tabs;

    public static final int TAB_APPROVED_ID = 0;
    public static final int TAB_STUDYING_ID = 1;
    public static final int TAB_NOT_COURSED_ID = 2;
    public static final int TAB_COMPLETE_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.my_plan_title));

        final Activity app = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(app)
                        .setMessage(getString(R.string.myplan_confirm_import_text))
                        .setTitle(getString(R.string.myplan_confirm_import_title))
                        .setPositiveButton("Ok, no drama!", new DialogInterface.OnClickListener() {
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

        DataFetcher.getInstance().getCourses(User.get().getPlanCode(), new Callback<List<Course>>() {
            @Override
            public void success(List<Course> courses, Response response) {
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), courses);
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setOffscreenPageLimit(0);
                mViewPager.setAdapter(mSectionsPagerAdapter);

                tabs = (TabLayout) findViewById(R.id.tabs);
                tabs.setupWithViewPager(mViewPager);
//                mSectionsPagerAdapter.setCourses(courses);
//                mSectionsPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.getMessage());
            }
        });

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
