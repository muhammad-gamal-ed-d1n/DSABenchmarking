package dsabenchmarking;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dsabenchmarking.datastructures.RedBlackTree;
import dsabenchmarking.enumeration.Color;
import dsabenchmarking.interfaces.ColoredNode;

public class RedBlackTreeTest {

    RedBlackTree tree;

    @BeforeEach
    void setUp() {
        tree = new RedBlackTree();
    }

    @Test
    void testInsertionAndInOrder() {
        tree.insert(3);
        tree.insert(1);
        tree.insert(5);
        tree.insert(2);

        int[] expected = {1, 2, 3, 5};
        assertArrayEquals(expected, tree.inOrder(), "in-order traversal should return sorted elements");
    }

    @Test
    void testEdgeCases() {
        tree.insert(10);
        tree.insert(10); // duplicate
        int[] expected = {10};
        assertArrayEquals(expected, tree.inOrder(), "duplicates should be ignored");

        tree.delete(10);
        assertEquals(0, tree.inOrder().length, "tree should be empty after deleting last element");

        tree.delete(10); // delete non-existent
        assertArrayEquals(new int[]{}, tree.inOrder(), "deleting from empty tree should do nothing");
    }

    @Test
    void queries() {
        boolean[] results = new boolean[1000];

        for (int i = 0; i < 500; i++) tree.insert(i);

        for (int i = 0; i < 500; i++) results[i] = tree.contains(i);
        for (int i = 500; i < 1000; i++) results[i] = tree.contains(i);

        boolean[] expected = new boolean[1000];
        for (int i = 0; i < 1000; i++) expected[i] = (i < 500);

        assertArrayEquals(expected, results, "issue with queries");
    }

    @Test
    void testRootIsBlack() {
        for (int i = 0; i < 1000; i++) {
            tree.insert(i);
            assertEquals(Color.BLACK, tree.getRoot().getColor(), "root must always be black");
        }
    }

    @Test
    void testNoConsecutiveReds() {
        for (int i = 0; i < 1000; i++) {
            tree.insert(i);
        }
        checkNoConsecutiveReds(tree.getRoot());
    }

    private void checkNoConsecutiveReds(ColoredNode node) {
        if (node == tree.getNIL()) return;
        if (node.getColor() == Color.RED) {
            if (node.getLeft() != tree.getNIL())
                assertEquals(Color.BLACK, node.getLeft().getColor(), "red node cannot have red left child");
            if (node.getRight() != tree.getNIL())
                assertEquals(Color.BLACK, node.getRight().getColor(), "red node cannot have red right child");
        }
        checkNoConsecutiveReds(node.getLeft());
        checkNoConsecutiveReds(node.getRight());
    }

    @Test
    void testBlackHeightConsistency() {
        for (int i = 0; i < 1000; i++) {
            tree.insert(i);
        }
        int blackHeight = getBlackHeight(tree.getRoot());
        assertTrue(blackHeight >= 0, "black-height should be consistent");
    }

    private int getBlackHeight(ColoredNode node) {
        if (node == tree.getNIL()) return 0;
        int left = getBlackHeight(node.getLeft());
        int right = getBlackHeight(node.getRight());
        assertEquals(left, right, "all paths must have the same number of black nodes");
        return left + (node.getColor() == Color.BLACK ? 1 : 0);
    }

    @Test
    void testDeleteRoot() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.delete(10);
        assertEquals(Color.BLACK, tree.getRoot().getColor(), "root after deletion must be black");
    }

    @Test
    void testStressInsertionAndInOrder() {
        int n = 1000;
        for (int i = 0; i < n; i++) tree.insert(i);

        int[] inOrder = tree.inOrder();
        for (int i = 0; i < n; i++) assertEquals(i, inOrder[i], "in-order should give sorted sequence");
    }

    @Test
    void testDeleteLeafAndInternalNode() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(12);
        tree.delete(5); // leaf
        tree.delete(15); // internal with one child
        int[] expected = {10, 12};
        assertArrayEquals(expected, tree.inOrder(), "after deletions, in-order must be correct");
    }
}