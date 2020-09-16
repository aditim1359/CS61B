package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Aditi Mahajan
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {
        try {
            String cwdString = System.getProperty("user.dir");

            if (args.length == 0) {
                throw new GitletException("Please enter a command.");
            }

            String commandStr = args[0];
            CommandManager commands = new CommandManager();

            if (commandStr.equals("init")) {
                Repository repo = new Repository();
                saveToDisk(repo);
                System.exit(0);
            } else {
                Repository repo = loadFromDisk(cwdString);
                commands.execute(commandStr, repo,
                        Arrays.copyOfRange(args, 1, args.length));
                saveToDisk(repo);
                System.exit(0);
            }
        } catch (GitletException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    /** Loads the gitlet repository from the disk.
     *
     * @param cwdString CWD as a string
     * @return created repository with files as where when last saved to disk
     */
    public static Repository loadFromDisk(String cwdString) {
        Path workDirec = Paths.get(cwdString);
        Path gitletPath = Paths.get(cwdString, ".gitlet");
        if (!Files.exists(gitletPath)) {
            throw new GitletException("Not in an initialized "
                    + "Gitlet directory.");
        }

        CommitStore commitStore = null;
        File load = new File(gitletPath + File.separator + "COMMIT_STORE");
        if (load.exists()) {
            commitStore = Utils.readObject(load, CommitStore.class);
        }

        String curBranchName = null;
        File loadBranch = new File(gitletPath + File.separator
                + "CUR_BRANCH_NAME");
        if (loadBranch.exists()) {
            curBranchName = Utils.readObject(loadBranch, String.class);
        }

        Index index = null;
        File loadIndex = new File(gitletPath + File.separator + "INDEX");
        if (loadIndex.exists()) {
            index = Utils.readObject(loadIndex, Index.class);
        }

        String head = null;
        File loadHead = new File(gitletPath + File.separator + "HEAD");
        if (loadHead.exists()) {
            head = Utils.readObject(loadHead, String.class);
        }

        BranchStore branchesMap = null;
        File loadBranchStore = new File(gitletPath + File.separator
                + "BRANCHES");
        if (loadBranchStore.exists()) {
            branchesMap = Utils.readObject(loadBranchStore, BranchStore.class);
        }

        BlobStore blobsMap = null;
        File loadBlobStore = new File(gitletPath + File.separator + "BLOBS");
        if (loadBlobStore.exists()) {
            blobsMap = Utils.readObject(loadBlobStore, BlobStore.class);
        }

        Repository repo = new Repository(cwdString,
                gitletPath, commitStore, blobsMap, branchesMap,
                index, head, curBranchName);

        return repo;
    }

    /** After command executed, saves gitlet directory to disk.
     *
     * @param repo current new state of gitlet
     * @throws IOException exception
     */
    public static void saveToDisk(Repository repo) throws IOException {
        Path gitletPath = repo.getGitletDirec();
        File gitletFile = gitletPath.toFile();

        if (!Files.exists(gitletPath)) {
            gitletFile.mkdir();
        } else if (!gitletFile.isDirectory()) {
            throw new GitletException(".gitlet is not a directory");
        }

        Utils.serialize(repo.getCommitStore(), new File(gitletFile,
                "COMMIT_STORE"));
        Utils.serialize(repo.getCurBranchName(), new File(gitletFile,
                "CUR_BRANCH_NAME"));
        Utils.serialize(repo.getIndex(), new File(gitletFile, "INDEX"));
        Utils.serialize(repo.getHEAD(), new File(gitletFile, "HEAD"));
        Utils.serialize(repo.getBranches(), new File(gitletFile, "BRANCHES"));
        Utils.serialize(repo.getBlobs(), new File(gitletFile, "BLOBS"));
    }
}
