package gitlet;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/** Commit Object.
 * @author Aditi Mahajan
 */
public class Commit implements Serializable {

    /** String of time of commit. */
    private String _timestamp;
    /** String of message of commit. */
    private String _logMessage;
    /** Map of file name to blob hash. */
    private Map<String, String> _map;
    /** String of SHA of commit. */
    private String _commitSHA1;
    /** main parent of commit hash. */
    private String _parent1;
    /** Merge parent of commit hash. */
    private String _parent2;

    /** Constructor for commit with no merge.
     *
     * @param message commit message
     * @param parent single parent commit Hash (SHA-1)
     * @param indexMap snapshot of repo's index map updated
     *                 with files from added and removed
     */
    public Commit(String message, String parent, Map<String, String> indexMap) {
        _logMessage = message;
        _parent1 = parent;
        _parent2 = "";
        ZonedDateTime date = ZonedDateTime.now();
        DateTimeFormatter form = DateTimeFormatter.ofPattern("EEE LLL d"
                + " HH:mm:ss y Z");
        _timestamp = date.format(form);
        _map = indexMap;
        _commitSHA1 = Utils.sha1(_timestamp + _logMessage
                + _parent1 + _parent2 + _map);
    }

    /** Constructor for commit with a merge (2 parents).
     *
     * @param message commit message
     * @param parent1 original branch's HEAD commit
     * @param parent2 merged branch's HEAD commit
     * @param indexMap snapshot of repo's index amp updated
     *                 with staged files from merge
     */
    public Commit(String message, String parent1, String parent2,
                  Map<String, String> indexMap) {
        _logMessage = message;
        _parent1 = parent1;
        _parent2 = parent2;
        ZonedDateTime date = ZonedDateTime.now();
        DateTimeFormatter form = DateTimeFormatter.ofPattern("EEE LLL d"
                + " HH:mm:ss y Z");
        _timestamp = date.format(form);
        _map = indexMap;
        _commitSHA1 = Utils.sha1(_timestamp + _logMessage
                + _parent1 + _parent2 + _map);
    }

    /** Constructor for init.
     *
     */
    public Commit() {
        _logMessage = "initial commit";
        _parent1 = "";
        _parent2 = "";

        ZoneId zoneID = ZoneId.of("UTC");
        final int year = 1970;
        int month = 1;
        int dayOfMonth = 1;
        int time = 0;
        ZonedDateTime date = ZonedDateTime.of(year, month,
                dayOfMonth, time, time, time, time, zoneID);
        ZoneId sysZoneID = ZoneId.systemDefault();
        ZonedDateTime sysDate = date.withZoneSameInstant(sysZoneID);
        DateTimeFormatter form = DateTimeFormatter.ofPattern("EEE LLL d"
                + " HH:mm:ss y Z");
        _timestamp = sysDate.format(form);

        _map = null;
        _commitSHA1 = Utils.sha1(_timestamp + _logMessage + _parent1
                + _parent2 + _map);
    }

    /** Sets commit map to provided one.
     *
     * @param indexMap given map
     */
    public void setMap(Map<String, String> indexMap) {
        _map = indexMap;
    }

    /** Commit's timestamp.
     *
     * @return timestamp
     */
    public String getTimestamp() {
        return _timestamp;
    }

    /** Commit's log message.
     *
     * @return log message
     */
    public String getLogMessage() {
        return _logMessage;
    }

    /** Commit's SHA.
     *
     * @return SHA
     */
    public String getCommitSHA1() {
        return _commitSHA1;
    }

    /** Commit's main parent.
     *
     * @return parent 1
     */
    public String getParent1() {
        return _parent1;
    }

    /** Commit's merge parent.
     *
     * @return parent 2
     */
    public String getParent2()  {
        return _parent2;
    }

    /** Commit's file map.
     *
     * @return map
     */
    public Map<String, String> getMap() {
        return _map;
    }
}
