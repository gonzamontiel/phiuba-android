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
    EXAM_PENDING,
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
        mapColors.put(EXAM_PENDING, R.color.course_exam_pending);
        mapColors.put(NOT_AVAILABLE, R.color.course_not_available);
    }

    private static HashMap<CourseStatus, Integer> mapNames;
    static
    {
        mapNames = new HashMap<CourseStatus, Integer>();
        mapNames.put(DEFAULT, R.string.course_default);
        mapNames.put(FAVOURITE, R.string.course_added);
        mapNames.put(STUDYING, R.string.course_studyng);
        mapNames.put(APPROVED, R.string.course_approved);
        mapNames.put(AVAILABLE, R.string.course_available);
        mapNames.put(EXAM_PENDING, R.string.course_exam_pending);
        mapNames.put(NOT_AVAILABLE, R.string.course_not_available);
    }
    public static int getColorByStatus(CourseStatus courseStatus) {
            return mapColors.get(courseStatus);
    }

    public static int getStringByStatus(CourseStatus courseStatus) {
        return mapNames.get(courseStatus);
    }
}

