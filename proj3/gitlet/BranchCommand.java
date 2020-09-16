package gitlet;

/** Branch Command.
 * @author Aditi Mahajan
 */
public class BranchCommand implements Command {
    /** Executes Branch COmmand.
     *
     * @param repo repository that command is run in
     * @param args Array of String args by user
     */
    @Override
    public void execute(Repository repo, String[] args) {
        if (repo.getBranches().getBranches().containsKey(args[0])) {
            throw new GitletException("A branch with that name "
                    + "already exists.");
        }

        Branch newBranch = new Branch(args[0], repo.getHEAD());

        repo.getBranches().addBranch(newBranch);
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
