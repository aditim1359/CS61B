package gitlet;

/** RM Branch Command.
 * @author Aditi Mahajan
 */
public class RMBranch implements Command {

    /** Executes RM Branch.
     *
     * @param repo repository that command is run in
     * @param args Array of String args by user
     */
    @Override
    public void execute(Repository repo, String[] args) {
        if (!repo.getBranches().getBranches().containsKey(args[0])) {
            throw new GitletException("A branch with that name "
                    + "does not exist.");
        } else if (repo.getCurBranchName().equals(args[0])) {
            throw new GitletException("Cannot remove the current branch.");
        } else {
            repo.getBranches().removeBranch(args[0]);
        }
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
