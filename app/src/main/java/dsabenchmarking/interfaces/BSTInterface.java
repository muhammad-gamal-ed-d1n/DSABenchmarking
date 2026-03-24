package dsabenchmarking.interfaces;

public interface BSTInterface {
    boolean insert(int v);
    boolean delete(int v);
    boolean contains(int v);
    int[] inOrder(); //return elements in sorted order
    int height(); //return height of tree. 0 if empty
    int size(); //return the number of elements in the tree
}
