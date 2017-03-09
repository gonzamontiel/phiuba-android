package mont.gonzalo.phiuba.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gonzalo Montiel on 1/22/17.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE user "        +
                    "id INTEGER, "      +
                    "name TEXT, "       +
                    "lastName TEXT "    +
                    "planCode TEXT";

    private static final String USER_COURSE_TABLE_CREATE =
            "CREATE TABLE userCourse "  +
                    "userId INTEGERm, " +
                    "courseCode TEXT, " +
                    "status INTEGER";

    public DatabaseOpenHelper(Context context) {
        super(context, "data", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE + " " + USER_COURSE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO what?
    }
}
