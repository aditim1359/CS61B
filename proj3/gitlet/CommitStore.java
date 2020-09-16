package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** Storage of all commits in repo.
 * @author Aditi Mahajan
 */
public class CommitStore implements Serializable {

    /** Map of commit hashes to commit obj.
     *
     */
    private Map<String, Commit> _commits;

    /** General init constructor.
     *
     */
    public CommitStore() {
        _commits = new HashMap<String, Commit>();
    }

    /** Constructor for store given already have past commits.
     *
     * @param prevCommits map of previous commit hashes to objects
     */
    public CommitStore(Map<String, Commit> prevCommits) {
        _commits = prevCommits;
    }

    /** Given the first x characters of a commit hash, returns
     * the entire commit hash.
     *
     * @param commitID shortened version of the commit's hash
     * @return complete SHA-1 hash of commit
     */
    public String lookUpCommitHash(String commitID) {
        for (String hash : _commits.keySet()) {
            if (hash.substring(0, commitID.length()).equals(commitID)) {
                return hash;
            }
        }
        return null;
    }

    /** Adds a commit to the store.
     *
     * @param commitHash SHA-1 hash of commit
     * @param commit commit obj corr. to hash
     */
    public void add(String commitHash, Commit commit) {
        _commits.put(commitHash, commit);
    }

    /** Gets _commit instance attribute.
     * @return _commit
     */
    public Map<String, Commit> getCommits() {
        return _commits;
    }
}
