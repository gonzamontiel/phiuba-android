package mont.gonzalo.phiuba.model;

public class TesisCondition extends ApprovalCondition {
    private int credits = 24;

    @Override
    public boolean isMetBy(User u) {
        String tesisCode = User.get().getPlan().getTesisCode();
        if (!tesisCode.isEmpty()) {
            return UserCourses.getInstance().isApproved(tesisCode);
        }
        return true;
    }
}
