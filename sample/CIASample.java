/**
 * CIASample.java
 *
 * Sample class used to evaluate the CIA tool in Project Part 3.
 * The method compute() is intentionally non-trivial: it mixes
 * sequential assignments, a conditional branch, a while loop,
 * and multiple uses of the same variable so that the PDG model
 * produces a rich (and imperfect) result.
 *
 * Node-counter labels assigned by StatementNodeBuilder will follow
 * a 0-based monotone counter.  The exact counter values for each
 * statement are determined by running the tool on this file and
 * recording the printed PDG labels; they are then used in the
 * ground-truth file.
 */
public class CIASample {

    /**
     * Computes a weighted sum with an early-exit condition.
     *
     * Statements (expected node counters shown as comments after the semi):
     *
     *   0: int sum = 0
     *   1: int i   = 1
     *   2: int n   = 10
     *   3: int threshold = 50
     *   4: while (i <= n)
     *   5:   sum = sum + i
     *   6:   i++
     *   7: int result = sum
     *   8: if (result > threshold)
     *   9:   result = threshold
     *  10: return result
     */
    public int compute() {
        int sum = 0;
        int i = 1;
        int n = 10;
        int threshold = 50;
        while (i <= n) {
            sum = sum + i;
            i++;
        }
        int result = sum;
        if (result > threshold) {
            result = threshold;
        }
        return result;
    }

    /**
     * A second, independent method so we can show the tool handles
     * multi-method files (only the method containing the change point
     * is analysed; the other is unaffected and produces no output).
     *
     *   11: int a = x
     *   12: int b = y
     *   13: if (a > b)
     *   14:   int temp = a
     *   15:   a = b
     *   16:   b = temp
     *   17: return a
     */
    public int min(int x, int y) {
        int a = x;
        int b = y;
        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }
        return a;
    }
}