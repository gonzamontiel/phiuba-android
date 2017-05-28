package mont.gonzalo.phiuba.layout;


import android.support.v4.app.Fragment;

import java.io.Serializable;

/**
 * Created by Gonzalo Montiel on 3/2/17.
 */

public abstract class SearchableFragment extends Fragment {
    public abstract void updateResults(String query);
    public abstract void reset();
    public abstract SearchableFragment getResultsFragment();

    public void loadFromIntent(Serializable data) {
    }
}
