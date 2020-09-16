
/** Disjoint sets of contiguous integers that allows (a) finding whether
 *  two integers are in the same set and (b) unioning two sets together.  
 *  At any given time, for a structure partitioning the integers 1 to N, 
 *  into sets, each set is represented by a unique member of that
 *  set, called its representative.
 *  @author Aditi Mahajan
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        ids = new int[N + 1];
        for (int i = 1; i <= N; i++){
            ids[i] = i;
        }

        counter = N;
    }

    /** Return the representative of the set currently containing V.
     *  Assumes V is contained in one of the sets.  */
    public int find(int v) {
        return ids[v];
    }

    /** Return true iff U and V are in the same set. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single set, returning its representative. */
    public int union(int u, int v) {
        int a = find(u);
        int b = find(v);

        //in same set already
        if (a == b) {
            return a;
        }

        for (int i = 1 ; i < ids.length; i++) {
            if (ids[i] == b) {
                ids[i] = a;
            }
        }
        counter--;
        return a;
    }

    private int[] ids;
    private int counter;
}
