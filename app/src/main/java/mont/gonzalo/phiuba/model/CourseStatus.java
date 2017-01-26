package mont.gonzalo.phiuba.model;

import java.util.HashMap;

import mont.gonzalo.phiuba.R;

/**
 * Created by gonzalo on 1/15/17.
 */

public enum CourseStatus {
    DEFAULT,
    ADDED,
    STUDYING,
    APPROVED;

    private static HashMap<CourseStatus, Integer>  mapColors;
    static
    {
        mapColors = new HashMap<CourseStatus, Integer>();
        mapColors.put(DEFAULT, R.color.course_default);
        mapColors.put(ADDED, R.color.course_added);
        mapColors.put(STUDYING, R.color.course_studyng);
        mapColors.put(APPROVED, R.color.course_approved);
    }

    public Integer getColor(CourseStatus ucs) {
        return mapColors.get(ucs);
    }
}
