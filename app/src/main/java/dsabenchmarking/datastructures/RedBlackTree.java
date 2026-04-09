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
                // ingore duplicate
                return false;
            }
        }

        newNode.setParent(parent);
        if (value < parent.getValue()) {
            parent.setLeft(newNode);
        } else {
            parent.setRight(newNode);
        }

        insertFixup(newNode);
        n++;
        return true;
    }

    private void insertFixup(ColoredNode node) {
        while (node.getParent() != null && node.getParent().getColor() == Color.RED) {
            ColoredNode parent = node.getParent();
            ColoredNode grandparent = parent.getParent();

            if (grandparent == null)
                break;

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
    public boolean delete(int value) {
        ColoredNode z = search(root, value);
        if (z == NIL)
            return false;

        ColoredNode y = z;
        Color yOriginalColor = y.getColor();
        ColoredNode x;

        if (z.getLeft() == NIL) {
            x = z.getRight();
            transplant(z, z.getRight());
        } else if (z.getRight() == NIL) {
            x = z.getLeft();
            transplant(z, z.getLeft());
        } else {
            y = minimum(z.getRight());
            yOriginalColor = y.getColor();
            x = y.getRight();

            if (y.getParent() == z) {
                x.setParent(y);
            } else {
                transplant(y, y.getRight());
                y.setRight(z.getRight());
                y.getRight().setParent(y);
            }

            transplant(z, y);
            y.setLeft(z.getLeft());
            y.getLeft().setParent(y);
            y.setColor(z.getColor());
        }

        if (yOriginalColor == Color.BLACK) {
            deleteFixup(x);
        }

        n--;
        return true;
    }

    private void deleteFixup(ColoredNode x) {
        while (x != root && x.getColor() == Color.BLACK) {
            if (x == x.getParent().getLeft()) {
                ColoredNode w = x.getParent().getRight();

                if (w.getColor() == Color.RED) {
                    w.setColor(Color.BLACK);
                    x.getParent().setColor(Color.RED);
                    leftRotation(x.getParent());
                    w = x.getParent().getRight();
                }

                if (w.getLeft().getColor() == Color.BLACK &&
                        w.getRight().getColor() == Color.BLACK) {
                    w.setColor(Color.RED);
                    x = x.getParent();
                } else {
                    if (w.getRight().getColor() == Color.BLACK) {
                        w.getLeft().setColor(Color.BLACK);
                        w.setColor(Color.RED);
                        rightRotation(w);
                        w = x.getParent().getRight();
                    }

                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(Color.BLACK);
                    w.getRight().setColor(Color.BLACK);
                    leftRotation(x.getParent());
                    x = root;
                }
            } else {
                // mirror case
                ColoredNode w = x.getParent().getLeft();

                if (w.getColor() == Color.RED) {
                    w.setColor(Color.BLACK);
                    x.getParent().setColor(Color.RED);
                    rightRotation(x.getParent());
                    w = x.getParent().getLeft();
                }

                if (w.getRight().getColor() == Color.BLACK &&
                        w.getLeft().getColor() == Color.BLACK) {
                    w.setColor(Color.RED);
                    x = x.getParent();
                } else {
                    if (w.getLeft().getColor() == Color.BLACK) {
                        w.getRight().setColor(Color.BLACK);
                        w.setColor(Color.RED);
                        leftRotation(w);
                        w = x.getParent().getLeft();
                    }

                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(Color.BLACK);
                    w.getLeft().setColor(Color.BLACK);
                    rightRotation(x.getParent());
                    x = root;
                }
            }
        }
        x.setColor(Color.BLACK);
    }

    private void transplant(ColoredNode u, ColoredNode v) {
        if (u.getParent() == null) {
            root = v;
        } else if (u == u.getParent().getLeft()) {
            u.getParent().setLeft(v);
        } else {
            u.getParent().setRight(v);
        }
        v.setParent(u.getParent());
    }

    private ColoredNode minimum(ColoredNode node) {
        while (node.getLeft() != NIL) {
            node = node.getLeft();
        }
        return node;
    }

    // @Override
    // public boolean delete(int v) {

    // if (deleteHelper(root, v)) {
    // n--;
    // return true;
    // }

    // return false;
    // }

    // private boolean deleteHelper(ColoredNode node, int v) {
    // if (node == null || node == NIL)
    // return false;

    // if (node.getValue() == v) {

    // if (node.getLeft() == NIL || node.getRight() == NIL) {
    // deleteOneChild(node);
    // } else {
    // // find predecessor
    // ColoredNode curr = node.getLeft(), pred = curr;
    // while (curr != NIL) {
    // pred = curr;
    // curr = curr.getRight();
    // }

    // int value = pred.getValue();
    // node.setValue(value);
    // deleteHelper(node.getLeft(), value);
    // }

    // return true;
    // }

    // if (node.getValue() < v) {
    // return deleteHelper(node.getRight(), v);
    // } else {
    // return deleteHelper(node.getLeft(), v);
    // }
    // }

    // private void deleteOneChild(ColoredNode target) {
    // ColoredNode child = (target.getLeft() == NIL) ? target.getRight() :
    // target.getLeft();

    // // replace non null child
    // replaceNode(target, child);
    // if (child.getParent() == null)
    // root = child;

    // // check if node is black, if true check if child is red
    // if (target.getColor() == Color.BLACK) {
    // // if child is red, simple case just recolor
    // if (child.getColor() == Color.RED) {
    // child.setColor(Color.BLACK);
    // } else { // else we must correct the violation
    // deleteCaseOne(child);
    // }
    // }
    // }

    // private void deleteCaseOne(ColoredNode doubleBlackNode) {
    // if (doubleBlackNode.getParent() == null) {
    // root = doubleBlackNode;
    // return;
    // }

    // deleteCaseTwo(doubleBlackNode);
    // }

    // private void deleteCaseTwo(ColoredNode doubleBlackNode) {
    // ColoredNode sibling = findSibling(doubleBlackNode);
    // ColoredNode parent = doubleBlackNode.getParent();

    // if (sibling.getColor() == Color.RED) {
    // if (isLeftChild(sibling)) {
    // rightRotation(parent);
    // } else {
    // leftRotation(parent);
    // }

    // if (sibling.getParent() == null) {
    // root = sibling;
    // }

    // Color tmp = parent.getColor();
    // parent.setColor(sibling.getColor());
    // sibling.setColor(tmp);
    // }
    // deleteCaseThree(doubleBlackNode);
    // }

    // private void deleteCaseThree(ColoredNode doubleBlackNode) {
    // ColoredNode sibling = findSibling(doubleBlackNode);
    // ColoredNode parent = doubleBlackNode.getParent();

    // if (parent.getColor() == Color.BLACK
    // && sibling.getColor() == Color.BLACK
    // && sibling.getLeft().getColor() == Color.BLACK
    // && sibling.getRight().getColor() == Color.BLACK) {
    // sibling.setColor(Color.RED);
    // deleteCaseOne(parent);
    // } else {
    // deleteCaseFour(doubleBlackNode);
    // }
    // }

    // private void deleteCaseFour(ColoredNode doubleBlackNode) {
    // ColoredNode sibling = findSibling(doubleBlackNode);
    // ColoredNode parent = doubleBlackNode.getParent();

    // if (sibling.getColor() == Color.BLACK) {
    // if (isLeftChild(doubleBlackNode)
    // && sibling.getRight().getColor() == Color.BLACK
    // && sibling.getLeft().getColor() == Color.RED
    // ) {
    // leftRotation(parent);

    // Color tmp = parent.getColor();
    // parent.setColor(sibling.getColor());
    // sibling.setColor(tmp);
    // } else if (
    // !isLeftChild(doubleBlackNode)
    // && sibling.getLeft().getColor() == Color.BLACK
    // && sibling.getRight().getColor() == Color.RED
    // ) {
    // Color tmp = parent.getColor();
    // parent.setColor(sibling.getColor());
    // sibling.setColor(tmp);
    // }
    // sibling.setColor(Color.RED);
    // parent.setColor(Color.BLACK);
    // return;
    // }

    // deleteCaseFive(doubleBlackNode);
    // }

    // private void deleteCaseFive(ColoredNode doubleBlackNode) {
    // ColoredNode sibling = findSibling(doubleBlackNode);

    // if (isLeftChild(sibling)
    // && sibling.getColor() == Color.BLACK
    // && sibling.getLeft().getColor() == Color.RED
    // && sibling.getRight().getColor() == Color.BLACK) {
    // sibling.getLeft().setColor(Color.BLACK);
    // sibling.setColor(Color.RED);
    // rightRotation(sibling);

    // } else if (!isLeftChild(sibling)
    // && sibling.getColor() == Color.BLACK
    // && sibling.getLeft().getColor() == Color.BLACK
    // && sibling.getRight().getColor() == Color.RED) {
    // sibling.getRight().setColor(Color.BLACK);
    // sibling.setColor(Color.RED);
    // leftRotation(sibling);
    // }

    // deleteCaseSix(doubleBlackNode);
    // }

    // private void deleteCaseSix(ColoredNode doubleBlackNode) {
    // ColoredNode sibling = findSibling(doubleBlackNode);
    // ColoredNode parent = doubleBlackNode.getParent();

    // if (isLeftChild(doubleBlackNode)) {
    // sibling.setColor(parent.getColor());
    // sibling.getRight().setColor(Color.BLACK);
    // parent.setColor(Color.BLACK);
    // leftRotation(parent);
    // } else {
    // sibling.setColor(parent.getColor());
    // sibling.getLeft().setColor(Color.BLACK);
    // parent.setColor(Color.BLACK);
    // rightRotation(parent);
    // }

    // if (sibling.getParent() == null) {
    // root = sibling;
    // }
    // }

    // private ColoredNode findSibling(ColoredNode node) {
    //     if (node.getParent().getLeft() == node) {
    //         return node.getParent().getRight();
    //     }
    //     return node.getParent().getLeft();
    // }

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
