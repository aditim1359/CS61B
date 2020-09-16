package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

/** Reset command.
 * @author Aditi Mahajan
 */
public class Reset implements Command {

    /** Executes Reset command.
     *
     * @param repo repository that command is run in
     * @param args Array of String args by user
     * @throws IOException exception
     */
    @Override
    public void execute(Repository repo, String[] args) throws IOException {
        String fullComHash = repo.getCommitStore().lookUpCommitHash(args[0]);
        if (fullComHash == null) {
            throw new GitletException("No commit with that id exists.");
        }

        Commit resetCommit = repo.getCommits().get(fullComHash);

        List<String> result = Utils.plainFilenamesIn(repo.getCwdString());

        for (String fileName : result) {
            if (resetCommit.getMap().containsKey(fileName)
                    && !repo.getIndex().getCommitMap()
                    .containsKey(fileName)) {
                throw new GitletException("There is an untracked file in "
                        + "the way; delete it, or add and commit it first.");
            }
        }

        for (String fileName : result) {
            if (!resetCommit.getMap().containsKey(fileName)) {
                File file = Utils.join(repo.getCwdString(), fileName);
                Utils.restrictedDelete(file);
            }
        }

        Checkout checkoutCommand = new Checkout();
        for (String fileName : resetCommit.getMap().keySet()) {
            String[] tempArgs = new String[] {fullComHash, "", fileName};
            checkoutCommand.execute(repo, tempArgs);
        }

        repo.setHEAD(fullComHash);

        Index newIndex = new Index(resetCommit.getMap());
        repo.setIndex(newIndex);

        repo.updateCurBranchHead(fullComHash);
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
