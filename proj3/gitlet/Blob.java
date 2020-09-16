package gitlet;

import java.io.Serializable;

/** Blob Object.
 * @author Aditi Mahajan
 */
public class Blob implements Serializable {
    /** Byte version of contents.*/
    private byte[] _blobContents;
    /** SHA. */
    private String _blobSHA1;


    /** Makes a Blob object.
     * @param name Name of the file that blob was created in
     * @param content Content of the blob
     */
    public Blob(String name, String content) {
        _blobContents = Utils.serialize(content);
        String blobText = name + content;
        _blobSHA1 = Utils.sha1(blobText);
    }

    /** Makes a Blob object.
     * @param name Name of the file that blob was created in
     * @param content Content of the blob
     */
    public Blob(String name, byte[] content) {
        _blobContents = content;
        byte[] nameByte = name.getBytes();
        byte[] blobBytes = new byte[nameByte.length + content.length];

        System.arraycopy(nameByte, 0, blobBytes, 0, nameByte.length);
        System.arraycopy(content, 0, blobBytes, nameByte.length,
                content.length);

        _blobSHA1 = Utils.sha1(blobBytes);
    }

    /** Gets blob's contents as a byte[].
     *
     * @return blob content
     */
    public byte[] getBlobContents() {
        return _blobContents;
    }

    /**
     * Gets blob's sha-1, name of the Blob file.
     * @return sha-1
     */
    public String getBlobSHA1() {
        return _blobSHA1;
    }
}
