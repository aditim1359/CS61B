package gitlet;

import java.io.Serializable;

/** Branch object.
 * @author Aditi Mahajan
 */
public class Branch implements Serializable {
    /** String name of branch. */
    private String _name;
    /** String Head commit hash. */
    private String _head;

    /** Constructor for a Branch.
     *
     * @param name name of branch
     * @param commitHash Head hash of the branch
     */
    public Branch(String name, String commitHash) {
        _name = name;
        _head = commitHash;
    }

    /** Allows for updating the branch head to a new commit.
     *
     * @param commitHash SHA-1 hash of new branch head commit
     */
    public void setHead(String commitHash) {
        _head = commitHash;
    }

    /** Returns name of the Branch.
     *
     * @return String name of branch
     */
    public String getName() {
        return _name;
    }

    /** Returns commit hash of Branch cur head.
     *
     * @return String commit hash
     */
    public String getHead() {
        return _head;
    }

}
