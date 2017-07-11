package mont.gonzalo.phiuba.model;

/**
 * Created by Gonzalo Montiel on 4/4/17.
 */

public abstract class ApprovalCondition {
    public abstract boolean isMetBy(User u);

    public static boolean metAll(Plan p, User u) {
        for (ApprovalCondition c: p.getAllConditions()) {
            if (!c.isMetBy(u)) {
                return false;
            }
        }
        return true;
    }
}

