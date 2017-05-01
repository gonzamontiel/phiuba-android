package mont.gonzalo.phiuba.layout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.CathedrasCombination;
import mont.gonzalo.phiuba.model.UserCourses;

public class WeekViewActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ProgressBar progressBar;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        setTitle("Mi semana");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        ActivityContext.set(this);
        rebuildTree();
    }

    private void rebuildTree() {
        CathedrasCombination cc = CathedrasCombination.getInstance();
        CombinationsTreeBuilder builder = new CombinationsTreeBuilder();
        builder.execute(cc);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        // Hide after 5 seconds
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                }, 5000);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public void setSectionsAdapter() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return WeekViewPlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return CathedrasCombination.getInstance().getCombinationCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Combiación número " + String.valueOf(position);
        }
    }

    private class CombinationsTreeBuilder extends AsyncTask<CathedrasCombination, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        protected String doInBackground(CathedrasCombination[] comb) {
            UserCourses.getInstanceSync();
            comb[0].loadCathedrasSync();
            comb[0].buildTree();
            comb[0].print();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hideProgressBar();
            setSectionsAdapter();
        }
    }
}
