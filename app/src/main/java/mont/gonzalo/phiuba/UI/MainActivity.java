package mont.gonzalo.phiuba.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.Cathedra;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.News;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CoursesFragment.OnListFragmentInteractionListener, NewsFragment.OnListFragmentInteractionListener, CourseDetailFragment.OnFragmentInteractionListener, CourseDetailFragment.OnListFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ACTIVE_FRAGMENT = "active_fragment";
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentFragment = null;
        Class fragmentClass = CoursesFragment.class;
        if (savedInstanceState != null) {
            Class savedActiveFragment = (Class) savedInstanceState.get(ACTIVE_FRAGMENT);
            fragmentClass = savedActiveFragment;
        }
        try {
            currentFragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            currentFragment = CoursesFragment.newInstance(1, this);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, currentFragment).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Envia sugerencias!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ACTIVE_FRAGMENT, currentFragment.getClass());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_courses) {
            Log.d(TAG, "Courses clicked!");
            currentFragment = CoursesFragment.newInstance(1, this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.flContent, currentFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_events) {

        } else if (id == R.id.nav_news) {
            Log.d(TAG, "News clicked!");
            currentFragment = NewsFragment.newInstance(1, this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.flContent, currentFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_myplan) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Manage clicks inside Courses list view
    @Override
    public void onListFragmentInteraction(Course item) {
        Log.d(TAG, item.getName() + " was clicked!");
        currentFragment = CourseDetailFragment.newInstance(this, this, item);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, currentFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Manage clicks on News view
    @Override
    public void onListFragmentInteraction(News item) {
        Log.d(TAG, item.getTitle() + " was clicked!");
    }

    // Manage clicks of Cathedras inside Courses detailed view
    @Override
    public void onListFragmentInteraction(Cathedra item) {
        Intent intent = new Intent(this, CathedraActivity.class);
        intent.putExtra(CathedraActivity.INTENT_CODE, (Serializable) item);
        startActivity(intent);
    }

    // Manage interaction inside Course detail view
    @Override
    public void onFragmentInteraction(Course course) {

    }
}