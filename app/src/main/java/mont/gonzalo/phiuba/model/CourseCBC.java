package mont.gonzalo.phiuba.model;

import java.util.Arrays;

/**
 * Created by Gonzalo Montiel on 4/20/17.
 */

public class CourseCBC extends Course {
    public CourseCBC() {
        super("CBC", "CBC");
        setDepCode("");
        setPlanCode("");
        setLink("");
        setDepto("CBC");
        setRequired(true);
        setCorrelatives(Arrays.asList(""));
    }

    @Override
    public int getCredits() {
        return 0;
    }

    @Override
    public String getLongName() {
        return getName();
    }

    @Override
    public boolean isApproved() {
        return true;
    }
}
