package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** Store of Branches in repo.
 * @author Aditi Mahajan
 */
public class BranchStore implements Serializable {
    /** Map of branch names to Obj. */
    private Map<String, Branch> _branches;

    /** General init constructor.
     *
     */
    public BranchStore() {
        _branches = new HashMap<String, Branch>();
    }

    /** Constructor for store given already have past branches.
     *
     * @param prevBranches map of previous branch names to objects
     */
    public BranchStore(Map<String, Branch> prevBranches) {
        _branches = prevBranches;
    }

    /** Adds a branch to the store of branches.
     *
     * @param branch Branch obj to add
     */
    public void addBranch(Branch branch) {
        _branches.put(branch.getName(), branch);
    }

    /** Removes a branch from the store of branches.
     * Doesn't remove the commits associated to branch.
     *
     * @param branchName Name of the branch to remove
     */
    public void removeBranch(String branchName) {
        _branches.remove(branchName);
    }

    /** Gets _branches attribute.
     * @return Map oof branches name to obj.
     */
    public Map<String, Branch> getBranches() {
        return _branches;
    }

}
