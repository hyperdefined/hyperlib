package lol.hyper.hyperlib.releases.modrinth;

public class ModrinthDownload {

    private String fileName;
    private String downloadUrl;
    private String sha1Hash;
    private String sha512Hash;

    /**
     * Create a Modrinth download object.
     */
    public ModrinthDownload() {
    }

    /**
     * Set the filename for this download.
     *
     * @param fileName The name of the file.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get the filename.
     *
     * @return The filename.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get the direct download link to this download.
     *
     * @return The direct download link from Modrinth.
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * Set the direct download link.
     *
     * @param downloadUrl The direct download link from Modrinth.
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    /**
     * Get the SHA1 hash of this file.
     *
     * @return The SHA1 has from Modrinth.
     */
    public String getSha1Hash() {
        return sha1Hash;
    }

    /**
     * Get the SHA256 hash of this file.
     *
     * @return The SHA256 has from Modrinth.
     */
    public String getSha512Hash() {
        return sha512Hash;
    }

    /**
     * Set the SHA1 hash for this file.
     *
     * @param sha1Hash The SHA1 hash.
     */
    public void setSha1Hash(String sha1Hash) {
        this.sha1Hash = sha1Hash;
    }

    /**
     * Set the SHA512 hash for this file.
     *
     * @param sha512Hash The SHA256 hash.
     */
    public void setSha512Hash(String sha512Hash) {
        this.sha512Hash = sha512Hash;
    }
}
