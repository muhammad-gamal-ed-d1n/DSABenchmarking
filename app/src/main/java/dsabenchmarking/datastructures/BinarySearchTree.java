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
            return true;
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
            if (child != null)
                child.setParent(parent);
            if (toBeDeleted == root) {
                root = child;
            }
        } else if (toBeDeleted.getRight() == null) {
            Node child = toBeDeleted.getLeft();
            if (parent != null) {
                if (isLeftChild(toBeDeleted)) {
                    parent.setLeft(child);
                } else {
                    parent.setRight(child);
                }
            }
            if (child != null)
                child.setParent(parent);

            if (toBeDeleted == root) {
                root = child;
            }
        } else {
            // we find the maximum node in the left subtree (inorder predecessor)
            Node curr = toBeDeleted.getLeft(), pred = toBeDeleted.getLeft();
            while (curr != null) {
                pred = curr;
                curr = curr.getRight();
            }

            int value = pred.getValue();
            toBeDeleted.setValue(value);

            // delete predecessor
            if (pred.getLeft() != null) {
                Node predParent = pred.getParent();
                if (isLeftChild(pred)) {
                    pred = null;
                } else {
                    predParent.setRight(pred.getLeft());
                    if (pred.getLeft() != null) {
                        pred.getLeft().setParent(predParent);
                    }
                }
            } else {
                if (isLeftChild(pred)) {
                    pred.getParent().setLeft(null);
                } else {
                    pred.getParent().setRight(null);
                }
            }
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
        List<Integer> array = new ArrayList<>();
        inOrderHelper(root, array);
        return array.stream().mapToInt(i -> i).toArray();
    }

    private void inOrderHelper(Node root, List<Integer> array) {
        if (root == null) return;

        inOrderHelper(root.getLeft(), array);
        array.add(root.getValue());
        inOrderHelper(root.getRight(), array);

        return;
    }

    @Override
    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(Node node) {
        if (node == null)
            return -1;

        int left = heightHelper(node.getLeft());
        int right = heightHelper(node.getRight());

        return Math.max(left, right) + 1;
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
