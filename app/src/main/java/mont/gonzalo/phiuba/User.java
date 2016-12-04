package mont.gonzalo.phiuba;

/**
 * Created by gonzalo on 11/20/16.
 */

public class User {
    private Plan plan;
    private String firstName;
    private String lastName;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.plan = null;
    }

    void selectPlan(String planCode) {
        this.plan = Plan.byCode(planCode);
    }

    public String getPlanCode() {
        return this.plan.getCode();
    }
}
