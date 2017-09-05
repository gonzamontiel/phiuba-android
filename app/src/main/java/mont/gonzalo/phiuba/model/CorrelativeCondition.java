package mont.gonzalo.phiuba.model;

public class CorrelativeCondition extends ApprovalCondition {
    private final String corrCode;

    public CorrelativeCondition(String corrCode) {
        this.corrCode = corrCode;
    }

    @Override
    public boolean isMetBy(User u) {
        if (isCourseCode() || isSpecialCourseCode()) {
            return UserCourses.getInstance().isApproved(corrCode);
        } else if (isCreditsCode()) {
            int credits = getCreditsFromCorrCode();
            return (new CreditCondition(credits)).isMetBy(u);
        }
        return false;
    }

    public int getCreditsFromCorrCode() {
        return Integer.parseInt(corrCode.replaceAll("CRED(\\d+)", "$1"));
    }

    public boolean isCourseCode() {
        return corrCode.matches("\\d\\d.\\d\\d");
    }

    public boolean isSpecialCourseCode() {
        return corrCode.matches("CBC");
    }

    public boolean isCreditsCode() {
        return corrCode.matches("CRED(\\d+)");
    }
}
