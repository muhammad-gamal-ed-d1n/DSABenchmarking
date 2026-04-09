package dsabenchmarking.services;

import dsabenchmarking.datastructures.BinarySearchTree;
import dsabenchmarking.interfaces.Node;

public class ValidateBST {
    public static boolean VALIDATE = false;

    public static void validateBST(BinarySearchTree tree) {
        if (!VALIDATE) return;

        isBST(tree.getRoot(), Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static boolean isBST(Node node, int min, int max) {
        if (node == null) return true;
        
        int v  = node.getValue();
        if (v <= min || v >= max) return false;

        return isBST(node.getLeft(), min, v) && isBST(node.getRight(), v, max);
    }
}
