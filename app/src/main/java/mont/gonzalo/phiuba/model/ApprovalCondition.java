package mont.gonzalo.phiuba.model;

/**
 * Created by Gonzalo Montiel on 4/4/17.
 */

public abstract class ApprovalCondition {
    public abstract boolean met(User u);

    public static boolean metAll(Plan p, User u) {
        for (ApprovalCondition c: p.getAllConditions()) {
            if (!c.met(u)) {
                return false;
            }
        }
        return true;
    }
}

