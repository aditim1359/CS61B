package gitlet;

/** Init Command.
 * @author Aditi Mahajan
 */
public class Init implements Command {

    /** Executes init command.
     *
     * @param repo repository that command is run in
     * @param args Array of String args by user
     */
    @Override
    public void execute(Repository repo, String[] args) {
        repo.init();
    }

    @Override
    public boolean needsRepo() {
        return false;
    }

    @Override
    public boolean checkArgs(String[] args) {
        return args.length == 0;
    }
}
