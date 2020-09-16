package gitlet;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

/** Checkout command.
 * @author Aditi Mahajan
 */
public class Checkout implements Command {

    /** Executes checkout command.
     *
     * @param repo repository that command is run in
     * @param args Array of String args by user
     * @throws IOException exception
     */
    @Override
    public void execute(Repository repo, String[] args) throws IOException {
        if (args.length == 1) {
            checkoutBranch(repo, args[0]);
        } else if (args.length == 2) {
            checkoutFile(repo, args[1]);
        } else if (args.length == 3) {
            checkoutFileWithCommit(repo, args[2], args[0]);
        }
    }

    /** Checkout procedure given a file name.
     *
     * @param repo current repo
     * @param fileName Name of file to checkout
     */
    private void checkoutFile(Repository repo, String fileName) {
        Commit curCommit = repo.getCommits().get(repo.getHEAD());
        if (curCommit.getMap().keySet().contains(fileName)) {
            String changeTOblobHash = curCommit.getMap().get(fileName);

            String cwd = repo.getCwdString();
            Path filePath = Paths.get(cwd, fileName);
            File file = Utils.join(cwd, fileName);
            Utils.writeContents(file, repo.getBlobs().getBlobs()
                    .get(changeTOblobHash).getBlobContents());
        } else {
            throw new GitletException("File does not exist in that commit.");
        }
    }

    /** Checkout procedure for checking out file at a commit.
     *
     * @param repo current repo
     * @param fileName file to checkout
     * @param commitID commit ID (not nec. complete hash)
     * @throws IOException exception
     */
    private void checkoutFileWithCommit(Repository repo, String fileName,
                                        String commitID) throws IOException {
        String fullCommitHash = repo.getCommitStore()
                .lookUpCommitHash(commitID);
        if (fullCommitHash == null) {
            throw new GitletException("No commit with that id exists.");
        }

        Commit commit = repo.getCommits().get(fullCommitHash);

        if (commit.getMap().keySet().contains(fileName)) {
            String changeTOblobHash = commit.getMap().get(fileName);

            String cwd = repo.getCwdString();

            Path filePath = Paths.get(cwd, fileName);
            File file = Utils.join(cwd, fileName);
            if (!Files.exists(filePath)) {
                file.createNewFile();
            }
            Utils.writeContents(file, repo.getBlobs().getBlobs()
                    .get(changeTOblobHash).getBlobContents());
        } else {
            throw new GitletException("File does not exist in that commit.");
        }
    }

    /** Checkout procedure for a branch.
     *
     * @param repo current repo
     * @param branchName name of branch to checkout
     * @throws IOException exception
     */
    private void checkoutBranch(Repository repo, String branchName)
            throws IOException {
        if (repo.getCurBranchName().equals(branchName)) {
            throw new GitletException("No need to checkout the "
                    + "current branch.");
        }
        if (!repo.getBranches().getBranches().containsKey(branchName)) {
            throw new GitletException("No such branch exists.");
        }

        String brCommitHash = repo.getBranches().getBranches()
                .get(branchName).getHead();
        Commit branchCommit = repo.getCommits().get(brCommitHash);

        List<String> result = Utils.plainFilenamesIn(repo.getCwdString());

        for (String fileName : result) {
            if (branchCommit.getMap().containsKey(fileName)
                    && !repo.getIndex().getCommitMap()
                    .containsKey(fileName)) {
                throw new GitletException("There is an untracked file in "
                        + "the way; delete it, or add and commit it first.");
            }
        }

        for (String fileName : result) {
            if (!branchCommit.getMap().containsKey(fileName)) {
                File file = Utils.join(repo.getCwdString(), fileName);
                Utils.restrictedDelete(file);
            }
        }

        for (String fileName : branchCommit.getMap().keySet()) {
            checkoutFileWithCommit(repo, fileName, brCommitHash);
        }

        repo.setHEAD(brCommitHash);

        repo.setCurBranchName(branchName);

        Index newIndex = new Index(branchCommit.getMap());
        repo.setIndex(newIndex);
    }

    /** Procedure to find files that exist in one not other.
     *
     * @param filesInCurrent set1
     * @param filesInChecked set2
     * @return exclusive or difference
     */
    private static Set<String> difference(Set<String> filesInCurrent,
                                          Set<String> filesInChecked) {
        for (String fileName : filesInCurrent) {
            String destFile = (filesInChecked.contains(fileName))
                    ? fileName : null;
            if (destFile == null) {
                filesInChecked.add(fileName);
            }
        }
        return filesInChecked;
    }

    @Override
    public boolean needsRepo() {
        return true;
    }

    @Override
    public boolean checkArgs(String[] args) {
        boolean argsGood = false;
        if (args.length > 0 && args.length <= 3) {
            argsGood = args.length != 3 || args[1].equals("--");
        }
        return argsGood;
    }
}
