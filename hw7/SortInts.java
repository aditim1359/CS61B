/** HW #7, Distribution counting for large numbers.
 *  @author Aditi Mahajan
 */
public class SortInts {

    /** Sort A into ascending order.  Assumes that 0 <= A[i] < n*n for all
     *  i, and that the A[i] are distinct. */
    static void sort(long[] A) {
        long[] temp = new long[10];
        int s = ("" + (A.length * A.length)).length();
        double place = Math.pow(10, s);
        for (int k = 1; k < place; k = k * 10) {
            for (long number: A) {
                int digit = (int) number / k;
                temp[digit] = number;
            }
        }
    }
}

