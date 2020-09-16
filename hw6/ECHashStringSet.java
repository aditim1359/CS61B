import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author Aditi Mahajan
 */
class ECHashStringSet implements StringSet {

    private int _numBucket;
    private int _numElem;
    private LinkedList<String>[] _buckets;

    ECHashStringSet() {
        _numBucket = 5;
        _numElem = 0;
        _buckets = new LinkedList[5];

    }

    @Override
    public void put(String s) {

        if (_numElem/_numBucket > 5){
            LinkedList<String>[] oldTemp = _buckets;
            _buckets = new LinkedList[oldTemp.length * 2];

            for (LinkedList<String> l : oldTemp) {
                if (l != null) {
                    for (String str : l) {
                        put(str);
                    }
                }
            }
        }

        _numElem += 1;
        int pos = hashHelper(s);

        if (_buckets[pos] == null) {
            _buckets[pos] = new LinkedList<String>();
        }
        _buckets[pos].add(s);
    }

    /** Takes a string S and returns the hash position in the _buckets.
     *
     * @param s input string
     * @return the hash index in the _buckets
     */
    public int hashHelper(String s) {
        int hash = s.hashCode();
        int unsign = (hash & 0x7fffffff) % _numBucket;
        return unsign;
    }

    @Override
    public boolean contains(String s) {
        int pos = hashHelper(s);
        if (_buckets[pos] == null) {
            return false;
        } else if (_buckets[pos].contains(s)) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> asList() {
        List<String> result = new ArrayList<String>();
        for (LinkedList<String> list : _buckets) {
            if (list != null) {
                for (String str : list) {
                    result.add(str);
                }
            }
        }
        return result;
    }

    public int findHash(String s) {
        return s.hashCode();
    }
}
