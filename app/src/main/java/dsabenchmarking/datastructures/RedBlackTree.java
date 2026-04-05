package dsabenchmarking.datastructures;

import dsabenchmarking.enumeration.Color;
import dsabenchmarking.interfaces.BSTInterface;
import dsabenchmarking.interfaces.ColoredNode;

public class RedBlackTree implements BSTInterface {

    private ColoredNode root;
    private int n;
    private ColoredNode NIL;

    public RedBlackTree() {
        NIL.setColor(Color.BLACK);
        this.root = NIL;
        this.n = 0;
    }

    // TODO: should refactor all this to point to the NIL node
    @Override
    public boolean insert(int v) {
        boolean ll = false, lr = false, rl = false, rr = false, l = false, r = false;

        if (root == null) {
            root = new RBNode(null, null, null, v, Color.BLACK);
            n = 1;
            return true;
        }

        ColoredNode curr = root, parent = null;

        // ge parent node
        while (curr != null) {
            parent = curr;
            if (curr.getValue() > v) {
                curr = curr.getLeft();
            } else if (curr.getValue() < v) {
                curr = curr.getRight();
            } else {
                return false;
            }
        }

        // determine position of inserted node
        ColoredNode newNode = new RBNode(null, null, null, v, Color.RED);
        if (parent.getValue() > v) {
            parent.setLeft(newNode);
            l = true;
        } else {
            parent.setRight(newNode);
            r = true;
        }

        // determine cases
        if (parent.getParent().getLeft() == parent && l) {
            ll = true;
        } else if (parent.getParent().getLeft() == parent && r) {
            lr = true;
        } else if (parent.getParent().getRight() == parent && l) {
            rl = true;
        } else {
            rr = true;
        }

        // handle cases
        if (ll || lr) {

            if (lr) {
                leftRotation(parent);
            }

            if (parent.getParent().getRight() != null && parent.getParent().getRight().getColor() == Color.RED) {
                // set the parent's and uncle's colors to black and traverse upwards
                parent.setColor(Color.BLACK);
                parent.getParent().getRight().setColor(Color.BLACK);

                // alternate colors upwards
                ColoredNode node = parent.getParent();
                while (node.getParent() != null) {
                    if (node.getColor() == Color.BLACK) {
                        node.getParent().setColor(Color.RED);
                    } else {
                        node.getParent().setColor(Color.BLACK);
                    }
                }
            } else {
                // rotate grandparent
                rightRotation(parent.getParent());

                // swap colors of parent and grandparent
                Color tmp = parent.getColor();
                parent.setColor(parent.getParent().getColor());
                parent.getParent().setColor(tmp);
            }
        } else {

            if (rl) {
                rightRotation(parent);
            }

            if (parent.getParent().getLeft() != null && parent.getParent().getLeft().getColor() == Color.RED) {
                // set the parent's and uncle's colors to black and traverse upwards
                parent.setColor(Color.BLACK);
                parent.getParent().getLeft().setColor(Color.BLACK);

                // alternate colors upwards
                ColoredNode node = parent.getParent();
                while (node.getParent() != null) {
                    if (node.getColor() == Color.BLACK) {
                        node.getParent().setColor(Color.RED);
                    } else {
                        node.getParent().setColor(Color.BLACK);
                    }
                }
            } else {
                // rotate grandparent
                leftRotation(parent.getParent());

                // swap colors of parent and grandparent
                Color tmp = parent.getColor();
                parent.setColor(parent.getParent().getColor());
                parent.getParent().setColor(tmp);
            }
        }

        root.setColor(Color.BLACK);

        return true;
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

        }

        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    private void deleteOneChild(ColoredNode target) {
        ColoredNode child = (target.getLeft() == NIL) ? target.getRight() : target.getLeft();

        transplant(target, child);

        if (target.getColor() == Color.BLACK) {
            if (child.getColor() == Color.RED) {
                child.setColor(Color.BLACK);
            } else {
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

        if(isLeftChild(sibling)) {
            rightRotation(doubleBlackNode.getParent());
        } else {
            leftRotation(doubleBlackNode.getParent());
        }

        if (sibling.getParent() == null) {
            root = sibling;
        }
        
        deleteCaseThree(doubleBlackNode);
    }

    private void deleteCaseThree(ColoredNode doubleBlackNode) {
        ColoredNode sibling = findSibling(doubleBlackNode);
        ColoredNode parent = doubleBlackNode.getParent();

        if(parent.getColor() == Color.BLACK
            && sibling.getColor() == Color.BLACK
            && sibling.getLeft().getColor() == Color.BLACK
            && sibling.getRight().getColor() == Color.BLACK
        ) {
            sibling.setColor(Color.RED);
            deleteCaseOne(parent);
        } else {
            deleteCaseFour(doubleBlackNode);
        }
    }

    private void deleteCaseFour(ColoredNode doubleBlackNode) {
        ColoredNode sibling = findSibling(doubleBlackNode);
        ColoredNode parent = doubleBlackNode.getParent();

        if (
            parent.getColor() == Color.RED
            && sibling.getColor() == Color.BLACK
            && sibling.getLeft().getColor() == Color.BLACK
            && sibling.getRight().getColor() == Color.BLACK
        ) {
            sibling.setColor(Color.RED);
            parent.setColor(Color.BLACK);
            return;
        }

        deleteCaseFive(doubleBlackNode);
    }

    private void deleteCaseFive(ColoredNode doubleBlackNode) {
        ColoredNode sibling = findSibling(doubleBlackNode);
        
        if (
            isLeftChild(sibling)
            && sibling.getColor() == Color.BLACK
            && sibling.getLeft().getColor() == Color.RED
            && sibling.getRight().getColor() == Color.BLACK
        ) {
            sibling.getLeft().setColor(Color.BLACK);
            sibling.setColor(Color.RED);
            rightRotation(sibling);
            
        }
        else if (
            !isLeftChild(sibling)
            && sibling.getColor() == Color.BLACK
            && sibling.getLeft().getColor() == Color.BLACK
            && sibling.getRight().getColor() == Color.RED
        ) {
            sibling.getRight().setColor(Color.BLACK);
            sibling.setColor(Color.RED);
            leftRotation(sibling);
        }

        deleteCaseSix(doubleBlackNode);
    }

    private void deleteCaseSix(ColoredNode doubleBlackNode) {
        ColoredNode sibling = findSibling(doubleBlackNode);
        ColoredNode parent = doubleBlackNode.getParent();

        if(isLeftChild(doubleBlackNode)) {
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

        if(sibling.getParent() == null) {
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

    private void rightRotation(ColoredNode parent) {
        ColoredNode child = parent.getLeft();

        // set the parent's left subtree to the child's right subtree
        parent.setLeft(child.getRight());
        if (child.getRight() != null) {
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
        if (child.getLeft() != null) {
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
        if (curr == null)
            return curr;

        if (curr.getValue() > value) {
            return search(curr.getLeft(), value);
        } else if (curr.getValue() < value) {
            return search(curr.getRight(), value);
        }

        return curr;
    }

    private ColoredNode getMinimum(ColoredNode node) {
        ColoredNode min = node;

        while (node != null) {
            min = node;
            node = node.getLeft();
        }

        return min;
    }

    private void transplant(ColoredNode a, ColoredNode b) {
        if (a.getParent() == null) {
            root = b;
        } else {
            b.setParent(a.getParent());

            if (a.getParent().getLeft() == a) {
                a.getParent().setLeft(b);
            } else {
                a.getParent().setRight(b);
            }
        }

        if (a.getLeft() == b) {
            b.setRight(a.getRight());
        } else {
            b.setRight(a.getLeft());
        }
    }
}
