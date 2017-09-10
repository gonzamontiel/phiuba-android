package mont.gonzalo.phiuba.layout;

import android.content.Context;

/**
 * Created by Gonzalo Montiel on 3/9/17.
 */
public class ActivityContext {
    private static Context _context;

    public static void set(Context context) {
        _context = context;
    }

    public static Context get() {
        return _context;
    }
}
