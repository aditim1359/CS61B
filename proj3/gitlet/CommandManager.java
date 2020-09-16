package gitlet;

import java.io.IOException;
import java.util.HashMap;

/** Command Manager to store all commands.
 * @author Aditi Mahajan*/

public class CommandManager {
    /** Map of string version of command to obj. */
    private HashMap<String, Command> commandMap;

    /** Constructs a store of all gitlet commands.
     * Makes looking at Main.java easier.
     *
     */
    public CommandManager() {
        commandMap = new HashMap<String, Command>();
        commandMap.put("init", new Init());
        commandMap.put("add", new Add());
        commandMap.put("rm", new RM());
        commandMap.put("commit", new CommitCommand());
        commandMap.put("checkout", new Checkout());
        commandMap.put("status", new Status());
        commandMap.put("log", new Log());
        commandMap.put("global-log", new GlobalLog());
        commandMap.put("branch", new BranchCommand());
        commandMap.put("rm-branch", new RMBranch());
        commandMap.put("reset", new Reset());
        commandMap.put("merge", new Merge());
        commandMap.put("find", new Find());
    }

    /** Checks that args are in the correct format for that command.
     *  Then, executes the command or throws exception.
     *
     * @param commandStr Command user inputted.
     * @param repo current repo
     * @param args args for the command user inputted
     * @throws IOException exception
     */
    public void execute(String commandStr, Repository repo, String[] args)
            throws IOException {
        Command runCommand = commandMap.get(commandStr);
        if (runCommand == null) {
            throw new GitletException("No command with that name exists.");
        }
        if (runCommand.checkArgs(args)) {
            runCommand.execute(repo, args);
        } else {
            throw new GitletException("Incorrect operands.");
        }
    }

}
