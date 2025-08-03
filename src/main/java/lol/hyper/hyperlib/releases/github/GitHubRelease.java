package lol.hyper.hyperlib.releases.github;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class GitHubRelease {

    private final String version;
    private String author;
    private String versionPage;
    private Date releaseDate;
    private final ArrayList<GitHubDownload> downloads = new ArrayList<>();

    public GitHubRelease(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(releaseDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.releaseDate = Date.from(offsetDateTime.toInstant());
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVersionPage() {
        return versionPage;
    }

    public void setVersionPage(String versionPage) {
        this.versionPage = versionPage;
    }

    public ArrayList<GitHubDownload> getDownloads() {
        return downloads;
    }

    public void addDownload(GitHubDownload download) {
        downloads.add(download);
    }
}