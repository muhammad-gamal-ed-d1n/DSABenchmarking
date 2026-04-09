package dsabenchmarking.datastructures;

import java.util.ArrayList;
import java.util.List;

import dsabenchmarking.enumeration.Color;
import dsabenchmarking.interfaces.BSTInterface;
import dsabenchmarking.interfaces.ColoredNode;

public class RedBlackTree implements BSTInterface {

    private ColoredNode root;
    private int n;
    private ColoredNode NIL;

    public RedBlackTree() {
        NIL = new RBNode(null, null, null, 0, Color.BLACK);
        NIL.setLeft(NIL);
        NIL.setRight(NIL);
        NIL.setParent(NIL);
        this.root = NIL;
        this.n = 0;
    }

    public ColoredNode getRoot() {
        return root;
    }

    public ColoredNode getNIL() {
        return NIL;
    }

@Override
public boolean insert(int value) {
    // 1. Standard BST insert
    ColoredNode newNode = new RBNode(NIL, NIL, null, value, Color.RED);
    if (root == NIL) {
        root = newNode;
        root.setColor(Color.BLACK);
        n++;
        return true;
    }

    ColoredNode curr = root, parent = null;
    while (curr != NIL) {
        parent = curr;
        if (value < curr.getValue()) {
            curr = curr.getLeft();
        } else if (value > curr.getValue()) {
            curr = curr.getRight();
        } else {
            // Value already exists
            return false;
        }
    }

    newNode.setParent(parent);
    if (value < parent.getValue()) {
        parent.setLeft(newNode);
    } else {
        parent.setRight(newNode);
    }

    // 2. Fix Red-Black properties
    insertFixup(newNode);
    n++;
    return true;
}

private void insertFixup(ColoredNode node) {
    while (node.getParent() != null && node.getParent().getColor() == Color.RED) {
        ColoredNode parent = node.getParent();
        ColoredNode grandparent = parent.getParent();

        if (grandparent == null) break;

        if (parent == grandparent.getLeft()) {
            ColoredNode uncle = grandparent.getRight();
            if (uncle.getColor() == Color.RED) {
                // Case 1: recolor
                parent.setColor(Color.BLACK);
                uncle.setColor(Color.BLACK);
                grandparent.setColor(Color.RED);
                node = grandparent;
            } else {
                // Case 2: rotate
                if (node == parent.getRight()) {
                    leftRotation(parent);
                    node = parent;
                    parent = node.getParent();
                }
                // Case 3: rotate and recolor
                parent.setColor(Color.BLACK);
                grandparent.setColor(Color.RED);
                rightRotation(grandparent);
                break;
            }
        } else {
            ColoredNode uncle = grandparent.getLeft();
            if (uncle.getColor() == Color.RED) {
                // Case 1: recolor
                parent.setColor(Color.BLACK);
                uncle.setColor(Color.BLACK);
                grandparent.setColor(Color.RED);
                node = grandparent;
            } else {
                // Case 2: rotate
                if (node == parent.getLeft()) {
                    rightRotation(parent);
                    node = parent;
                    parent = node.getParent();
                }
                // Case 3: rotate and recolor
                parent.setColor(Color.BLACK);
                grandparent.setColor(Color.RED);
                leftRotation(grandparent);
                break;
            }
        }
    }
    root.setColor(Color.BLACK);
}
    @Override
    public boolean delete(int v) {
        // search for node
        ColoredNode target = search(root, v);
        if (target == NIL) {
            return false;
        }

        if (target.getLeft() == NIL || target.getRight() == NIL) {
            deleteOneChild(target);
        } else {
            // find predecessor
            ColoredNode curr = target.getLeft(), pred = curr;
            while (curr != NIL) {
                pred = curr;
                curr = curr.getRight();
            }

            int value = pred.getValue();
            deleteOneChild(pred);
            target.setValue(value);
        }

        n--;
        return true;
    }

    private void deleteOneChild(ColoredNode target) {
        ColoredNode child = (target.getLeft() == NIL) ? target.getRight() : target.getLeft();

        // replace non null child
        replaceNode(target, child);
        if (child.getParent() == null)
            root = child;

        // check if node is black, if true check if child is red
        if (target.getColor() == Color.BLACK) {
            // if child is red, simple case just recolor
            if (child.getColor() == Color.RED) {
                child.setColor(Color.BLACK);
            } else { // else we must correct the violation
                deleteCaseOne(child);
            }
        }
    }

    private void deleteCaseOne(ColoredNode doubleBlackNode) {
        if (doubleBlackNode == root) {
            doubleBlackNode.setColor(Color.BLACK);
            return;
        }

        deleteCaseTwo(doubleBlackNode);
    }

    private void deleteCaseTwo(ColoredNode doubleBlackNode) {
        ColoredNode sibling = findSibling(doubleBlackNode);
        ColoredNode parent = doubleBlackNode.getParent();

        if (parent.getColor() == Color.BLACK
                && sibling.getColor() == Color.RED
                && sibling.getLeft().getColor() == Color.BLACK
                && sibling.getRight().getColor() == Color.BLACK) {
            if (isLeftChild(sibling)) {
                rightRotation(parent);
            } else {
                leftRotation(parent);
            }

            if (sibling.getParent() == null) {
                root = sibling;
            }

            sibling.setColor(Color.BLACK);
            parent.setColor(Color.RED);
        }
        deleteCaseThree(doubleBlackNode);
    }

