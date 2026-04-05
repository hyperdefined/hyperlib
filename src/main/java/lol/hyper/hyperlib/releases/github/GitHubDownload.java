package lol.hyper.hyperlib.releases.github;

public class GitHubDownload {

    private final String version;
    private String fileName;
    private String downloadUrl;
    private String sha256Hash;

    public GitHubDownload(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getSha256Hash() {
        return sha256Hash;
    }

    public void setSha256Hash(String sha256Hash) {
        this.sha256Hash = sha256Hash;
    }
}
