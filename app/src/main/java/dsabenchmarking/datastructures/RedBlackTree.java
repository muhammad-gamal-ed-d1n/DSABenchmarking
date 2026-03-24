package dsabenchmarking.datastructures;

import dsabenchmarking.interfaces.BSTInterface;
import dsabenchmarking.interfaces.Node;

public class RedBlackTree implements BSTInterface {

    private Node root;
    private int n;

    public RedBlackTree() {
        this.root = null;
        this.n = 0;
    }

    @Override
    public boolean insert(int v) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public boolean delete(int v) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public boolean contains(int v) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contains'");
    }

    @Override
    public int[] inOrder() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inOrder'");
    }

    @Override
    public int height() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'height'");
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'size'");
    }

    private void rightRotation(Node parent) {
        Node child = parent.getLeft();

        //set the parent's left subtree to the child's right subtree
        parent.setLeft(child.getRight());
        if (child.getRight() != null) {
            child.getRight().setParent(parent);
        }

        //set the child's parent to the parents parent
        child.setParent(parent.getParent());
        if (parent.getParent() == null) { // if no parent then its the root
            root = child;
        } else if (parent.getParent().getLeft() == parent) { // if parent is left child
            parent.getParent().setLeft(child);
        } else { // else its a right child
            parent.getParent().setRight(child);
        }

        // set the child's right to the parent and the parent's parent to the child
        child.setRight(parent);
        parent.setParent(child);
    }

    private void leftRotation(Node parent) {
        Node child = parent.getRight();

        //set the parent's right subtree to the child's left subtree
        parent.setRight(child.getLeft());
        if (child.getLeft() != null) {
            child.getLeft().setParent(parent);
        }

        //set the child's parent to the parents parent
        child.setParent(parent.getParent());
        if (parent.getParent() == null) { // if no parent then its the root
            root = child;
        } else if (parent.getParent().getLeft() == parent) { // if parent is left child
            parent.getParent().setLeft(child);
        } else { // else its a right child
            parent.getParent().setRight(child);
        }

        // set the child's left to the parent and the parent's parent to the child
        child.setLeft(parent);
        parent.setParent(child);
    }
}
