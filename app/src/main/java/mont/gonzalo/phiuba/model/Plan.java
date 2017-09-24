package mont.gonzalo.phiuba.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mont.gonzalo.phiuba.layout.ActivityContext;

/**
 * Created by Gonzalo Montiel on 11/20/16.
 */
public class Plan {
    private static final int DEFAULT_CRED = 240;
    private String code;
    private String name = "";
    private String link = "";
    private Branch selectedBranch = null;
    private int credits;
    private ArrayList<Branch> branches = new ArrayList<>();
    private List<ApprovalCondition> conditions = new ArrayList<>();

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

    private static HashMap<String, ArrayList<String> > planTesisCode = new HashMap<>();
    static {
        planTesisCode.put("AGRIMEN86",  new ArrayList<>( Arrays.asList("70.00", "70.99")));
        planTesisCode.put("ALIMENT86",  new ArrayList<>( Arrays.asList("76.90", "76.90")));
        planTesisCode.put("CIVIL09",  new ArrayList<>( Arrays.asList("84.00", "84.99")));
        planTesisCode.put("CIVIL86",  new ArrayList<>( Arrays.asList("64.00", "64.99")));
        planTesisCode.put("ELECTRI09",  new ArrayList<>( Arrays.asList("85.00", "85.99")));
        planTesisCode.put("ELECTRI86",  new ArrayList<>( Arrays.asList("65.00", "65.99")));
        planTesisCode.put("ELECTRO09",  new ArrayList<>( Arrays.asList("86.00", "86.99")));
        planTesisCode.put("ELECTRO86",  new ArrayList<>( Arrays.asList("66.00", "66.99")));
        planTesisCode.put("INDUSTR11",  new ArrayList<>( Arrays.asList("91.00", "91.99")));
        planTesisCode.put("INDUSTR86",  new ArrayList<>( Arrays.asList("72.00", "72.99")));
        planTesisCode.put("INFORMA86",  new ArrayList<>( Arrays.asList("75.00", "75.99")));
        planTesisCode.put("MECANIC86",  new ArrayList<>( Arrays.asList("67.00", "67.99")));
        planTesisCode.put("NAVAL09",  new ArrayList<>( Arrays.asList("73.00", "73.99")));
        planTesisCode.put("PETROLE09",  new ArrayList<>( Arrays.asList("", "")));
        planTesisCode.put("QUIMICA86",  new ArrayList<>( Arrays.asList("76.00", "76.99")));
        planTesisCode.put("SISTEMA14",  new ArrayList<>( Arrays.asList("", "")));
        planTesisCode.put("SISTEMA86",  new ArrayList<>( Arrays.asList("", "75.32")));
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

    public ArrayList<Branch> getBranches() {
        return branches;
    }

    public double getCredits() {
        return credits > 0 ? credits : DEFAULT_CRED;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getTesisCode() {
        return planTesisCode.get(this.getCode()).get(0);
    }

    public String getTPCode() {
        return planTesisCode.get(this.getCode()).get(1);
    }

    public boolean isComplete() {
        boolean meetAllConditions= true;
        for (ApprovalCondition condition: conditions) {
            meetAllConditions = meetAllConditions && condition.isMetBy(User.get());
            if (!meetAllConditions)
                break;
        }
        return meetAllConditions;
    }

    public static String getFromSharedPrefs() {
        Context context = ActivityContext.get();
        if (context != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            return prefs.getString("pref_plan", getDefault());
        }
        return getDefault();
    }

    public Branch getSelectedBranch() {
        return selectedBranch;
    }

    public void setSelectedBranch(Branch selectedBranch) {
        this.selectedBranch = selectedBranch;
    }

    public String getBranchName(String branchCode) {
        for (Branch b: getBranches()) {
            if (b.getCode().equals(branchCode)) {
                return b.getName();
            }
        }
        return "";
    }
}

