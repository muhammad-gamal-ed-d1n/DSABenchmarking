package dsabenchmarking;

import dsabenchmarking.datastructures.BinarySearchTree;
import dsabenchmarking.datastructures.RedBlackTree;
import dsabenchmarking.services.Generator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public String getGreeting() {
        return "hello";
    }

    public static void main(String[] args) {
        // trees
        RedBlackTree rbtree = new RedBlackTree();
        BinarySearchTree bst = new BinarySearchTree();
        
        // input distributions
        int[] random = Generator.generateArray(100000);
        int[] nearlySorted1 = Generator.generateArray(100000, 5), 
        nearlySorted5 = Generator.generateArray(100000, 5),
        nearlySorted10 = Generator.generateArray(100000, 5);

        // benchmarking variables
        long start, end;

        // insert elements into both trees
        for (int i = 0; i < 100000; i++) {
            start = System.currentTimeMillis();
            rbtree.insert(random[i]);
            end = System.currentTimeMillis();

        }
    }
}
