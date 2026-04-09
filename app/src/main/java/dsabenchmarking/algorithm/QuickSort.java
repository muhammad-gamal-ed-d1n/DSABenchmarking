package dsabenchmarking.algorithm;


public class QuickSort {

    public void quickSort(int[] arr, int p, int r) {
        if (p < r) {
            int q = partition(arr, p, r);
            quickSort(arr, p, q);
            quickSort(arr, q + 1, r);
        }
    }

    private int partition(int[] arr, int p, int r) {
        int i = p;
        int x = arr[p];

        for (int j = p + 1; j < r; j++) {
            if (arr[j] <= x) {
                i++;
                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
        }

        int tmp = arr[p];
        arr[p] = arr[i];
        arr[i] = tmp;

        return i;
    }
}