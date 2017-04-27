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
        setCredits(1);
        setCorrelatives(Arrays.asList(""));
    }
}
