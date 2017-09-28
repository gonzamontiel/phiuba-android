package mont.gonzalo.phiuba;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import mont.gonzalo.phiuba.api.DataFetcher;
import mont.gonzalo.phiuba.model.Branch;
import mont.gonzalo.phiuba.model.Plan;
import mont.gonzalo.phiuba.model.User;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || WeekViewPreferenceFragment.class.getName().equals(fragmentName)
                || MyPlanPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);
            onSharedPreferenceChanged(null, "");

            bindPreferenceSummaryToValue(findPreference("server_ip"));
            bindPreferenceSummaryToValue(findPreference("event_keywords"));
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("server_ip")) {
                DataFetcher.getInstance().updateServer();
            } else if (key.equals("event_notif") && sharedPreferences.getBoolean("event_notif", false)) {
                NotificationEventReceiver.setupAlarm(getContext());
            } else if (key.equals("event_keywords")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("last_searched_events", 0);
                editor.commit();
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class WeekViewPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_weekview);
            setHasOptionsMenu(true);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    public static class MyPlanPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_my_plan);
            setHasOptionsMenu(true);
            final ListPreference planListPreference = (ListPreference) findPreference("pref_plan");
            final ListPreference branchListPreference = (ListPreference) findPreference("pref_branch");
            final ListPreference tesisListPreference = (ListPreference) findPreference("pref_tesis");
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            bindPreferenceSummaryToValue(tesisListPreference);

            branchListPreference.setSummary(prefs.getString("pref_branch_name", ""));

            DataFetcher.getInstance().getPlans(new Callback<List<Plan>>() {
                @Override
                public void success(List<Plan> plans, Response response) {
                    Plan.setAvailablePlans(plans);
                    CharSequence[] entries = new CharSequence[plans.size()];
                    CharSequence[] entryValues = new CharSequence[plans.size()];

                    for (int i = 0; i < plans.size(); i++) {
                        entryValues[i] = plans.get(i).getCode();
                        entries[i] = plans.get(i).getShortName();
                    }
                    planListPreference.setEntries(entries);
                    planListPreference.setEntryValues(entryValues);
                    planListPreference.setSummary(Plan.byCode(planListPreference.getValue()).getShortName());
                    fillBranchesPreference(Plan.byCode(planListPreference.getValue()), branchListPreference);
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });

            branchListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue((String) newValue);
                    preference.setSummary(index >= 0? listPreference.getEntries()[index]: null);
                    Plan p = Plan.byCode(prefs.getString("pref_plan", Plan.getDefault()));
                    prefs.edit().putString("pref_branch_name", p.getBranchName((String) newValue)).commit();
                    return true;
                }
            });
            planListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Plan plan = Plan.byCode(newValue.toString());
                    User.get().selectPlan(plan);
                    fillBranchesPreference(plan, branchListPreference);
                    planListPreference.setSummary(plan.getShortName());
                    prefs.edit().putString("pref_branch", "").commit();
                    prefs.edit().putString("pref_branch_name", "").commit();
                    return true;
                }
            });
        }

        private void fillBranchesPreference(Plan plan, ListPreference branchListPreference) {
            ArrayList<Branch> branches = plan.getBranches();
            if (!branches.isEmpty()) {
                CharSequence[] entries = new CharSequence[branches.size()];
                CharSequence[] entryValues = new CharSequence[branches.size()];
                for (int i = 0; i < branches.size(); i++) {
                    entryValues[i] = branches.get(i).getCode();
                    entries[i] = branches.get(i).getName();
                }
                branchListPreference.setEntries(entries);
                branchListPreference.setEntryValues(entryValues);
            } else {
                branchListPreference.setEntries(getActivity().getResources().getStringArray(
                        R.array.branch_preference_default_entries));
                branchListPreference.setEntryValues(getActivity().getResources().getStringArray(
                            R.array.branch_preference_default_entry_values));
            }
        }
    }
}
