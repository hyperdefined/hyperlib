package lol.hyper.hyperlib.releases.modrinth;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModrinthRelease {

    private final ArrayList<String> supportedVersions = new ArrayList<>();
    private final ArrayList<String> supportedLoaders = new ArrayList<>();
    private final String version;
    private Date releaseDate;
    private String releaseType;
    private String versionPage;
    private final List<ModrinthDownload> downloads = new ArrayList<>();

    public ModrinthRelease(String version) {
        this.version = version;
    }

    public void addSupportedVersion(String version) {
        supportedVersions.add(version);
    }

    public List<String> getSupportedVersions() {
        return supportedVersions;
    }

    public List<String> getSupportedLoaders() {
        return supportedLoaders;
    }

    public void addSupportedLoader(String loader) {
        this.supportedLoaders.add(loader);
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

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public List<ModrinthDownload> getDownloads() {
        return downloads;
    }

    public void addDownload(ModrinthDownload download) {
        downloads.add(download);
    }

    public void setVersionPage(String versionPage) {
        this.versionPage = versionPage;
    }

    public String getVersionPage() {
        return versionPage;
    }

    @Override
    public String toString() {
        return version;
    }
}
