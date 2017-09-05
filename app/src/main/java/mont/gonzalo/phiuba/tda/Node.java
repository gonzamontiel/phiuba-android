package mont.gonzalo.phiuba.tda;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Node<T>{
    private T data = null;
    private List<Node> children = new LinkedList<>();
    private Node left = null;
    private Node right = null;
    private Node parent = null;

    public Node(T data) {
        this.data = data;
    }

    public void addChild(Node child) {
        child.setParent(this);
        children.add(child);
    }

    public void addChild(T data) {
        Node child = new Node(data);
        child.setParent(this);
        children.add(child);
        checkNeighbors();
    }

    public void removeChild(Node<T> tNode) {
        this.children.remove(tNode);
    }

    public void addChildrenData(List<T> newChildrenData) {
        List<Node> nodes = new ArrayList<>();
        for (T t: newChildrenData) {
            nodes.add(new Node(t));
        }
        addChildren(nodes);
    }

    public void addChildren(List<Node> newChildren) {
        for (int i = 0; i < newChildren.size(); i++) {
            Node t = newChildren.get(i);
            t.left = i == 0 ? null : newChildren.get(i-1);
            t.right = (i + 1 < newChildren.size()) ? newChildren.get(i + 1) : null;
            t.setParent(this);
            this.getChildren().add(t);
        }
        checkNeighbors();
    }

    private void checkNeighbors() {
        if (left != null && left.lastChild() != null && !isLeaf()) {
            firstChild().setLeft(left.lastChild());
            left.lastChild().setRight(firstChild());
        }
        if (right != null && right.firstChild() != null && !isLeaf()) {
            lastChild().setRight(right.firstChild());
            right.firstChild().setLeft(lastChild());
        }
    }

    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    public Node firstChild() {
        return isLeaf() ? null : children.get(0);
    }

    public Node lastChild() {
        return isLeaf() ? null : children.get(children.size() - 1);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public void print() {
        System.out.print("Nodo " + this.getData() + ": ");
        for (Node<T> child : getChildren()) {
            System.out.print(child.getData() + " ");
        }
        System.out.println();
        for (Node<T> child : getChildren()) {
            child.print();
        }
    }

    public void copyChildrenToSiblings() {
        Node next = this.right;
        List<Node> newChildren;
        while (next != null) {
            newChildren = new ArrayList<>();
            next.getChildren().clear();
            for (Node child: this.getChildren()) {
                newChildren.add(new Node(child.getData()));
            }
            next.addChildren(newChildren);
            next = next.right;
        }
    }

    public void removeFromLeafs() {
        if (!isLeaf()) {
            return;
        }
        if (getRight() != null) {
            getRight().setLeft(getLeft());
        }
        if (getLeft() != null) {
            getLeft().setRight(getRight());
        }
        getParent().removeChild(this);
    }
}