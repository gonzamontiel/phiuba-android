package mont.gonzalo.phiuba.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import mont.gonzalo.phiuba.layout.ActivityContext;

/**
 * Created by Gonzalo Montiel on 4/6/17.
 */
public class Branch {
    private String code;
    private String name;
    private String[] required;

    public String[] getRequired() {
        return required;
    }

    public void setRequired(String[] required) {
        this.required = required;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getFromSharedPrefs() {
        Context context = ActivityContext.get();
        if (context != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            return prefs.getString("pref_branch", "");
        }
        return "";
    }

    public static String getNameFromSharedPrefs() {
        Context context = ActivityContext.get();
        if (context != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            return prefs.getString("pref_branch_name", "");
        }
        return "";
    }

}
