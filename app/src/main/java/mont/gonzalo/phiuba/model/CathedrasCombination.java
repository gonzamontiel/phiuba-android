package mont.gonzalo.phiuba.model;

import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Gonzalo Montiel on 3/20/17.
 */
public class CathedrasCombination implements Serializable {
    private HashMap<String, List<Cathedra>> cathedras;
    private HashMap<String, Integer> sizes;
    private HashMap<String, Integer> pointers;
    List<Map.Entry<String, Integer>> sortedSizes;
    private int maxCombinations;


    public CathedrasCombination() {
        cathedras = new HashMap<String, List<Cathedra>>();
        sizes = new HashMap<String, Integer>();
        pointers = new HashMap<String, Integer>();
        maxCombinations = 1;
    }

    public void fillWith(UserCourses userCourses) {
        maxCombinations = 1;
        for (String courseCode: userCourses.getStudyingCourses().keySet()) {
            List<Cathedra> css = userCourses.getCourse(courseCode).getCathedras();
            cathedras.put(courseCode, css);
            sizes.put(courseCode, css.size());
            maxCombinations *= css.size();
        }
        Set<Map.Entry<String, Integer>> set = sizes.entrySet();
        sortedSizes = new ArrayList<Map.Entry<String, Integer>>(set);
        Collections.sort(sortedSizes, new SizesComparator());

        for (Map.Entry<String, Integer> entry: sortedSizes){
            int size = entry.getValue();
            String courseName = entry.getKey();
            for (int i = 0; i < size; i++) {
                Log.d(String.valueOf(i), cathedras.get(courseName).get(i).getTeachers());

            }
        }
    }

    public HashMap<String, List<Cathedra>> getCathedras() {
        return cathedras;
    }

    public Combination getNext(int n) {
        return null;
    }

    public class Combination {
        private final String courseName;
        private final String teachers;
        private final List<WeekViewEvent> events;

        public Combination(String courseName, String teachers, List<WeekViewEvent> events) {
            this.courseName = courseName;
            this.teachers = teachers;
            this.events = events;
        }
    }

    private class SizesComparator implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }
    }
}
