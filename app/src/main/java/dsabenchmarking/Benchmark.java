package dsabenchmarking;

import dsabenchmarking.datastructures.BinarySearchTree;
import dsabenchmarking.datastructures.RedBlackTree;
import dsabenchmarking.services.Generator;
import dsabenchmarking.services.ValidateBST;
import dsabenchmarking.services.ValidateRBT;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Benchmark {

    // public static void main(String[] args) {
    // RedBlackTree tree = new RedBlackTree();
    // ASTPrinter printer = new ASTPrinter(tree.getNIL());
    // for (int i = 0; i < 10; i++) {
    // tree.insert(i);
    // }
    // printer.printTree(tree.getRoot());
    // System.out.println(tree.height());
    // int[] res = tree.inOrder();
    // System.out.println(Arrays.toString(res));
    // }

    public static void main(String[] args) {
        int n = 100000;

        ValidateBST.VALIDATE = false;
        ValidateRBT.VALIDATE = false;

        int[] random = Generator.generateArray(n);
        int[] nearlySorted1 = Generator.generateArray(n, 1);
        int[] nearlySorted5 = Generator.generateArray(n, 5);
        int[] nearlySorted10 = Generator.generateArray(n, 10);

        runAll("Fully Random", random);
        runAll("Nearly Sorted (1%)", nearlySorted1);
        runAll("Nearly Sorted (5%)", nearlySorted5);
        runAll("Nearly Sorted (10%)", nearlySorted10);
    }

    private static void runAll(String label, int[] input) {
        log.warn("===============================");
        log.warn("{}", label);
        log.warn("===============================");

        int runs = 5;

        long[] rbtInsertTimes = new long[runs];
        long[] bstInsertTimes = new long[runs];

        long[] rbtSearchTimes = new long[runs];
        long[] bstSearchTimes = new long[runs];

        long[] rbtDeleteTimes = new long[runs];
        long[] bstDeleteTimes = new long[runs];

        long[] rbtSortTimes = new long[runs];
        long[] bstSortTimes = new long[runs];

        int rbtHeight = 0, bstHeight = 0;

        for (int run = 0; run < runs; run++) {

            RedBlackTree rbt = new RedBlackTree();
            BinarySearchTree bst = new BinarySearchTree();

            // -------- INSERT --------
            rbtInsertTimes[run] = time(() -> {
                for (int v : input) {
                    rbt.insert(v);
                }
                ValidateRBT.validateRBT(rbt);
            });

            bstInsertTimes[run] = time(() -> {
                for (int v : input) {
                    bst.insert(v);
                }
                ValidateBST.validateBST(bst);
            });

            if (run == runs - 1) {
                rbtHeight = rbt.height();
                bstHeight = bst.height();
            }

            // -------- CONTAINS --------
            int[] queries = new int[100000];

            for (int i = 0; i < 50000; i++) {
                queries[i] = input[i];
            }
            for (int i = 50000; i < 100000; i++) {
                queries[i] = Integer.MAX_VALUE - i;
            }

            rbtSearchTimes[run] = time(() -> {
                for (int q : queries)
                    rbt.contains(q);
            });

            bstSearchTimes[run] = time(() -> {
                for (int q : queries)
                    bst.contains(q);
            });

            // -------- DELETE --------
            int deleteCount = input.length / 5;
            int[] toDelete = new int[deleteCount];

            for (int i = 0; i < deleteCount; i++) {
                toDelete[i] = input[i];
            }

            rbtDeleteTimes[run] = time(() -> {
                for (int v : toDelete) {
                    rbt.delete(v);
                }
                ValidateRBT.validateRBT(rbt);
            });

            bstDeleteTimes[run] = time(() -> {
                for (int v : toDelete) {
                    bst.delete(v);
                }
                ValidateBST.validateBST(bst);
            });

            // -------- TREE SORT --------
            rbtSortTimes[run] = time(() -> {
                RedBlackTree t = new RedBlackTree();
                for (int v : input) {
                    t.insert(v);
                }
                ValidateRBT.validateRBT(t);
                t.inOrder();
            });

            bstSortTimes[run] = time(() -> {
                BinarySearchTree t = new BinarySearchTree();
                for (int v : input) {
                    t.insert(v);
                }
                ValidateBST.validateBST(t);
                t.inOrder();
            });
        }

        logStats("Insert", rbtInsertTimes, bstInsertTimes);
        log.warn("Heights → RBT: {} | BST: {}", rbtHeight, bstHeight);

        logStats("Contains", rbtSearchTimes, bstSearchTimes);
        logStats("Delete", rbtDeleteTimes, bstDeleteTimes);
        logStats("Tree Sort", rbtSortTimes, bstSortTimes);
    }

    private static long time(Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        return end - start;
    }

    private static void logStats(String op, long[] rbt, long[] bst) {
        double rbtMean = mean(rbt);
        double bstMean = mean(bst);

        log.warn("--- {} ---", op);

        log.warn("RBT mean: {} ms", rbtMean / 1e6);
        log.warn("BST mean: {} ms", bstMean / 1e6);

        log.warn("RBT median: {} ms", median(rbt) / 1e6);
        log.warn("BST median: {} ms", median(bst) / 1e6);

        log.warn("RBT std: {} ms", std(rbt) / 1e6);
        log.warn("BST std: {} ms", std(bst) / 1e6);

        log.warn("Speedup (BST / RBT): {}", (bstMean / rbtMean));
    }

    private static double mean(long[] arr) {
        double sum = 0;
        for (long v : arr)
            sum += v;
        return sum / arr.length;
    }

    private static double median(long[] arr) {
        long[] copy = arr.clone();
        java.util.Arrays.sort(copy);
        int mid = copy.length / 2;
        return (copy.length % 2 == 0)
                ? (copy[mid - 1] + copy[mid]) / 2.0
                : copy[mid];
    }

    private static double std(long[] arr) {
        double m = mean(arr);
        double sum = 0;
        for (long v : arr) {
            sum += (v - m) * (v - m);
        }
        return Math.sqrt(sum / arr.length);
    }
}
