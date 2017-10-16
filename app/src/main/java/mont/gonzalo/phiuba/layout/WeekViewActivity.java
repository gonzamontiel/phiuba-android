package mont.gonzalo.phiuba.layout;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.SettingsActivity;
import mont.gonzalo.phiuba.model.CathedrasCombination;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.UserCourses;

public class WeekViewActivity extends AppCompatActivity implements CoursesFragment.OnListFragmentInteractionListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ProgressBar progressBar;
    private FloatingActionButton addButton;
    private ViewPager mViewPager;
    private ArrayList<Course> coursesToAdd;
    private ArrayList<Course> coursesToRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        setTitle("Mi semana");

        coursesToAdd = new ArrayList<>();
        coursesToRemove = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        initFab();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        ActivityContext.set(this);
        rebuildTree();
    }

    private void initFab() {
        addButton = (FloatingActionButton) findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCoursesDialog();
            }
        });
    }

    private void openCoursesDialog() {
        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.simple_courses, null);
        RecyclerView coursesList = (RecyclerView) view.findViewById(R.id.simple_courses_rv);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_selected);
        WeekViewCoursesAdapter coursesAdapter = new WeekViewCoursesAdapter(
                UserCourses.getInstance().getAvailableAndStudyingCourses(), this);
        coursesList.setLayoutManager(new LinearLayoutManager(this));
        coursesList.setAdapter(coursesAdapter);
        dialog.setContentView(view);
        dialog.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Course c : coursesToRemove) {
                    UserCourses.getInstance().removeCourse(c);
                }
                for (Course c : coursesToAdd) {
                    UserCourses.getInstance().addStudying(c);
                }
                rebuildTree();
                dialog.dismiss();
                coursesToAdd.clear();
                coursesToRemove.clear();
            }
        });
    }

    private void rebuildTree() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        CathedrasCombination cc = CathedrasCombination.getInstance();
        CombinationsTreeBuilder builder = new CombinationsTreeBuilder();
        builder.setAvoidCollisions(prefs.getBoolean("weekview_hide_collisions", true));
        builder.execute(cc);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        // Hide after 30 seconds
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                }, 30000);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public void setSectionsAdapter() {
        TextView v = (TextView) findViewById(R.id.nocomb);
        v.setVisibility(View.GONE);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private void setNoCombinationsPlaceHolder() {
        TextView v = (TextView) findViewById(R.id.nocomb);
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_week_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.WeekViewPreferenceFragment.class.getName() );
            intent.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Course item) {
        if (UserCourses.getInstance().isStudying(item)) {
            this.coursesToRemove.add(item);
        } else {
            this.coursesToAdd.add(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSectionsPagerAdapter != null) {
            mSectionsPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("weekview_hide_collisions") || key.equals("weekview_auto_hide_cathedras")) {
            recreate();
        }
    }

    private void showFailingCourses() {
        ArrayList failing = CathedrasCombination.getInstance().getFailingCourses();
        if (!failing.isEmpty()) {
            Toast.makeText(this, "Las siguientes materias fueron excluidas pues no tienen información de cátedras disponible: " + failing, Toast.LENGTH_LONG).show();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return WeekViewPlaceholderFragment.newInstance(position + 1, getCount());
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return CathedrasCombination.getInstance().getCombinationCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Combinación " + String.valueOf(position) + " de " + getCount();
        }
    }

    private class CombinationsTreeBuilder extends AsyncTask<CathedrasCombination, Integer, String> {
        private boolean avoidCollisions;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        protected String doInBackground(CathedrasCombination[] comb) {
            UserCourses.getInstanceSync();
            comb[0].loadCathedrasSync();
            comb[0].buildTree();
            if (avoidCollisions) {
                comb[0].removeCollisions();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hideProgressBar();
            if (CathedrasCombination.getInstance().getCombinationCount() > 0) {
                setSectionsAdapter();
                showFailingCourses();
            } else {
                setNoCombinationsPlaceHolder();
            }
        }

        public void setAvoidCollisions(boolean avoidCollisions) {
            this.avoidCollisions = avoidCollisions;
        }
    }
}
