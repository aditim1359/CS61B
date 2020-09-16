package gitlet;

/** Find Command.
 * @author Aditi Mahajan
 */
public class Find implements Command {

    /** Executes  Find command.
     *
     * @param repo repository that command is run in
     * @param args Array of String args by user
     */
    @Override
    public void execute(Repository repo, String[] args) {
        boolean found = false;

        for (Commit commit : repo.getCommits().values()) {
            if (commit.getLogMessage().equals(args[0])) {
                found = true;
                System.out.println(commit.getCommitSHA1());
            }
        }

        if (!found) {
            throw new GitletException("Found no commit with that message.");
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
