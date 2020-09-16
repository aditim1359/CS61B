import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int end = Math.min(k, array.length);

            for (int i = 0; i < end; i++) {
                for (int j = i; j > 0; j--) {
                    if (array[j] < array[j-1]) {
                        swap(array, j-1, j);
                    }
                    else {
                        break;

                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            /** Selection sort is slower than an insertion sort. */
            int end = Math.min(k, array.length);
            for(int i = 0; i < end; i++){
                int min = i;
                for(int j = i + 1; j < end; j++) {
                    if (array[min] > array[j]) {
                        min = j;
                    }
                }

                swap(array, min, i);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            k = Math.min(k,array.length);
            mergeSort(array, 0, k, new int[k]);
        }

        // may want to add additional methods

        private void mergeSort(int[] array, int left, int right, int[] result) {
            int mid = (right + left) / 2;
            if (right - left > 1) {
                mergeSort(array, left, mid, result);
                mergeSort(array, mid, right, result);
                combine(array, left, mid, right, result);

                System.arraycopy(result, left, array, left, right-left);
            }
        }

        private void combine(int[] array, int left, int mid, int right, int[] result) {
            for (int l = left, r = mid; l < mid || r < right;) {
                if((r == right) || (l < mid && array[l] < array[r])) {
                    result[l+r-mid] = array[l];
                    l++;
                } else if ((l == mid) ||  (r < right && array[l] >= array[r]))  {
                    result[l+r-mid] = array[r];
                    r++;
                }
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override

        /** Used Kevin Wayne and Bob Sedgewick's Algorithms textbook
         * example for inspiration.
         *
         * http://algs4.cs.princeton.edu/51radix/LSD.java.html
         */

        public void sort(int[] arr, int k) {
            int bits = 32;
            int bitPerByte = 8;
            int pos = 1 << bitPerByte;
            int group = bits / bitPerByte;
            int end = Math.min(k, arr.length);
            int mask = pos - 1;

            int[] result = new int[end];

            for (int i = 0; i < group; i++) {
                int[] count = new int[pos + 1];

                for (int j = 0; j < end; j++) {
                    int d = (arr[j] >> bitPerByte * i) * mask;
                    count[d + 1]++;
                }

                for (int r = 0; r < pos; r++) {
                    count[r + 1] += count[r];
                }

                if (i == group - 1) {
                    int shift1 = count[pos] = count[pos / 2];
                    int shift2 = count[pos / 2];

                    for (int q = 0; q < pos / 2; q++) {
                        count[q] += shift1;
                    }
                    for (int q = pos / 2; q < pos; q++) {
                        count[q] -= shift2;
                    }
                }

                for (int p = 0; p < k; p++) {
                    int c = (arr[p] >> bitPerByte * i) & mask;
                    result[count[c]++] = arr[p];
                }
                for (int p = 0; p < k; p++) {
                    arr[p] = result[p];
                }
            }
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
