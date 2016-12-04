package mont.gonzalo.phiuba;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by gonzalo on 11/20/16.
 */
public class Plan {
    private String planCode;
    private JSONObject information;
    private static HashMap<String, Plan> plans = new HashMap<String, Plan>();

    Plan(String planCode) {
        this.planCode = planCode;
    }

    static Plan byCode(String planCode) {
        if (!plans.containsKey(planCode)) {
            plans.put(planCode, new Plan(planCode));
        }
        return plans.get(planCode);
    }

    public String getCode() {
        return planCode;
    }
}
