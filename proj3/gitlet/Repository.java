package gitlet;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/** Repository for gitlet.
 * @author Aditi Mahajan
 */
public class Repository implements Serializable {

    /** Path to working directory. */
    private Path _workDirec;

    /** Path to gitlet directory. */
    private Path _gitletDirec;

    /** String of working directory. */
    private String _cwdString;
    /** Repo's Index. */
    private Index _index;
    /** Branch on. */
    private String _curBranchName;
    /** Current commit Store. */
    private CommitStore _commitStore;
    /** Current branch Store. */
    private BranchStore _branches;
    /** Current blob store. */
    private BlobStore _blobs;
    /** String of head commit hash. */
    private String _HEAD;

    /** Constructor for init command.
     *
     */
    public Repository() {
        init();
    }

    /** Creates Repository object once gitlet run in CWD and loaded from disk.
     *
     * @param cwdString _cwdString
     * @param gitletDir gitlet directory
     * @param commitStore saved commit store
     * @param blobs saved blobs
     * @param branches saved branches
     * @param head head commit Hash
     * @param curBranchName name of current Branch on
     * @param index index
     */
    public Repository(String cwdString, Path gitletDir,
                      CommitStore commitStore, BlobStore blobs,
                      BranchStore branches, Index index,
                      String head, String curBranchName) {
        _workDirec = Paths.get(cwdString);
        _gitletDirec = gitletDir;
        _cwdString = cwdString;
        _curBranchName = curBranchName;
        _commitStore = commitStore;
        _branches = branches;
        _blobs = blobs;
        _HEAD = head;
        _index = index;
    }

    /** Initializes a repository in response to command init.
     *
     */
    public void init() {
        _cwdString = System.getProperty("user.dir");
        _workDirec = Paths.get(_cwdString);

        String gitlet = ".gitlet";
        Path gitletPath = Paths.get(_cwdString, gitlet);

        if (Files.exists(gitletPath)) {
            throw new GitletException("A Gitlet version-control system "
                    + "already exists in the current directory.");
        }

        _gitletDirec = gitletPath;

        _index = new Index();
        Commit firstCommit = new Commit("initial commit", "",
                _index.getCommitMap());

        _HEAD = firstCommit.getCommitSHA1();

        _commitStore = new CommitStore();
        _commitStore.add(firstCommit.getCommitSHA1(), firstCommit);

        Branch curBranch = new Branch("master", firstCommit.getCommitSHA1());

        _branches = new BranchStore();
        _branches.addBranch(curBranch);
        _curBranchName = "master";

        _blobs = new BlobStore();
    }

    /** Gets repo's index.
     *
     * @return repository index
     */
    public Index getIndex() {
        return _index;
    }

    /** Sets repository's index to the given Index obj.
     *
     * @param index Index object to set to
     */
    public void setIndex(Index index) {
        _index = index;
    }

    /** Gets repo's working directory path.
     *
     * @return Path of the CWD
     */
    public Path getWorkDirec() {
        return _workDirec;
    }

    /** Gets repo's cwd name.
     *
     * @return String of the CWD
     */
    public String getCwdString() {
        return _cwdString;
    }

    /** Gets repo's gitlet directory path.
     *
     * @return Path of gitlet directory
     */
    public Path getGitletDirec() {
        return _gitletDirec;
    }

    /** Gets map of Blob SHA-1 Hash to the Blob Obj in repo.
     *
     * @return Map of Blob Hash to the Blob Object
     */
    public BlobStore getBlobs() {
        return _blobs;
    }

    /** Gets map of Commit SHA-1 Hash to the commit Obj in
     *  repo from commit store.
     *
     * @return Map of Commit Hash to the Commit Object
     */
    public Map<String, Commit> getCommits() {
        return _commitStore.getCommits();
    }

    /** Gets commit store object of repo.
     *
     * @return commit Store
     */
    public CommitStore getCommitStore() {
        return _commitStore;
    }

    /** Gets map of Branch SHA-1 Hash to the Branch Obj in repo.
     *
     * @return Map of Branch Hash to the Branch Object
     */
    public BranchStore getBranches() {
        return _branches;
    }

    /** Gets commit Hash of current Head.
     *
     * @return Hash of current commit
     */
    public String getHEAD() {
        return _HEAD;
    }

    /** Sets repo's HEAD to a new commit.
     *
     * @param commitHASH commit's SHA-1 hash
     */
    public void setHEAD(String commitHASH) {
        _HEAD = commitHASH;
    }
    /** Get the name of the current branch.
     *
     * @return String name of cur branch
     */
    public String getCurBranchName() {
        return _curBranchName;
    }

    /** Update where current Head is to given commit Hash.
     *
     * @param newCommit new head's commit Hash
     */
    public void updateCurBranchHead(String newCommit) {
        _branches.getBranches().get(_curBranchName).setHead(newCommit);
    }

    /** Update the current branch on to the given name.
     *
     * @param branchName new branch on name
     */
    public void setCurBranchName(String branchName) {
        _curBranchName = branchName;
    }
}
