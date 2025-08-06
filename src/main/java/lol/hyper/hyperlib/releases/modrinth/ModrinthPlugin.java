package lol.hyper.hyperlib.releases.modrinth;

import lol.hyper.hyperlib.HyperLib;
import lol.hyper.hyperlib.utils.JSONUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ModrinthPlugin {

    private final String projectID;
    private final List<ModrinthRelease> releases = new ArrayList<>();
    private String slug;
    private String type;
    private String latest;

    /**
     * Create a Modrinth plugin object.
     *
     * @param projectID    The project ID. This can be the slug or the ID from Modrinth.
     */
    public ModrinthPlugin(String projectID) {
        this.projectID = projectID;
    }

    /**
     * Load the project information.
     */
    public void get() {
        String projectUrl = "https://api.modrinth.com/v2/project/" + projectID;
        JSONObject projectFetch = JSONUtils.requestJSONObject(projectUrl);
        if (projectFetch == null) {
            HyperLib.getPluginLogger().error("Unable to fetch plugin information from {}", projectUrl);
            return;
        }

        slug = projectFetch.getString("slug");
        type = projectFetch.getString("project_type");

        String versionsUrl = "https://api.modrinth.com/v2/project/" + projectID + "/version";
        JSONArray versionsFetch = JSONUtils.requestJSONArray(versionsUrl);
        if (versionsFetch == null) {
            HyperLib.getPluginLogger().error("Unable to fetch plugin information from {}", versionsUrl);
            return;
        }

        // Modrinth stores each version as a JSONObject in a JSONArray.
        // Each JSON is a number, with 0 being the first one.
        // Load the main JSON, then iterate through the JSONArray.
        for (int i = 0; i < versionsFetch.length(); i++) {
            // Get the JSON with this release.
            JSONObject versionData = versionsFetch.getJSONObject(i);
            // Save the information about the release.
            ModrinthRelease release = new ModrinthRelease(versionData.getString("version_number"));
            release.setReleaseType(versionData.getString("version_type"));
            release.setReleaseDate(versionData.getString("date_published"));
            release.setVersionPage(getProjectPage() + "/version/" + versionData.getString("version_number"));

            // The files attached to the version are in a JSONArray.
            JSONArray downloads = versionData.getJSONArray("files");
            for (int j = 0; j < downloads.length(); j++) {
                // Get the download information for this version.
                JSONObject downloadData = downloads.getJSONObject(j);
                ModrinthDownload download = new ModrinthDownload();
                download.setDownloadUrl(downloadData.getString("url"));
                download.setFileName(downloadData.getString("filename"));
                JSONObject hashes = downloadData.getJSONObject("hashes");
                download.setSha1Hash(hashes.getString("sha1"));
                download.setSha512Hash(hashes.getString("sha512"));

                release.addDownload(download);
            }

            // Get the loaders this version is for.
            JSONArray gameVersions = versionData.getJSONArray("game_versions");
            JSONArray supportedLoaders = versionData.getJSONArray("loaders");
            for (int j = 0; j < gameVersions.length(); j++) {
                release.addSupportedVersion(gameVersions.getString(j));
            }
            for (int k = 0; k < supportedLoaders.length(); k++) {
                release.addSupportedLoader(supportedLoaders.getString(k));
            }

            releases.add(release);

            // If this is the first version, set it to the latest one.
            if (i == 0) {
                latest = versionData.getString("version_number");
            }
        }
    }

    /**
     * Get all releases for this project.
     *
     * @return An array of ModrinthRelease objects.
     */
    public List<ModrinthRelease> getReleases() {
        return releases;
    }

    /**
     * Get a certain release by version.
     *
     * @param version The version to get.
     * @return The ModrinthRelease object, or null if the version does not exist.
     */
    public @Nullable ModrinthRelease getReleaseByVersion(String version) {
        return releases.stream().filter(release -> release.getVersion().equalsIgnoreCase(version)).findFirst().orElse(null);
    }

    /**
     * Get the latest version of this project.
     *
     * @return The ModrinthRelease for the latest version.
     */
    public @Nullable ModrinthRelease getLatestRelease() {
        return releases.stream().filter(release -> release.getVersion().equalsIgnoreCase(latest)).findFirst().get();
    }

    /**
     * Get how many versions outdated a given release is.
     *
     * @param release The release to check.
     * @return How many "builds behind" it is.
     */
    public int buildsVersionsBehind(ModrinthRelease release) {
        return releases.indexOf(release);
    }

    /**
     * Get the project page.
     *
     * @return The project page.
     */
    public String getProjectPage() {
        return "https://modrinth.com/" + type + "/" + slug;
    }
}
