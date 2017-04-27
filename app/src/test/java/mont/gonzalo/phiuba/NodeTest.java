package mont.gonzalo.phiuba;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mont.gonzalo.phiuba.tda.Node;

public class NodeTest {
    @Test
    public void addition_isCorrect() throws Exception {
        HashMap<String, List<String>> menu = new HashMap<>();
        menu.put("entrada", Arrays.asList("omelette", "sopa", "tortilla", "empanadita"));
        menu.put("plato", Arrays.asList("entraña", "canelones", "fideos", "ravioles", "asado"));
        menu.put("postre", Arrays.asList("mouse", "helado", "marquise", "flan", "dulceybatata"));
        menu.put("brebaje", Arrays.asList("te", "cafe", "trago"));
        Node<String> root = new Node<>("MENU");
        Node<String> node = root;
        for (String cat: menu.keySet()) {
            System.out.println("Adding elements of " + cat + ": " + menu.get(cat));
            node.addChildrenData(menu.get(cat));
            node.copyChildrenToSiblings();
            node = node.firstChild();
        }

        Node<String> current = node;
        int count = 0;
        while (current != null) {
            String combination = current.getData() + " ";
            Node<String> parent = current.getParent();
            while (parent != null) {
                combination += parent.getData() + " ";
                parent = parent.getParent();
            }
            System.out.println(combination);
            count++;
            current = current.getRight();
        }
        System.out.println("se hallaron " + count + " combinaciones de menu");
    }
}