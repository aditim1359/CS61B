import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Aditi Mahajan
 */
public class BSTStringSetTest  {
    BSTStringSet set = new BSTStringSet();

    @Test
    public void testPut() {
        set.put("A");
        set.put("1");
        set.put("C");
        set.put("B");

        String[] sol = {"1", "A", "B", "C"};
        List result = set.asList();
        for (int i = 0; i < sol.length; i++) {
            assertEquals("mismatch at " + sol[i], sol[i], result.get(i));
        }
    }

    @Test
    public void testContains() {
        set.put("C");
        set.put("A");
        set.put("B");
        set.put("d");
        set.put("Z");
        set.put("z");

        assertTrue(set.contains("d"));
        assertFalse(set.contains("E"));
    }

    @Test
    public void testSortedIter() {
        set.put("C");
        set.put("A");
        set.put("B");
        set.put("d");
        set.put("Z");
        set.put("z");

        List list = set.rangeList("A", "C");
        String[] sol = {"A", "B", "C"};
        for (int i = 0; i < sol.length; i++) {
            assertEquals("mismatch at " + sol[i], sol[i], list.get(i));
        }
    }
}
