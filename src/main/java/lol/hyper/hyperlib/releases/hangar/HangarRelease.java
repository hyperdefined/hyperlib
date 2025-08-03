package lol.hyper.hyperlib.releases.hangar;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class HangarRelease {

    private final ArrayList<String> supportedVersions = new ArrayList<>();
    private final String version;
    private final String loader;
    private String author;
    private String versionPage;
    private Date releaseDate;
    private final ArrayList<HangarDownload> downloads = new ArrayList<>();

    public HangarRelease(String version, String loader) {
        this.version = version;
        this.loader = loader;
    }

    public void addSupportedVersion(String version) {
        supportedVersions.add(version);
    }

    public ArrayList<String> getSupportedVersions() {
        return supportedVersions;
    }

    public String getLoader() {
        return loader;
    }

    public String getVersion() {
        return version;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setReleaseDate(String releaseDate) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(releaseDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.releaseDate = Date.from(offsetDateTime.toInstant());
    }

    public ArrayList<HangarDownload> getDownloads() {
        return downloads;
    }

    public void addDownload(HangarDownload download) {
        downloads.add(download);
    }

    public void setVersionPage(String versionPage) {
        this.versionPage = versionPage;
    }

    public String getVersionPage() {
        return versionPage;
    }
}
