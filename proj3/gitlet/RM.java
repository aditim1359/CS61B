package gitlet;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** RM command.
 * @author Aditi Mahajan
 */
public class RM implements Command {

    /** Executes  RM.
     *
     * @param repo repository that command is run in
     * @param args Array of String args by user
     */
    @Override
    public void execute(Repository repo, String[] args) {
        String fileName = args[0];
        Path filePath = Paths.get(repo.getCwdString(), fileName);

        Index repoIndex = repo.getIndex();
        boolean staged = repoIndex.getAdded().containsKey(fileName);
        boolean tracked = repoIndex.getCommitMap().containsKey(fileName);

        if (!tracked && !staged) {
            throw new GitletException("No reason to remove the file.");
        } else {
            if (staged) {
                repoIndex.getAdded().remove(fileName);
            }
            if (tracked) {
                repoIndex.addToRemoved(fileName);
                if (Files.exists(filePath)) {
                    File file = Utils.join(repo.getCwdString(), fileName);
                    Utils.restrictedDelete(file);
                }
            }
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
