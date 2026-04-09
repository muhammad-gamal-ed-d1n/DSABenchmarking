package dsabenchmarking;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dsabenchmarking.datastructures.BinarySearchTree;

public class BinarySearchTreeTest {

    BinarySearchTree tree;

    @BeforeEach
    void setUp() {
        tree = new BinarySearchTree();
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

    @Test
    void testInsertionOfElements() {
        int n = 100;
        for (int i = 0; i < n; i++) tree.insert(i * 2); // even numbers

        int[] inOrder = tree.inOrder();
        assertEquals(n, inOrder.length, "tree should contain 100 elements");

        for (int i = 0; i < n; i++) assertEquals(i * 2, inOrder[i], "in-order mismatch for 100 elements");
    }

    @Test
    void testInsertionOfElementsRandomOrder() {
        int n = 1000;
        Random rand = new Random(42732198);
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = rand.nextInt(10000);
            tree.insert(values[i]);
        }

        int[] inOrder = tree.inOrder();
        // verify sorted
        for (int i = 1; i < inOrder.length; i++) {
            assertTrue(inOrder[i-1] <= inOrder[i], "in-order traversal should be sorted");
        }
    }

    @Test
    void testDeleteManyElements() {
        int n = 200;
        for (int i = 0; i < n; i++) tree.insert(i);

        // delete even numbers
        for (int i = 0; i < n; i += 2) tree.delete(i);

        int[] inOrder = tree.inOrder();
        for (int i = 0; i < inOrder.length; i++) {
            assertEquals(i * 2 + 1, inOrder[i], "in-order after deletion of evens is incorrect");
        }
    }

    @Test
    void testStressInsertDelete() {
        int n = 500;
        for (int i = 0; i < n; i++) tree.insert(i);

        for (int i = 0; i < n; i += 3) tree.delete(i); // delete multiples of 3

        int[] inOrder = tree.inOrder();
        for (int i = 0; i < inOrder.length; i++) {
            assertNotEquals(0, inOrder[i] % 3, "deleted multiples of 3 should not exist");
        }
    }

    @Test
    void testDuplicatesIgnored() {
        int n = 50;
        for (int i = 0; i < n; i++) tree.insert(i);
        for (int i = 0; i < n; i++) tree.insert(i); // duplicate inserts

        int[] inOrder = tree.inOrder();
        assertEquals(n, inOrder.length, "duplicates should not increase tree size");

        for (int i = 0; i < n; i++) assertEquals(i, inOrder[i], "in-order mismatch with duplicates");
    }
}