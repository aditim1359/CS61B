package gitlet;

/** GLobal Log Command.
 * @author Aditi Mahajan
 */
public class GlobalLog implements Command {
    /** Executes Global Log command.
     *
     * @param repo repository that command is run in
     * @param args Array of String args by user
     */
    @Override
    public void execute(Repository repo, String[] args) {
        for (Commit commit : repo.getCommits().values()) {
            System.out.println("===");
            System.out.println("commit " + commit.getCommitSHA1());
            if (!commit.getParent2().isEmpty()) {
                System.out.println("Merge: "
                        + commit.getParent1().substring(0, 7)
                        + " " + commit.getParent2().substring(0, 7));
            }
            System.out.println("Date: " + commit.getTimestamp());
            System.out.println(commit.getLogMessage());
            System.out.println();
        }
    }

    @Override
    public boolean needsRepo() {
        return true;
    }

    @Override
    public boolean checkArgs(String[] args) {
        return args.length == 0;
    }
}
