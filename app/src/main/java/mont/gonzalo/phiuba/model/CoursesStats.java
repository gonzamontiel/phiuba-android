package mont.gonzalo.phiuba.model;

import java.util.List;

/**
 * Created by Gonzalo Montiel on 9/27/17.
 */

public class CoursesStats {
    private static CoursesStats _instance = null;
    public enum TYPE {
        OBL,
        OPT,
        ORIENT
    }

    public static CoursesStats getInstance() {
        if (_instance == null) {
                _instance = new CoursesStats();
        }
        return _instance;
    }

    public int getApprovedCount(CoursesStats.TYPE type) {
        return  getCount(UserCourses.getInstance().getApprovedCourses(), type);
    }

    public int getStudyingCount(CoursesStats.TYPE type) {
        return  getCount(UserCourses.getInstance().getStudyingCourses(), type);
    }

    public int getNotCoursedCount(CoursesStats.TYPE type) {
        return  getCount(UserCourses.getInstance().getNotCoursedCourses(), type);
    }

    public int getRequiredCount() {
        return getCount(UserCourses.getInstance().getAll(), TYPE.OBL);
    }

    public int getOptionalCount() {
        return getCount(UserCourses.getInstance().getAll(), TYPE.OPT);
    }

    public int getBranchCount() {
        return getCount(UserCourses.getInstance().getAll(), TYPE.ORIENT);
    }

    public int getCount(List<Course> courses, CoursesStats.TYPE type) {
        int count = 0;
        for (Course c: courses) {
            switch (type) {
                case OBL: if (c.isRequired()) {count++;} break;
                case OPT: if (!c.isRequired()) {count++;} break;
                case ORIENT: if (c.isFromCurrentBranch()) {count++;} break;
            }
        }
        return count;
    }

}
