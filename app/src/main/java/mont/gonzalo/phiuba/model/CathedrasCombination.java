package mont.gonzalo.phiuba.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mont.gonzalo.phiuba.tda.Node;

/**
 * Created by Gonzalo Montiel on 3/20/17.
 */
public class CathedrasCombination implements Serializable {
    private HashMap<String, List<Cathedra>> cathedrasByCourse;
    private HashMap<String, Cathedra> cathedras;
    private List<Cathedra> all;
    private List<Cathedra> pinnedCathedras;
    private Node<String> root;
    private Node<String> leftmostLeaf = null;

    private static CathedrasCombination _instance;

    public static CathedrasCombination getInstance() {
        if (_instance == null) {
            _instance = new CathedrasCombination();
        }
        return _instance;
    }

    private CathedrasCombination() {
        cathedrasByCourse = UserCourses.getInstance().getStudiyngCoursesWithCathedras();
        cathedras = new HashMap<>();
    }

    public void buildTree() {
        cathedras.clear();
        root = new Node<>(null);
        Node<String> node = root;
        for (String code: cathedrasByCourse.keySet()) {
            List<String> names = new ArrayList<>();
            for (Cathedra cat: cathedrasByCourse.get(code)) {
                String key = code + " - " + cat.getTeachers();
                cathedras.put(key, cat);
                names.add(key);
            }
            node.addChildrenData(names);
            node.copyChildrenToSiblings();
            node = node.firstChild();
        }
        leftmostLeaf = node;
    }

    public List<Cathedra> getAtPosition(int n) {
        Node<String> nodeToFind = leftmostLeaf;
        List<Cathedra> cathedrasComb = new ArrayList<>();
        int count = 1;
        while (nodeToFind != null && count++ < n) {
            nodeToFind = nodeToFind.getRight();
        }
        Node<String> parent = nodeToFind;
        while (parent != null) {
            cathedrasComb.add(cathedras.get(parent.getData()));
            parent = parent.getParent();
        }
        return cathedrasComb;
    }

    public void print() {
        root.print();
    }

    private boolean schedulesCollision(List<Cathedra> cathedrasComb) {
        return false;
    }

    public int getCombinationCount() {
        return 5;
    }
}
