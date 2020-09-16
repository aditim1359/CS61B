package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** Index Object.
 * @author Aditi Mahajan
 */
public class Index implements Serializable {
    /**
     * Maps file names to blob hashes for latest commit.
     */
    private Map<String, String> _commitMap;

    /**
     * Maps file name to blob hashes of files added to stage.
     */
    private Map<String, String> _added;

    /**
     * List of file names of removed files.
     */
    private ArrayList<String> _removed;

    /** Constructor of Index for init.
     *
     */
    public Index() {
        _commitMap = new HashMap<String, String>();
        _added = new HashMap<String, String>();
        _removed = new ArrayList<String>();
    }

    /** Constructor of index given a previous map.
     *
     * @param savedMap previous map
     */
    public Index(Map<String, String> savedMap) {
        _commitMap = savedMap;
        _added = new HashMap<String, String>();
        _removed = new ArrayList<String>();
    }

    /** Add a file to be removed and staged.
     *
     * @param fileName file to be removed
     */
    public void addToRemoved(String fileName) {
        _removed.add(fileName);
    }

    /** Returns Index's map.
     *
     * @return commit Map
     */
    public Map<String, String> getCommitMap() {
        return _commitMap;
    }

    /** Returns index's added stage.
     *
     * @return _added
     */
    public Map<String, String> getAdded() {
        return _added;
    }

    /** Returns index's removed stage.
     *
     * @return _removed
     */
    public ArrayList<String> getRemoved() {
        return _removed;
    }

    /** Moves files from stage to commit map, updating as needed.
     * Executed before a commit.
     *
     * @return new index map
     */
    public Map<String, String> newCommit() {
        if (_removed.isEmpty() && _added.isEmpty()) {
            throw new GitletException("No changes added to the commit.");
        }

        for (String fileRemove : _removed) {
            _commitMap.remove(fileRemove);
        }
        for (String fileUpdate : _added.keySet()) {
            _commitMap.remove(fileUpdate);
            _commitMap.put(fileUpdate, _added.get(fileUpdate));
        }

        _added.clear();
        _removed.clear();

        return _commitMap;
    }
}
