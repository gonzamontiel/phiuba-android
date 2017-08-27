package mont.gonzalo.phiuba.layout;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.SettingsActivity;
import mont.gonzalo.phiuba.api.DataFetcher;
import mont.gonzalo.phiuba.model.CalendarIntegration;
import mont.gonzalo.phiuba.model.Cathedra;
import mont.gonzalo.phiuba.model.CathedraSchedule;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.Department;
import mont.gonzalo.phiuba.model.Event;
import mont.gonzalo.phiuba.model.News;
import mont.gonzalo.phiuba.model.Plan;
import mont.gonzalo.phiuba.model.User;
import mont.gonzalo.phiuba.model.UserCourses;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CoursesFragment.OnListFragmentInteractionListener,
        NewsFragment.OnListFragmentInteractionListener,
        CourseDetailFragment.OnFragmentInteractionListener,
        CourseDetailFragment.OnListFragmentInteractionListener,
        DepartmentsFragment.OnListFragmentInteractionListener,
        EventsFragment.OnListFragmentInteractionListener,
        SearchView.OnQueryTextListener,
        DepartmentDetailFragment.OnListFragmentInteractionListener,
        MiniCoursesAdapter.OnCorrelativeInteractionListener,
        Observer {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String ACTIVE_FRAGMENT = "active_fragment";
    public static final String SPECIFIC_FRAGMENT = "specific_fragment";
    public static final String SPECIFIC_FRAGMENT_DATA = "specific_fragment_data";
    private SearchView searchView;
    private SearchableFragment currentFragment;
    private ProgressBar progressBar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityContext.set(this);
        DataFetcher.getInstance().addObserver(this);
        initializeDrawer();
        initiate(savedInstanceState);
    }

    private void initiate(Bundle savedInstanceState) {
        showProgressBar();

        User.initialize();
        Plan.initialize();

        applyDefaultFragment(savedInstanceState);
        showFloatingButton();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(getDefaultItemSelected());
        navigationView.setNavigationItemSelectedListener(this);
        fillSpinnerWithPlans(navigationView);

        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        checkNetwork();
    }

    private void showProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.progress_spinner);
        progressBar.setVisibility(View.VISIBLE);
        // Hide after 5 seconds
        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            }, 5000);
    }

    private void initializeDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void showFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Envia sugerencias!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void applyDefaultFragment(Bundle savedInstanceState) {
        Class fragmentClass = null;
        currentFragment = null;
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            currentFragment = (SearchableFragment) savedInstanceState.get(ACTIVE_FRAGMENT);
        } else if (intent != null) {
            fragmentClass = (Class) intent.getSerializableExtra(SPECIFIC_FRAGMENT);
        }
        if (currentFragment == null) {
            fragmentClass = (fragmentClass == null) ? getDefaultFragmentClass() : fragmentClass;
            try {
                currentFragment = (SearchableFragment) fragmentClass.newInstance();
                currentFragment.loadFromIntent(intent.getSerializableExtra(SPECIFIC_FRAGMENT_DATA));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, currentFragment).commit();
    }

    private void fillSpinnerWithPlans(NavigationView navigationView) {
        final Spinner spinner = (Spinner) navigationView.getHeaderView(0).findViewById(R.id.spinner);
        DataFetcher.getInstance().getPlans(new Callback<List<Plan>>() {
            @Override
            public void success(List<Plan> plans, Response response) {
                Plan.setAvailablePlans(plans);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.menu_spinner_item_first, Plan.getAvailableNames());
                spinnerArrayAdapter.setDropDownViewResource(R.layout.menu_spinner_item);
                spinner.setAdapter(spinnerArrayAdapter);

                String shortName = User.get().getPlan().getShortName();
                int count = 0;
                for (String name: Plan.getAvailableNames()) {
                    if (name == shortName) {
                        break;
                    }
                    count++;
                }
                spinner.setSelection(count);
            }

            @Override
            public void failure(RetrofitError error) {
                String[] defaults = new String[] {Plan.getDefault()};
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.menu_spinner_item_first, defaults);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.menu_spinner_item);
                spinner.setAdapter(spinnerArrayAdapter);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean firstTime = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (firstTime) {
                    firstTime = false;
                    return;
                }
                String data = spinner.getItemAtPosition(position).toString();
                Plan p = Plan.byShortName(data);
                User.get().selectPlan(p);
                UserCourses.getInstance().updateCourses();
                currentFragment.reset();
                drawer.closeDrawers();
                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Class getDefaultFragmentClass() {
        return NewsFragment.class;
    }

    private int getDefaultItemSelected() {
        return R.id.nav_events;
    }

    private void checkNetwork() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        Log.d("is connected ", String.valueOf(isConnected));
        if (!isConnected)
            showConnectionIndicator();
        else
            hideConnectionIndicator();
    }

    private void hideConnectionIndicator() {
        final View viewForSnackBar = this.findViewById(android.R.id.content);
        View v = viewForSnackBar.findViewById(R.id.disconnected_placeholder);
        v.setVisibility(View.GONE);
    }

    private void showConnectionIndicator() {
        final View viewForSnackBar = this.findViewById(android.R.id.content);
        Snackbar snack = Snackbar.make(viewForSnackBar, R.string.disconnected_message, 15000)
                .setAction("Action", null);

        TextView textView = (TextView) snack.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);  // show multiple line
        snack.show();
        
        View v = viewForSnackBar.findViewById(R.id.disconnected_placeholder);
        v.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ACTIVE_FRAGMENT, (Serializable) currentFragment);
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
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_update) {
            initiate(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment newOne = null;
        if (id == R.id.nav_courses) {
            newOne = CoursesFragment.newInstance(1, this);
        } else if (id == R.id.nav_events) {
            newOne = EventsFragment.newInstance(1, this);
        } else if (id == R.id.nav_news) {
            newOne = NewsFragment.newInstance(1, this);
        } else if (id == R.id.nav_depts) {
            newOne = DepartmentsFragment.newInstance(1, this);
        } else if (id == R.id.nav_myplan) {
            UserCourses.getInstance().printSummary();
            Intent intent = new Intent(this, MyPlanActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_myweek) {
            Intent intent = new Intent(this, WeekViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Log.d(TAG, "Share clicked!");
        } else if (id == R.id.nav_send) {
            Log.d(TAG, "Nav clicked!");
        }
        if (newOne != null && newOne != currentFragment) {
            currentFragment = (SearchableFragment) newOne;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.flContent, currentFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            searchView.setQuery("", false);
            searchView.setIconified(true);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Manage clicks inside Courses list view
    @Override
    public void onListFragmentInteraction(Course item) {
        goToCourseItem(item);
    }

    private void goToCourseItem(Course item) {
        currentFragment = CourseDetailFragment.newInstance(this, this, item);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, currentFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Manage clicks on News view
    @Override
    public void onListFragmentInteraction(News item) {
        goToNewsItem(item);
    }

    private void goToNewsItem(News item) {
        currentFragment = NewsDetailFragment.newInstance(item);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, currentFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        searchView.setQuery("", false);
        searchView.setIconified(true);
    }

    // Manage clicks of Cathedras inside Courses detailed view
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onListFragmentInteraction(final Cathedra item, final String courseName, final String teachers) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Agregar al calendario")
                .setMessage("Esto te llevará a agregar un evento recurrente a tu calendario. Siempre que quieras, podés eliminarlo desde el mismo calendario.")
                .setPositiveButton("Dale!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CalendarIntegration calendarIntegration = new CalendarIntegration();
                        CathedraSchedule schedule = item.getSchedules().get(0);
                        Intent calIntent = calendarIntegration.getCathedraScheduleIntent(schedule, courseName, teachers);
                        startActivity(calIntent);
                    }
                })
                .setNegativeButton("No, gracias", null)
                .show();

    }

    // Manage interaction inside Course detail view
    @Override
    public void onFragmentInteraction(Course course) {
    }

    @Override
    public void onListFragmentInteraction(Department item) {
        goToDepartment(item);
    }

    private void goToDepartment(Department item) {
        currentFragment = DepartmentDetailFragment.newInstance(this, item);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, currentFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Event item) {
        goToEventItem();
    }

    private void goToEventItem() {
//        currentFragment = EventDetailFragment.newInstance(this, this, item);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.flContent, currentFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
    }

    private void doHandleSearch(String query) {
        if (query.length() >= 3) {
            currentFragment.updateResults(query);
        } else if (query.length() == 0) {
            currentFragment.reset();
        }
    }

    private void handleSearch(String query) {
        Log.d(TAG, query);
        // Handle search in current fragment
        if (currentFragment.getClass() == currentFragment.getResultsFragment().getClass()) {
            doHandleSearch(query);
        } else {
            try {
                currentFragment = currentFragment.getResultsFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flContent, currentFragment);
                currentFragment.updateResults(query);
                transaction.addToBackStack(null);
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        handleSearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (currentFragment.getClass() == currentFragment.getResultsFragment().getClass()) {
            doHandleSearch(query);
            return true;
        }
        return false;
    }

    @Override
    public void update(Observable obs, Object arg) {
        if (obs instanceof DataFetcher) {
            DataFetcher df = (DataFetcher) obs;
            if (!df.isRunning()) {
                showConnectionIndicator();
            }
        }
        progressBar.setVisibility(View.GONE);
    }

    public void showDepartment(Department dep) {
        goToDepartment(dep);
    }

    @Override
    public void onCorrelativeInteraction(Course item) {
        goToCourseItem(item);
    }
}