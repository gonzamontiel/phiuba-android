package mont.gonzalo.phiuba.model;

public class CreditCondition extends ApprovalCondition {
    private int credits;

    CreditCondition (int credits) {
        this.credits = credits;
    }

    @Override
    public boolean met(User u) {
        return User.get().getTotalCredits() > credits;
    }
}
