package mont.gonzalo.phiuba.model;

import java.util.HashMap;

import mont.gonzalo.phiuba.R;

/**
 * Created by Gonzalo Montiel on 1/15/17.
 */

public enum CourseStatus {
    DEFAULT,
    FAVOURITE,
    STUDYING,
    APPROVED,
    AVAILABLE,
    NOT_AVAILABLE;

    private static HashMap<CourseStatus, Integer> mapColors;
    static
    {
        mapColors = new HashMap<CourseStatus, Integer>();
        mapColors.put(DEFAULT, R.color.course_default);
        mapColors.put(FAVOURITE, R.color.course_added);
        mapColors.put(STUDYING, R.color.course_studyng);
        mapColors.put(APPROVED, R.color.course_approved);
        mapColors.put(AVAILABLE, R.color.course_available);
        mapColors.put(NOT_AVAILABLE, R.color.course_not_available);
    }

    public static int getByStatus(CourseStatus courseStatus) {
            return mapColors.get(courseStatus);
    }
}
