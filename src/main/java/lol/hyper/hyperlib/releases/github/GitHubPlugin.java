package lol.hyper.hyperlib.releases.github;

import lol.hyper.hyperlib.HyperLib;
import lol.hyper.hyperlib.utils.JSONUtils;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GitHubPlugin {

    private final String organization;
    private final String repository;
    private final List<GitHubRelease> releases = new ArrayList<>();
    private String latest;
    private String gitHubUrl;

    public GitHubPlugin(String organization, String repository) {
        this.organization = organization;
        this.repository = repository;
    }

    public void get() {
        gitHubUrl = "https://github.com/" + organization + "/" + repository;
        String apiUrl = "https://api.github.com/repos/" + organization + "/" + repository;
        JSONArray releasesFetch = JSONUtils.requestJSONArray(apiUrl + "/releases");
        if (releasesFetch == null) {
            HyperLib.getPluginLogger().error("Unable to fetch plugin information from {}", apiUrl + "/releases");
            return;
        }

        // GitHub stores each release in an JSONArray
        // Each element is a JSONObject
        for (int i = 0; i < releasesFetch.length(); i++) {
            JSONObject releaseData = releasesFetch.getJSONObject(i);
            String version = releaseData.getString("tag_name");
            GitHubRelease gitHubRelease = new GitHubRelease(version);

            // "login" is the username
            JSONObject author = releaseData.getJSONObject("author");
            gitHubRelease.setAuthor(author.getString("login"));

            gitHubRelease.setVersionPage(gitHubUrl + "/releases/tag/" + version);
            gitHubRelease.setReleaseDate(releaseData.getString("published_at"));

            // assets are the files on the release
            JSONArray assets = releaseData.getJSONArray("assets");
            for (int j = 0; j < assets.length(); j++) {
                JSONObject assetData = assets.getJSONObject(j);

                // only save jar files for download
                if (!assetData.getString("content_type").equalsIgnoreCase("application/java-archive")) {
                    continue;
                }

                GitHubDownload gitHubDownload = new GitHubDownload(version);
                gitHubDownload.setDownloadUrl(assetData.getString("browser_download_url"));
                gitHubDownload.setFileName(assetData.getString("name"));
                gitHubDownload.setSha256Hash(assetData.getString("digest").replace("sha256:", ""));
                gitHubRelease.addDownload(gitHubDownload);
            }

            releases.add(gitHubRelease);

            // the first element is the latest release
            if (i == 0) {
                latest = version;
            }
        }
    }

    /**
     * Get all releases for this project.
     *
     * @return An array of GitHubRelease objects.
     */
    public List<GitHubRelease> getReleases() {
        return releases;
    }

    /**
     * Get a certain release by version.
     *
     * @param version The version to get.
     * @return The GitHubRelease object, or null if the version does not exist.
     */
    public @Nullable GitHubRelease getReleaseByVersion(String version) {
        return releases.stream().filter(release -> release.getVersion().equalsIgnoreCase(version)).findFirst().orElse(null);
    }

    /**
     * Get the latest version of this project.
     *
     * @return The GitHubRelease for the latest version.
     */
    public @Nullable GitHubRelease getLatestRelease() {
        return releases.stream().filter(release -> release.getVersion().equalsIgnoreCase(latest)).findFirst().get();
    }

    /**
     * Get how many versions outdated a given release is.
     *
     * @param release The release to check.
     * @return How many "builds behind" it is.
     */
    public int buildsVersionsBehind(GitHubRelease release) {
        return releases.indexOf(release);
    }

    /**
     * Get the project page.
     *
     * @return The project page.
     */
    public String getProjectPage() {
        return gitHubUrl;
    }
}
