package mont.gonzalo.phiuba.layout;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import mont.gonzalo.phiuba.R;

public class WeekViewSettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

}
