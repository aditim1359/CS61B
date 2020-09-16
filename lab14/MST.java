import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/** Minimal spanning tree utility.
 *  @author Aditi Mahajan
 */
public class MST {

    /** Given an undirected, weighted, connected graph whose vertices are
     *  numbered 1 to V, and an array E of edges, returns an array of edges
     *  in E that form a minimal spanning tree of the input graph.
     *  Each edge in E is a three-element int array of the form (u, v, w),
     *  where 0 < u < v <= V are vertex numbers, and 0 <= w is the weight
     *  of the edge. The result is an array containing edges from E.
     *  Neither E nor the arrays in it may be modified.  There may be
     *  multiple edges between vertices.  The objects in the returned array
     *  are a subset of those in E (they do not include copies of the
     *  original edges, just the original edges themselves.) */
    public static int[][] mst(int V, int[][] E) {
        E = Arrays.copyOf(E, E.length);
        int numEdgesInResult = V - 1; // FIXME: how many edges should there be in our MST?
        int[][] result = new int[numEdgesInResult][];

        UnionFind union = new UnionFind(V);

        if (E.length < 1) {
            //result[0] = E[0];
            return result;
        }

        PriorityQueue<int[]> pq = new PriorityQueue<>(E.length, EDGE_WEIGHT_COMPARATOR);

        for (int i = 0; i < E.length; i++) {
            pq.add(E[i]);
        }

        int a = 0;
        while (a < V - 1) {
            int[] edge = pq.remove();
            int x = union.find(edge[0]);
            int y = union.find(edge[1]);

            if (x == y) {
                //make a cycle
            } else {
                result[a] = edge;
                a++;
                union.union(x, y);
            }
        }
        //        Comparator<int[]> comp = EDGE_WEIGHT_COMPARATOR;
//
//        int min = Integer.MAX_VALUE;
//        for (int edge = 1; edge <= V; edge++) {
//            int weight = E[edge][2];
//            for (int compEdge = 1; compEdge <= V; compEdge++) {
//                if ()
//            }
//        }

//
//        int minnCost = 0;
//        int edge_count = 0;
//        while (edge_count < V - 1)
//        {
//            int min = Integer.MAX_VALUE, a = -1, b = -1;
//            for (int i = 0; i < V; i++)
//            {
//                for (int j = 0; j < V; j++)
//                {
//                    if (union.find(i) != union.find(j) && E[i][j] < min)
//                    {
//                        min = E[i][j];
//                        a = i;
//                        b = j;
//                    }
//                }
//            }
//
//            union.union(a, b);
//            System.out.printf("Edge %d:(%d, %d) cost:%d \n",
//                    edge_count++, a, b, min);
//            minnCost += min;
//        }
        // FIXME: what other data structures do I need?
        // FIXME: do Kruskal's Algorithm
        return result;
    }

    /** An ordering of edges by weight. */
    private static final Comparator<int[]> EDGE_WEIGHT_COMPARATOR =
        new Comparator<int[]>() {
            @Override
            public int compare(int[] e0, int[] e1) {
                return e0[2] - e1[2];
            }
        };

}
