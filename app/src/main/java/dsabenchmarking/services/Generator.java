package dsabenchmarking.services;

import java.util.Random;

public class Generator {

    public static int[] generateArray(int n) {
        int[] result = new int[n];
        Random rand = new Random(490271490);

        for (int i = 0; i < n; i++) {
            result[i] = rand.nextInt((10 * n) + 1);
        }
        
        return result;
    }

    public static int[] generateArray(int n, int x) {
        int[] result = new int[n];
        Random rand = new Random(78932789);

        for (int i = 0; i < n; i++) {
            result[i] = i;
        }

        for (int i = 0; i < (n * x) / 100; i++) {
            int ix = rand.nextInt(n), iy = rand.nextInt(n);

            int tmp = result[ix];
            result[ix] = result[iy];
            result[iy] = tmp;
        }

        return result;
    }

}
