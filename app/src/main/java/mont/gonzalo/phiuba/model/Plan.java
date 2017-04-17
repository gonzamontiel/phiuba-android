package mont.gonzalo.phiuba.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.layout.ActivityContext;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Gonzalo Montiel on 11/20/16.
 */
public class Plan {
    private static final int DEFAULT_CRED = 240;
    private String code;
    private String name = "";
    private String link = "";
    private int credits;
    private List<Branch> branches;

    private static HashMap<String, Plan> plans = new HashMap<String, Plan>();
    private static HashMap<String, String> planShortNames = new HashMap<String, String>();
    static {
        planShortNames.put("AGRIMEN86", "Lic. Agrimensura");
        planShortNames.put("ALIMENT86", "Ing. Alimentos");
        planShortNames.put("CIVIL09", "Ing. Civil 2009");
        planShortNames.put("CIVIL86", "Ing. Civil 1986");
        planShortNames.put("ELECTRI09", "Ing. Electricista 2009");
        planShortNames.put("ELECTRI86", "Ing. Electricista 1986");
        planShortNames.put("ELECTRO09", "Ing. Electrónica 2009");
        planShortNames.put("ELECTRO86", "Ing. Electrónica 1986");
        planShortNames.put("INDUSTR11", "Ing. Industrial 2011");
        planShortNames.put("INDUSTR86", "Ing. Industrial 1986");
        planShortNames.put("INFORMA86", "Ing. Informática");
        planShortNames.put("MECANIC86", "Ing. Mecánica 1986");
        planShortNames.put("NAVAL09", "Ing. Naval 2009");
        planShortNames.put("PETROLE09", "Ing. Petróleo 2009");
        planShortNames.put("QUIMICA86", "Ing. Química 1986");
        planShortNames.put("SISTEMA14", "Lic. Sistemas 2014");
        planShortNames.put("SISTEMA86", "Lic. Sistemas 1986");
    }

    Plan(String code) {
        this.code = code;
    }

    public static Plan byShortName(String shortName) {
        for (String key: planShortNames.keySet()){
            if (planShortNames.get(key) == shortName) {
                return plans.get(key);
            }
        }
        return null;
    }

    public static Plan byCode(String planCode) {
        if (!plans.containsKey(planCode)) {
            plans.put(planCode, new Plan(planCode));
        }
        return plans.get(planCode);
    }

    public String getCode() {
        return code;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name != "" ? name : getShortName();
    }

    public String getShortName() {
        return nameByCode(code);
    }

    private String nameByCode(String code) {
        return planShortNames.get(code);
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getDefault() {
        return "INFORMA86";
    }

    public static void initialize() {
    }

    public static HashMap<String, Plan> getPlans() {
        return plans;
    }

    @Override
    public String toString() {
        return nameByCode(this.getCode());
    }

    public static void setAvailablePlans(List<Plan> plans) {
        for (Plan p : plans) {
            Plan.plans.put(p.getCode(), p);
        }
    }

    public static List<String> getAvailableNames() {
        List<String>  names = new ArrayList<String>();
        for (Plan p : plans.values()) {
            names.add(p.getShortName());
        }
        return names;
    }

    public ApprovalCondition[] getAllConditions() {
        return new ApprovalCondition[]{
                new CreditCondition(credits)
        };
    }

    public double getCredits() {
        return credits > 0 ? credits : DEFAULT_CRED;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branchis) {
        this.branches = branchis;
    }

    public static String getFromSharedPrefs() {
        Context context = ActivityContext.get();
        if (context != null) {
            SharedPreferences mPrefs = context.getSharedPreferences(
                    context.getResources().getString(R.string.shared_prefs), MODE_PRIVATE);
            return mPrefs.getString("plan", getDefault());
        }
        return getDefault();
    }

    public void saveToSharedPrefs() {
        Context context = ActivityContext.get();
        SharedPreferences mPrefs = context.getSharedPreferences(
                context.getResources().getString(R.string.shared_prefs), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("plan", this.getCode());
        prefsEditor.commit();
    }
}

