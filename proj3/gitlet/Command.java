package gitlet;

import java.io.IOException;

/** general command format.
 * @author Aditi Mahajan*/

public interface Command {

    /** Runs the command.
     *
     * @param repo repository that command is run in
     * @param args Array of String args by user
     */
    void execute(Repository repo, String[] args) throws IOException;

    /** Determines if a repository is needed to run command.
     *
     * @return boolean true if neneds repo
     */
    boolean needsRepo();

    /** Checks if the arguments are in the correct format for command.
     *
     * @param args Array String of args by user
     * @return true if in correct form
     */
    boolean checkArgs(String[] args);
}
