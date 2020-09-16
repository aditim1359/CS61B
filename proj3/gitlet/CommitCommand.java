package gitlet;

/** Commit Command.
 * @author Aditi Mahajan
 */
public class CommitCommand implements Command {

    /** Executes commit command.
     *
     * @param repo repository that command is run in
     * @param args Array of String args by user
     */
    @Override
    public void execute(Repository repo, String[] args) {

        Commit commit = new Commit(args[0], repo.getHEAD(),
                repo.getIndex().newCommit());
        repo.getCommits().put(commit.getCommitSHA1(), commit);
        repo.setHEAD(commit.getCommitSHA1());

        repo.updateCurBranchHead(commit.getCommitSHA1());
    }

    /** Special commit for merges.
     * Includes merge message.
     *
     * @param repo Current repo
     * @param args current and given branches.
     */
    public void mergeCommit(Repository repo, String[] args) {
        Commit commit = new Commit(args[0],  repo.getHEAD(), args[1],
                repo.getIndex().newCommit());
        repo.getCommits().put(commit.getCommitSHA1(), commit);
        repo.setHEAD(commit.getCommitSHA1());

        repo.updateCurBranchHead(commit.getCommitSHA1());
    }

    @Override
    public boolean needsRepo() {
        return true;
    }

    @Override
    public boolean checkArgs(String[] args) {
        if (args.length == 1 && args[0].isEmpty()) {
            throw new GitletException("Please enter a commit message.");
        }
        return args.length == 1;
    }
}
