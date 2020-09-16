package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** Store of all blobs in repo.
 * @author Aditi Mahajan
 */
public class BlobStore implements Serializable {
    /** Map of blob hash to blob object. */
    private Map<String, Blob> _blobs;

    /** General init constructor.
     *
     */
    public BlobStore() {
        _blobs = new HashMap<String, Blob>();
    }

    /** Constructor for store given already have past blobs.
     *
     * @param prevBlobs map of blob SHA-1 hashes to blobs obj
     */
    public BlobStore(Map<String, Blob> prevBlobs) {
        _blobs = prevBlobs;
    }

    /** Adds a blob obj to the store.
     *
     * @param blob Blob Obj to add
     */
    public void addBlob(Blob blob) {
        _blobs.put(blob.getBlobSHA1(), blob);
    }

    /** Gets _blobs attribute.
     * @return _blobs
     */
    public Map<String, Blob> getBlobs() {
        return _blobs;
    }
}