    private void deleteCaseThree(ColoredNode doubleBlackNode) {
        ColoredNode sibling = findSibling(doubleBlackNode);
        ColoredNode parent = doubleBlackNode.getParent();

        if (parent.getColor() == Color.BLACK
                && sibling.getColor() == Color.BLACK
                && sibling.getLeft().getColor() == Color.BLACK
                && sibling.getRight().getColor() == Color.BLACK) {
            sibling.setColor(Color.RED);
            deleteCaseOne(parent);
        } else {
            deleteCaseFour(doubleBlackNode);
        }
    }

    private void deleteCaseFour(ColoredNode doubleBlackNode) {
        ColoredNode sibling = findSibling(doubleBlackNode);
        ColoredNode parent = doubleBlackNode.getParent();

        if (parent.getColor() == Color.RED
                && sibling.getColor() == Color.BLACK
                && sibling.getLeft().getColor() == Color.BLACK
                && sibling.getRight().getColor() == Color.BLACK) {
            sibling.setColor(Color.RED);
            parent.setColor(Color.BLACK);
            return;
        }

        deleteCaseFive(doubleBlackNode);
    }

    private void deleteCaseFive(ColoredNode doubleBlackNode) {
        ColoredNode sibling = findSibling(doubleBlackNode);

        if (isLeftChild(sibling)
                && sibling.getColor() == Color.BLACK
                && sibling.getLeft().getColor() == Color.RED
                && sibling.getRight().getColor() == Color.BLACK) {
            sibling.getLeft().setColor(Color.BLACK);
            sibling.setColor(Color.RED);
            rightRotation(sibling);

        } else if (!isLeftChild(sibling)
                && sibling.getColor() == Color.BLACK
                && sibling.getLeft().getColor() == Color.BLACK
                && sibling.getRight().getColor() == Color.RED) {
            sibling.getRight().setColor(Color.BLACK);
            sibling.setColor(Color.RED);
            leftRotation(sibling);
        }

        deleteCaseSix(doubleBlackNode);
    }

    private void deleteCaseSix(ColoredNode doubleBlackNode) {
        ColoredNode sibling = findSibling(doubleBlackNode);
        ColoredNode parent = doubleBlackNode.getParent();

        if (isLeftChild(doubleBlackNode)) {
            sibling.setColor(parent.getColor());
            sibling.getRight().setColor(Color.BLACK);
            parent.setColor(Color.BLACK);
            leftRotation(parent);
        } else {
            sibling.setColor(parent.getColor());
            sibling.getLeft().setColor(Color.BLACK);
            parent.setColor(Color.BLACK);
            rightRotation(parent);
        }

        if (sibling.getParent() == null) {
            root = sibling;
        }
    }

    private ColoredNode findSibling(ColoredNode node) {
        if (node.getParent().getLeft() == node) {
            return node.getParent().getRight();
        }
        return node.getParent().getLeft();
    }

    private boolean isLeftChild(ColoredNode node) {
        if (node.getParent() == null) {
            return false;
        }
        return node.getParent().getLeft() == node;
    }

    @Override
    public boolean contains(int v) {
        return search(root, v) != NIL;
    }

    @Override
    public int[] inOrder() {
        List<Integer> array = new ArrayList<>();
        inOrderHelper(root, array);
        return array.stream().mapToInt(i -> i).toArray();
    }

    private void inOrderHelper(ColoredNode root, List<Integer> array) {
        if (root == NIL)
            return;

        inOrderHelper(root.getLeft(), array);
        array.add(root.getValue());
        inOrderHelper(root.getRight(), array);

        return;
    }

    @Override
    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(ColoredNode node) {
        if (node == NIL)
            return -1;

        int left = heightHelper(node.getLeft());
        int right = heightHelper(node.getRight());

        return Math.max(left, right) + 1;
    }

    @Override
    public int size() {
        return n;
    }

    private void rightRotation(ColoredNode parent) {
        ColoredNode child = parent.getLeft();

        // set the parent's left subtree to the child's right subtree
        parent.setLeft(child.getRight());
        if (child.getRight() != NIL) {
            child.getRight().setParent(parent);
        }

        // set the child's parent to the parents parent
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

    private void leftRotation(ColoredNode parent) {
        ColoredNode child = parent.getRight();

        // set the parent's right subtree to the child's left subtree
        parent.setRight(child.getLeft());
        if (child.getLeft() != NIL) {
            child.getLeft().setParent(parent);
        }

        // set the child's parent to the parents parent
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

    private ColoredNode search(ColoredNode curr, int value) {
        if (curr == NIL)
            return curr;

        if (curr.getValue() > value) {
            return search(curr.getLeft(), value);
        } else if (curr.getValue() < value) {
            return search(curr.getRight(), value);
        }

        return curr;
    }

    public void replaceNode(ColoredNode toBeReplaced, ColoredNode child) {
        ColoredNode parent = toBeReplaced.getParent();

        if (parent == null) {
            root = child;
        } else {
            if (isLeftChild(toBeReplaced)) {
                parent.setLeft(child);
            } else {
                parent.setRight(child);
            }
        }
        child.setParent(parent);
    }
}
