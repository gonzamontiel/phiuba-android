package mont.gonzalo.phiuba.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import mont.gonzalo.phiuba.model.CourseStatus;
import mont.gonzalo.phiuba.model.User;
import mont.gonzalo.phiuba.model.UserCourse;

/**
 * Created by gonzalo on 1/22/17.
 */

public class Database {
    private DatabaseOpenHelper helper;
    private SQLiteDatabase db;

    // Since by now we only allow one user, use this Id to reference it
    private static int USER_ID = 1;

    public Database(Context context) {
        this.helper = new DatabaseOpenHelper(context);
        this.db = this.helper.getReadableDatabase();
    }

    public SQLiteDatabase get() {
        return this.db;
    }

    public void insertUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", USER_ID);
        contentValues.put("name", user.getFirstName());
        contentValues.put("lastName", user.getLastName());
        contentValues.put("planCode", user.getPlanCode());
        db.insert("user", null, contentValues);
    }

    public User getUser() {
        ContentValues contentValues = new ContentValues();
        Cursor res = db.rawQuery("SELECT * FROM user where id = " + USER_ID, null);
        User u = new User(res.getString(res.getColumnIndex("firstName")), res.getString(res.getColumnIndex("lastName")));
        u.selectPlan(res.getString(res.getColumnIndex("planCode")));
        return u;
    }

    public void changeUserPlan(User user, String planCodes) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", user.getFirstName());
        contentValues.put("lastName", user.getLastName());
        contentValues.put("planCode", user.getPlanCode());
        db.insert("user", null, contentValues);
    }

    public void addOrUpdateCourseStatus(User user, String courseCode, CourseStatus status) {

    }

    public ArrayList<UserCourse> getUserCourses() {
        Cursor res = db.rawQuery("SELECT * FROM userCourse where userId = " + USER_ID, null);
        ArrayList<UserCourse> courses = new ArrayList<UserCourse>();
        while (res.isAfterLast() == false) {
            res.moveToNext();
        }
        return courses;
    }
}
