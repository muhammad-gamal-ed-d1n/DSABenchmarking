package dsabenchmarking.datastructures;

import java.util.ArrayList;
import java.util.List;

import dsabenchmarking.interfaces.BSTInterface;
import dsabenchmarking.interfaces.Node;

public class BinarySearchTree implements BSTInterface {
    private Node root;
    private int size = 0;

    public Node getRoot() {
        return root;
    }

    @Override
    public boolean insert(int v) {
        // if first insertion then set root
        if (root == null) {
            root = new BSTNode(null, null, null, v);
            size++;
            return true;
        }

        // else perform normal insertion
        Node curr = root, parent = root;
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

        // place node
        Node child = new BSTNode(null, null, null, v);
        child.setParent(parent);
        if (parent.getValue() > v) {
            parent.setLeft(child);
        } else {
            parent.setRight(child);
        }

        size++;
        return true;
    }

    @Override
    public boolean delete(int v) {

        Node toBeDeleted = search(root, v);
        if (toBeDeleted == null)
            return false;

        if (size == 1) {
            root = null;
            size--;
        }

        Node parent = toBeDeleted.getParent();
        if (toBeDeleted.getLeft() == null) {
            Node child = toBeDeleted.getRight();
            if (parent != null) {
                if (isLeftChild(toBeDeleted)) {
                    parent.setLeft(child);
                } else {
                    parent.setRight(child);
                }
            }
            if (child != null) child.setParent(parent);
        } else if (toBeDeleted.getRight() == null) {
            Node child = toBeDeleted.getLeft();
            if (parent != null) {
                if (isLeftChild(toBeDeleted)) {
                    parent.setLeft(child);
                } else {
                    parent.setRight(child);
                }
            }
            if (child != null) child.setParent(parent);
        } else {
            // we find the maximum node in the left subtree (inorder predecessor)
            Node curr = toBeDeleted.getLeft(), pred = toBeDeleted.getLeft();
            while (curr != null) {
                pred = curr;
                curr = curr.getRight();
            }

            if (isLeftChild(pred)) {
                pred.getParent().setLeft(null);
            } else {
                pred.getParent().setRight(null);
            }

            toBeDeleted.setValue(pred.getValue());
            pred = null;
        }

        size--;
        return true;
    }

    public Node search(Node root, int v) {
        // if node is null return null
        if (root == null)
            return null;

        // else recurse
        if (root.getValue() > v) {
            return search(root.getLeft(), v);
        } else if (root.getValue() < v) {
            return search(root.getRight(), v);
        }

        // found
        return root;
    }

    @Override
    public boolean contains(int v) {
        return search(root, v) != null;
    }

    @Override
    public int[] inOrder() {
        List<Integer> inOrder = new ArrayList<>();
        Node curr = root;
        return inOrderHelper(curr, inOrder);
    }

    private int[] inOrderHelper(Node root, List<Integer> inOrder) {
        if (root == null) return inOrder();

        inOrder.add(root.getValue());
        inOrderHelper(root.getLeft(), inOrder);
        inOrderHelper(root.getRight(), inOrder);

        return inOrder();
    }

    @Override
    public int height() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'height'");
    }

    @Override
    public int size() {
        return size;
    }

    private boolean isLeftChild(Node node) {
        if (node.getParent() == null) {
            return false;
        }
        return node.getParent().getLeft() == node;
    }
}
