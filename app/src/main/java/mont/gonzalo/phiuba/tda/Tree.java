package mont.gonzalo.phiuba.tda;

public class Tree<T>{
    private Node<T> root;

    public Tree(T data) {
        this.root = new Node<>(data);
    }

    public Node<T> getRoot() {
        return root;
    }

    public void print() {
        for (Node<T> child : root.getChildren()) {
            System.out.print(child.getData() + " ");
        }
        System.out.println();
        for (Node<T> child : root.getChildren()) {
            child.print();
        }
    }
}