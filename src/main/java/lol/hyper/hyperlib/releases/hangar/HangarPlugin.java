package lol.hyper.hyperlib.releases.hangar;

import lol.hyper.hyperlib.HyperLib;
import lol.hyper.hyperlib.utils.JSONUtils;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HangarPlugin {

    private final String projectID;
    private final String loader;
    private String author;
    private String latest;
    private final List<HangarRelease> releases = new ArrayList<>();

    public HangarPlugin(String projectID, String loader) {
        this.projectID = projectID;
        this.loader = loader;
    }

    public void get() {
        String projectUrl = "https://hangar.papermc.io/api/v1/projects/" + projectID;
        JSONObject projectFetch = JSONUtils.requestJSONObject(projectUrl);
        if (projectFetch == null) {
            HyperLib.getPluginLogger().error("Unable to fetch plugin information from {}", projectUrl);
            return;
        }

        JSONObject namespace = projectFetch.getJSONObject("namespace");
        author = namespace.getString("owner");

        String versionsUrl = "https://hangar.papermc.io/api/v1/projects/" + projectID + "/versions";
        JSONObject versionsFetch = JSONUtils.requestJSONObject(versionsUrl);
        if (versionsFetch == null) {
            HyperLib.getPluginLogger().error("Unable to fetch plugin information from {}", versionsUrl);
            return;
        }
        JSONArray result = versionsFetch.getJSONArray("result");

        // Hangar stores each version as a JSONObject in a JSONArray called `result`.
        // Each JSON is a number, with 0 being the first one.
        // Load the main JSON, then iterate through the `result` JSONArray.
        for (int i = 0; i < result.length(); i++) {
            JSONObject releaseData = result.getJSONObject(i);
            String version = releaseData.getString("name");
            HangarRelease release = new HangarRelease(version, loader);
            release.setReleaseDate(releaseData.getString("createdAt"));
            release.setAuthor(releaseData.getString("author"));
            release.setVersionPage(getProjectPage() + "/versions/" + version);

            JSONObject platformDependencies = releaseData.getJSONObject("platformDependencies");
            // The loader the user specific is not listed on this version as supported.
            if (!platformDependencies.has(loader.toUpperCase(Locale.ROOT))) {
                continue;
            }

            // Add each version this release supports.
            JSONArray platformVersions = platformDependencies.getJSONArray(loader.toUpperCase(Locale.ROOT));
            for (int j = 0; j < platformVersions.length(); j++) {
                release.addSupportedVersion(platformVersions.getString(j));
            }

            // Save the information about the download for the loader.
            HangarDownload hangarDownload = new HangarDownload(release.getVersion());
            JSONObject downloads = releaseData.getJSONObject("downloads");
            JSONObject loaderDownloads = downloads.getJSONObject(loader.toUpperCase(Locale.ROOT));

            // Because Hangar supports external downloads, we must handle it.
            // If there is an external download, read the `externalUrl` string instead.
            String downloadUrl = loaderDownloads.optString("downloadUrl");
            String externalUrl = loaderDownloads.optString("externalUrl");
            // Furthermore, the file information is not present if the download is external.
            if (!downloadUrl.isEmpty()) {
                hangarDownload.setDownloadUrl(downloadUrl);
                JSONObject fileInfo = loaderDownloads.getJSONObject("fileInfo");
                hangarDownload.setFileName(fileInfo.getString("name"));
                hangarDownload.setSha256Hash(fileInfo.getString("sha256Hash"));
            } else if (!externalUrl.isEmpty()) {
                hangarDownload.setDownloadUrl(externalUrl);
                hangarDownload.setFileName(null);
                hangarDownload.setSha256Hash(null);
            } else {
                hangarDownload.setDownloadUrl(null);
            }
            release.addDownload(hangarDownload);

            releases.add(release);

            // If this is the first version, set it to the latest one.
            if (i == 0) {
                latest = version;
            }
        }
    }

    /**
     * Get all releases for this project.
     *
     * @return An array of HangarRelease objects.
     */
    public List<HangarRelease> getReleases() {
        return releases;
    }

    /**
     * Get a certain release by version.
     *
     * @param version The version to get.
     * @return The HangarRelease object, or null if the version does not exist.
     */
    public @Nullable HangarRelease getReleaseByVersion(String version) {
        return releases.stream().filter(release -> release.getVersion().equalsIgnoreCase(version)).findFirst().orElse(null);
    }

    /**
     * Get the latest version of this project.
     *
     * @return The HangarRelease for the latest version.
     */
    public @Nullable HangarRelease getLatestRelease() {
        return releases.stream().filter(release -> release.getVersion().equalsIgnoreCase(latest)).findFirst().get();
    }

    /**
     * Get how many versions outdated a given release is.
     *
     * @param release The release to check.
     * @return How many "builds behind" it is.
     */
    public int buildsVersionsBehind(HangarRelease release) {
        return releases.indexOf(release);
    }

    /**
     * Get the project page.
     *
     * @return The project page.
     */
    public String getProjectPage() {
        return "https://hangar.papermc.io/" + author + "/" + projectID;
    }
}
