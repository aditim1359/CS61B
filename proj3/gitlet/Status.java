package gitlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/** Status Command.
 * @author Aditi Mahajan
 */
public class Status implements Command {

    /** Executes status.
     *
     * @param repo repository that command is run in
     * @param args Array of String args by user
     */
    @Override
    public void execute(Repository repo, String[] args) {
        System.out.println("=== Branches ===");
        Set<String> setBr = repo.getBranches().getBranches().keySet();
        TreeSet<String> orderedBr = new TreeSet<String>(setBr);
        for (String brName : orderedBr) {
            if (brName.equals(repo.getCurBranchName())) {
                System.out.print("*");
            }
            System.out.println(brName);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        Set<String> set = repo.getIndex().getAdded().keySet();
        TreeSet<String> orderedSet = new TreeSet<String>(set);
        for (String fileName : orderedSet) {
            System.out.println(fileName);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        ArrayList<String> ordered = repo.getIndex().getRemoved();
        Collections.sort(ordered);
        for (String fileName : ordered) {
            System.out.println(fileName);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        System.out.println("=== Untracked Files ===");
        System.out.println();
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
