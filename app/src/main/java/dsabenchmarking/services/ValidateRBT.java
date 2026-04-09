package dsabenchmarking.services;

import dsabenchmarking.datastructures.RedBlackTree;
import dsabenchmarking.enumeration.Color;
import dsabenchmarking.interfaces.ColoredNode;

public class ValidateRBT {
    public static boolean VALIDATE = false;

    public static void validateRBT(RedBlackTree tree) {
        if (!VALIDATE)
            return;

        isBST(tree.getRoot(), Integer.MIN_VALUE, Integer.MAX_VALUE, tree.getNIL());
        checkNode(tree.getRoot(), tree.getNIL());
        // computeBlackHeight(tree.getRoot(), tree.getNIL());
    }

    private static boolean isBST(ColoredNode node, int min, int max, ColoredNode NIL) {
        if (node == NIL)
            return true;

        int v = node.getValue();
        if (v <= min || v >= max)
            return false;

        return isBST(node.getLeft(), min, v, NIL) && isBST(node.getRight(), v, max, NIL);
    }

    private static int computeBlackHeight(ColoredNode node, ColoredNode NIL) {
        if (node == NIL)
            return 1; // NIL leaves counted as black
        int left = computeBlackHeight(node.getLeft(), NIL);
        int right = computeBlackHeight(node.getRight(), NIL);
        if (left != right)
            throw new IllegalStateException("Black height violation");
        return left + (node.getColor() == Color.BLACK ? 1 : 0);
    }

    private static void checkNode(ColoredNode node, ColoredNode NIL) {
        if (node == NIL)
            return;

        if (node.getColor() == Color.RED) {
            if ((node.getLeft() != null && node.getLeft().getColor() == Color.RED) ||
                    (node.getRight() != null && node.getRight().getColor() == Color.RED)) {
                throw new IllegalStateException("Red node has red child");
            }
        }

        checkNode(node.getLeft(), NIL);
        checkNode(node.getRight(), NIL);
    }
}
