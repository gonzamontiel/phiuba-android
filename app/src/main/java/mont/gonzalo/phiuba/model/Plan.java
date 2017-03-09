package mont.gonzalo.phiuba.model;

import java.util.HashMap;

/**
 * Created by Gonzalo Montiel on 11/20/16.
 */
public class Plan {
    private String code;
    private String name;
    private String link;
    private static HashMap<String, Plan> plans = new HashMap<String, Plan>();

    Plan(String code) {
        this.code = code;
    }

    static Plan byCode(String planCode) {
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getDefault() {
        return "INFORMA86";
    }
}
