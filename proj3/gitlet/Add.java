package gitlet;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Add Command.
 * @author Aditi Mahajan
 */
public class Add implements Command {

    /** Executes add command.
     *
     * @param repo repository that command is run in
     * @param args Array of String args by user
     */
    @Override
    public void execute(Repository repo, String[] args) {
        String fileName = args[0];
        String cwd = repo.getCwdString();

        Path filePath = Paths.get(cwd, fileName);
        if (!Files.exists(filePath)) {
            throw new GitletException("File does not exist.");
        }

        File file = Utils.join(cwd, fileName);

        Blob fileBlob = new Blob(fileName, Utils.readContents(file));
        repo.getBlobs().addBlob(fileBlob);

        Index repoIndex = repo.getIndex();

        if (repoIndex.getCommitMap().containsKey(fileName) && repoIndex
                .getCommitMap().get(fileName)
                .equals(fileBlob.getBlobSHA1())) {
            repoIndex.getAdded().remove(fileName);
            repoIndex.getRemoved().remove(fileName);
        } else if (repoIndex.getAdded().containsKey(fileName)) {
            repoIndex.getAdded().replace(fileName, fileBlob.getBlobSHA1());
        } else {
            repoIndex.getAdded().put(fileName, fileBlob.getBlobSHA1());
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
