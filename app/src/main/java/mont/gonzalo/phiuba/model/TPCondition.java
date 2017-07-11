package mont.gonzalo.phiuba.model;

public class TPCondition extends ApprovalCondition {
    private int credits = 24;

    @Override
    public boolean isMetBy(User u) {
        String tpCode = User.get().getPlan().getTPCode();
        if (tpCode.isEmpty()) {
            return UserCourses.getInstance().isApproved(tpCode);
        }
        return true;
    }
}
