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

    static User mock_instance = null;

    void selectPlan(String planCode) {
        this.plan = Plan.byCode(planCode);
    }

    public String getPlanCode() {
        return this.plan.getCode();
    }

    static User getMock() {
        if (mock_instance == null) {
            mock_instance = new User("Harry", "Potter");
            mock_instance.selectPlan(Plan.getDefault());
        }
        return mock_instance;
    }
}
