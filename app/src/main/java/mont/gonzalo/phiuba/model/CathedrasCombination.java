package mont.gonzalo.phiuba.model;

import android.support.annotation.NonNull;

import com.alamkanak.weekview.WeekViewEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mont.gonzalo.phiuba.api.DataFetcher;
import mont.gonzalo.phiuba.layout.MaterialColors;
import mont.gonzalo.phiuba.tda.Node;

/**
 * Created by Gonzalo Montiel on 3/20/17.
 */
public class CathedrasCombination implements Serializable {
    private HashMap<String, List<Cathedra>> cathedrasByCourse;
    private HashMap<String, Cathedra> cathedras;
    private HashMap<String, Integer> colors;
    private List<Cathedra> pinnedCathedras;
    private Node<String> root;
    private Node<String> leftmostLeaf = null;
    private int combinations;
    private ArrayList<String> failingCourseCodes;
    private static CathedrasCombination _instance;

    public static CathedrasCombination getInstance() {
        if (_instance == null) {
            _instance = new CathedrasCombination();
        }
        return _instance;
    }

    private CathedrasCombination() {
        cathedrasByCourse = new HashMap<>();
        cathedras = new HashMap<>();
        colors =  new HashMap<>();
        failingCourseCodes = new ArrayList<>();
        combinations = 0;
    }

    public void buildTree() {
        cathedras.clear();
        combinations = 1;
        root = new Node<>(null);
        Node<String> node = root;
        for (String code: cathedrasByCourse.keySet()) {
            combinations *= cathedrasByCourse.get(code).size();
            List<String> names = new ArrayList<>();
            for (Cathedra cat: cathedrasByCourse.get(code)) {
                String key = code + " - " + cat.getTeachers();
                names.add(key);
                cat.setColor(colors.get(code));
                cathedras.put(key, cat);
            }
            if (!names.isEmpty()) {
                node.addChildrenData(names);
                node.copyChildrenToSiblings();
                node = node.firstChild();
            }
        }
        leftmostLeaf = node;
    }

    public void removeCollisions() {
        if (isEmpty())
            return;

        Node<String> currentLeaf = leftmostLeaf, rightLeaf;
        combinations = 0;
        while (currentLeaf != null) {
            List<Cathedra> cathedrasComb = getCombinationFromLeaf(currentLeaf);
            rightLeaf = currentLeaf.getRight();
            if (schedulesCollide(cathedrasComb)) {
                currentLeaf.removeFromLeafs();
            } else {
                combinations++;
            }
            currentLeaf = rightLeaf;
        }
    }

    @NonNull
    private List<Cathedra> getCombinationFromLeaf(Node<String> nodeToFind) {
        List<Cathedra> cathedrasComb = new ArrayList<>();
        Node<String> parent = nodeToFind;
        while (parent != null) {
            cathedrasComb.add(cathedras.get(parent.getData()));
            parent = parent.getParent();
        }
        if (!cathedrasComb.isEmpty()) {
            cathedrasComb.remove(cathedrasComb.size() - 1);
        }
        return cathedrasComb;
    }

    public List<Cathedra> getAtPosition(int n) {
        Node<String> nodeToFind = leftmostLeaf;
        List<Cathedra> cathedrasComb = new ArrayList<>();
        int count = 1;
        while (nodeToFind != null && count++ < n) {
            nodeToFind = nodeToFind.getRight();
        }
        return getCombinationFromLeaf(nodeToFind);
    }

    public void print() {
        root.print();
    }

    public void pinCathedra(Cathedra c) {
        this.pinnedCathedras.add(c);
    }

    private boolean schedulesCollide(List<Cathedra> cathedrasComb) {
        for (Cathedra c1: cathedrasComb) {
            if (c1 == null) {
                break;
            }
            for (Cathedra c2: cathedrasComb) {
                if (c2 == null) {
                    break;
                }
                if (!c1.getCourseCode().equals(c2.getCourseCode()) &&
                        collide(c1, c2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean collide(Cathedra c1, Cathedra c2) {
        for (WeekViewEvent event1: c1.toWeekEvents("")) {
            for (WeekViewEvent event2 : c2.toWeekEvents("")) {
                if (eventsCollide(event1, event2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean eventsCollide(WeekViewEvent e1, WeekViewEvent e2) {
        boolean isBefore = e1.getEndTime().before(e2.getStartTime());
        boolean isAfter= e1.getStartTime().after(e2.getEndTime());
        return !isBefore && !isAfter;
    }

    public int getCombinationCount() {
        return combinations;
    }

    public void loadCathedrasSync() {
        failingCourseCodes.clear();
        cathedrasByCourse.clear();
        UserCourses uc = UserCourses.getInstance();
        int n = 0;
        for (String code: uc.getStudyingCourses()) {
            Course c = uc.getCourse(code);
            if (c != null) {
                c.setCathedras(DataFetcher.getInstance().getCathedrasSync(code));
                if (c.hasCathedrasAvailable()) {
                    cathedrasByCourse.put(code, c.getCathedras());
                } else {
                    failingCourseCodes.add(code);
                }
                colors.put(code, MaterialColors.get(n++));
            }
        }
    }

    public void addCathedraByCourse(String code, List<Cathedra> list) {
        cathedrasByCourse.put(code, list);
    }

    public Integer generateColorForCourse(String code) {
        Integer color = MaterialColors.getRandom();
        while (colors.containsValue(color)) {
            color = MaterialColors.getRandom();
        }
        return color;
    }

    public boolean isEmpty() {
        return cathedrasByCourse.keySet().isEmpty();
    }

    public ArrayList<String> getFailingCourses() {
        return failingCourseCodes;
    }
}
