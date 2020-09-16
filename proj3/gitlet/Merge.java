package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/** Merge Command.
 * @author Aditi Mahajan
 */
public class Merge implements Command {
    /** Executes merge command.
     *
     * @param repo repository that command is run in
     * @param args Array of String args by user
     * @throws IOException exception
     */
    @Override
    public void execute(Repository repo, String[] args) throws IOException {
        if (!repo.getIndex().getRemoved().isEmpty()
                || !repo.getIndex().getAdded().isEmpty()) {
            throw new GitletException("You have uncommitted changes.");
        }
        List<String> result = Utils.plainFilenamesIn(repo.getCwdString());
        for (String fileName : result) {
            if (!repo.getIndex().getCommitMap().containsKey(fileName)) {
                throw new GitletException("There is an untracked file in "
                        + "the way; delete it, or add and commit it first.");
            }
        }
        String givenBranch = args[0];
        if (!repo.getBranches().getBranches().containsKey(givenBranch)) {
            throw new GitletException("A branch with that name "
                    + "does not exist.");
        }
        if (givenBranch.equals(repo.getCurBranchName())) {
            throw new GitletException("Cannot merge a branch with itself.");
        }
        Commit currentCom = repo.getCommits().get(repo.getHEAD());
        Branch branchObjGiven = repo.getBranches().getBranches()
                .get(givenBranch);
        String commitHashGivenBran = branchObjGiven.getHead();
        Commit branchCom = repo.getCommits().get(commitHashGivenBran);
        Commit splitCom = splitCommit(repo, branchCom, currentCom);
        if (splitCom == null) {
            throw new GitletException("No Split Commit found.");
        }
        if (splitCom.getCommitSHA1().equals(branchCom.getCommitSHA1())) {
            throw new GitletException("Given branch is an ancestor"
                    + " of the current branch.");
        } else if (splitCom.getCommitSHA1()
                .equals(currentCom.getCommitSHA1())) {
            Checkout checkout = new Checkout();
            checkout.execute(repo, args);
            throw new GitletException("Current branch fast-forwarded.");
        }
        Set<String> allFiles = allFiles(splitCom, branchCom, currentCom);
        boolean conflict = false;
        for (String fileName : allFiles) {
            int caseNum = caseNumber(fileName, repo, splitCom,
                    branchCom, currentCom);
            conflict = procedure(caseNum, repo, fileName, splitCom,
                    branchCom, currentCom);
        }
        CommitCommand commitCom = new CommitCommand();
        String[] commitArgs = {"Merged " + givenBranch + " into "
                + repo.getCurBranchName() +  ".", commitHashGivenBran};
        commitCom.mergeCommit(repo, commitArgs);
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /** Finds the split commit for two commits.
     *
     * @param repo current repo
     * @param givenCommit commit 1
     * @param curCommit commit 2
     * @return split of commit 1 and 2
     */
    private static Commit splitCommit(Repository repo,
                                      Commit givenCommit, Commit curCommit) {
        int depth = 1;

        LinkedHashMap<String, Integer> curHistory
                = new LinkedHashMap<String, Integer>();
        ancestors(repo, curCommit.getCommitSHA1(), curHistory, depth);

        LinkedHashMap<String, Integer> givenHistory
                = new LinkedHashMap<String, Integer>();
        ancestors(repo, givenCommit.getCommitSHA1(), givenHistory, depth);

        LinkedHashMap<String, Integer> common
                = new LinkedHashMap<String, Integer>();

        for (Map.Entry<String, Integer> pos : curHistory.entrySet()) {
            if (givenHistory.containsKey(pos.getKey())) {
                common.put(pos.getKey(), pos.getValue());
            }
        }

        if (!common.isEmpty()) {
            Integer smallestDepth = Integer.MAX_VALUE;
            String smallPos = "";
            for (Map.Entry<String, Integer> pos : common.entrySet()) {
                if (pos.getValue() <= smallestDepth) {
                    smallPos = pos.getKey();
                    smallestDepth = pos.getValue();
                }
            }
            return repo.getCommits().get(smallPos);
        } else {
            return null;
        }

    }

    /** Returns all the ancestors (self included) of the given commit.
     *
     * @param repo current repo
     * @param commit given commit
     * @param history set of history currently known
     * @param depth Hwo far from main commit
     * @return set of complete history
     */
    private static LinkedHashMap<String, Integer> ancestors(
            Repository repo, String commit, LinkedHashMap<String,
            Integer> history, int depth) {
        String pos = commit;
        String pos2 = null;
        while (!pos.isEmpty()) {
            if (history.containsKey(pos)) {
                if (depth <= history.get(pos)) {
                    history.put(pos, depth);
                }
            } else {
                history.put(pos, depth);
            }
            if (!repo.getCommits().get(pos).getParent2().isEmpty()) {
                pos2 = repo.getCommits().get(pos).getParent2();
                int depth2 = depth + 1;
                ancestors(repo, pos2, history, depth2);
            }
            pos = repo.getCommits().get(pos).getParent1();
            depth++;
        }
        return history;
    }

    /** Returns all files that show up in commit 1, 2, and 3.
     * No repeats.
     *
     * @param split commit 1
     * @param given commit 2
     * @param current commit 3
     * @return all fileNames in the 3 commits
     */
    private static Set<String> allFiles(Commit split,
                                        Commit given, Commit current) {
        Set<String> result = new HashSet<String>();
        result.addAll(split.getMap().keySet());
        result.addAll(given.getMap().keySet());
        result.addAll(current.getMap().keySet());
        return result;
    }

    /** With the given commits, makes a number for
     * the file for which scenario falls under.
     *
     * @param fileName Name of file
     * @param repo current repo
     * @param split split commit
     * @param given given commit
     * @param current current commit
     * @return case Number made
     *
     *
     */
    private static int caseNumber(String fileName, Repository repo,
                                  Commit split, Commit given, Commit current) {
        int caseNum = 0b0000_0000_0000;

        if (!split.getMap().containsKey(fileName)) {
            caseNum = 0b0000_0000_0001;
            if (!given.getMap().containsKey(fileName)) {
                caseNum += 0b0001_0000;
            } else {
                caseNum += 0b0100_0000;
                if (!current.getMap().containsKey(fileName)) {
                    caseNum += 0b0001_0000_0000;
                } else {
                    if (current.getMap().get(fileName)
                            .equals(given.getMap().get(fileName))) {
                        caseNum += 0b0100_0000_0000;
                    } else {
                        caseNum += 0b1000_0000_0000;
                    }
                }
            }
        } else {
            caseNum = 0b0000_0000_0010;
            if (!given.getMap().containsKey(fileName)) {
                caseNum += 0b0001_0000;
                if (!current.getMap().containsKey(fileName)) {
                    caseNum += 0b0001_0000_0000;
                } else {
                    if (current.getMap().get(fileName)
                            .equals(split.getMap().get(fileName))) {
                        caseNum += 0b0010_0000_0000;
                    } else {
                        caseNum += 0b1000_0000_0000;
                    }
                }
            } else {
                if (split.getMap().get(fileName).equals(given.getMap()
                        .get(fileName))) {
                    caseNum = H411;
                } else {
                    caseNum += 0b0100_0000;
                    if (!current.getMap().containsKey(fileName)) {
                        caseNum += 0b0001_0000_0000;
                    } else {
                        if (current.getMap().get(fileName).equals(given.getMap()
                                .get(fileName))) {
                            caseNum += 0b0100_0000_0000;
                        } else if (current.getMap().get(fileName)
                                .equals(split.getMap()
                                .get(fileName))) {
                            caseNum += 0b0010_0000_0000;
                        } else {
                            caseNum += 0b1000_0000_0000;
                        }
                    }
                }
            }
        }
        return caseNum;
    }

    /** In branch,  split, not current. */
    static final int H011 = 0b0000_0001_0001;

    /** not in split, diff in given, not in current. */
    static final int H020 = 0b0001_0100_0001;

    /** not split, diff given, same current.*/
    static final int H220 = 0b0100_0100_0001;
    /** not split, diff given, same current.*/
    static final int H420 = 0b1000_0100_0001;
    /** not split, diff given, same current.*/
    static final int H101 = 0b0010_0001_0010;
    /** not split, diff given, same current.*/
    static final int H401 = 0b1000_0001_0010;
    /** not split, diff given, same current.*/
    static final int H001 = 0b0001_0001_0010;
    /** not split, diff given, same current.*/
    static final int H411 = 0b1000_0010_0010;
    /** not split, diff given, same current.*/
    static final int H121 = 0b0010_0100_0010;
    /** not split, diff given, same current.*/
    static final int H221 = 0b0100_0100_0010;
    /** not split, diff given, same current.*/
    static final int H421 = 0b1000_0100_0010;
    /** not split, diff given, same current.*/
    static final int H021 = 0b0001_0100_0010;

    /** Based on the case number, executes the merge.
     *
     * @param caseNumber case Number
     * @param repo current repo
     * @param fileName Name of the file to perform merge on
     * @param split split commit
     * @param given given commit
     * @param current current commit
     * @return True if a merge conflict occured
     * @throws IOException exception
     */
    private static boolean procedure(int caseNumber, Repository repo,
                                     String fileName, Commit split,
                                     Commit given, Commit current)
            throws IOException {
        Checkout checkout = new Checkout();
        Add add = new Add();
        RM rm = new RM();
        boolean mergeConflict = false;

        if (caseNumber == H011) {
            boolean nothing = true;
        } else if (caseNumber == H020) {
            String[] checkArgs = {given.getCommitSHA1(), "--", fileName};
            checkout.execute(repo, checkArgs);
            String[] addArgs = {fileName};
            add.execute(repo, addArgs);
        } else if (caseNumber == H220) {
            boolean nothing = true;
        } else if (caseNumber == H420) {
            conflictProcedure(current, given, repo, fileName);
            mergeConflict = true;
        } else if (caseNumber == H101) {
            String[] rmArgs = {fileName};
            rm.execute(repo, rmArgs);
        } else if (caseNumber == H401) {
            conflictProcedure(current, given, repo, fileName);
            mergeConflict = true;
        } else if (caseNumber == H001) {
            boolean nothing = true;
        } else if (caseNumber == H411) {
            boolean nothing = true;
        } else if (caseNumber == H121) {
            String[] checkArgs = {given.getCommitSHA1(), "--", fileName};
            checkout.execute(repo, checkArgs);
            String[] addArgs = {fileName};
            add.execute(repo, addArgs);
        } else if (caseNumber == H221) {
            boolean nothing = true;
        } else if (caseNumber == H421) {
            conflictProcedure(current, given, repo, fileName);
            mergeConflict = true;
        } else if (caseNumber == H021) {
            conflictProcedure(current, given, repo, fileName);
            mergeConflict = true;
        } else {
            throw new GitletException("Aditi messed up somewhere2");
        }

        return mergeConflict;
    }

    /** Merge conflict procedure.
     * Concatenates the two versions of the file into one conflict file.
     * Writes it to CWD
     *
     * @param current current commit
     * @param given givne commit
     * @param repo current repo
     * @param fileName name of file
     */
    public static void conflictProcedure(Commit current, Commit given,
                                         Repository repo, String fileName) {

        String currentContents = null;
        if (!current.getMap().containsKey(fileName)) {
            currentContents = "";
        } else {
            String currentFileHash = current.getMap().get(fileName);
            currentContents = new String(repo.getBlobs().getBlobs()
                    .get(currentFileHash).getBlobContents());
        }

        String givenContents = null;
        if (!given.getMap().containsKey(fileName)) {
            givenContents = "";
        } else {
            String givenFileHash = given.getMap().get(fileName);
            givenContents = new String(repo.getBlobs().getBlobs()
                    .get(givenFileHash).getBlobContents());
        }

        String newContents = "<<<<<<< HEAD\n" + currentContents
                + "=======\n" + givenContents + ">>>>>>>\n";

        Blob newFileBlob = new Blob(fileName, newContents.getBytes());
        repo.getBlobs().addBlob(newFileBlob);

        Index repoIndex = repo.getIndex();

        if (repoIndex.getAdded().containsKey(fileName)) {
            repoIndex.getAdded().replace(fileName, newFileBlob.getBlobSHA1());
        } else {
            repoIndex.getAdded().put(fileName, newFileBlob.getBlobSHA1());
        }

        String cwd = repo.getCwdString();
        File file = Utils.join(cwd, fileName);
        Utils.writeContents(file, newContents.getBytes());
    }

    @Override
    public boolean needsRepo() {
        return true;
    }

    @Override
    public boolean checkArgs(String[] args) {
        return args.length == 1;
    }
}
