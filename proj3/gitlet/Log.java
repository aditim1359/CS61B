package gitlet;

/** Log Command.
 * @author Aditi Mahajan
 */
public class Log implements Command {
    /** Executes Log command.
     *
     * @param repo repository that command is run in
     * @param args Array of String args by user
     */
    @Override
    public void execute(Repository repo, String[] args) {
        String head = repo.getHEAD();
        Commit commit = repo.getCommits().get(head);

        while (!commit.getParent1().isEmpty()) {
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
            commit = repo.getCommits().get(commit.getParent1());
        }

        System.out.println("===");
        System.out.println("commit " + commit.getCommitSHA1());
        if (!commit.getParent2().isEmpty()) {
            System.out.println("Merge: " + commit.getParent1().substring(0, 7)
                    + " " + commit.getParent2().substring(0, 7));
        }
        System.out.println("Date: " + commit.getTimestamp());
        System.out.println(commit.getLogMessage());
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
