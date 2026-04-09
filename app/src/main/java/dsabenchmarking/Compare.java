package dsabenchmarking;

import dsabenchmarking.algorithm.QuickSort;
import dsabenchmarking.datastructures.BinarySearchTree;
import dsabenchmarking.datastructures.RedBlackTree;
import dsabenchmarking.interfaces.BSTInterface;
import dsabenchmarking.services.CSVWriter;
import dsabenchmarking.services.Generator;

public class Compare {

    public static void main(String[] args) {
        CSVWriter writer;

        try {

            writer = new CSVWriter("results.csv");
            int[] sizes = { 100, 1000, 10000, 20000, 30000, 60000, 80000, 100000 };

            for (int size : sizes) {
                int[] random = Generator.generateArray(size);
                int[] nearly1 = Generator.generateArray(size, 1);
                int[] nearly5 = Generator.generateArray(size, 5);
                int[] nearly10 = Generator.generateArray(size, 10);

                runSortBench(writer, "Random", random, size);
                runSortBench(writer, "Nearly1%", nearly1, size);
                runSortBench(writer, "Nearly5%", nearly5, size);
                runSortBench(writer, "Nearly10%", nearly10, size);
            }

            writer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static long benchmark(int[] arr, Runnable sort) {
        long start = System.nanoTime();
        sort.run();
        long time = System.nanoTime() - start;
        return time;
    }

    private static void runSortBench(CSVWriter csv, String type, int[] input, int size) throws Exception {
        RedBlackTree rbt = new RedBlackTree();
        BinarySearchTree bst = new BinarySearchTree();
        QuickSort quickSort = new QuickSort();

        int[] copy;

        copy = input.clone();

        // RBSort
        long t1 = benchmark(copy, () ->treeSort(rbt, copy));
        csv.write("RBSort", type, size, t1);

        // BSTSort
        long t2 = benchmark(copy, () -> treeSort(bst, copy));
        csv.write("BSTSort", type, size, t2);


        // QuickSort
        long t3 = benchmark(copy, () -> quickSort.quickSort(copy, 0, copy.length));
        csv.write("QuickSort", type, size, t3);



    }

    public static void treeSort(BSTInterface tree, int[] arr) {
        for (int elem: arr) {
            tree.insert(elem);
        }
        tree.inOrder();
    }
}
