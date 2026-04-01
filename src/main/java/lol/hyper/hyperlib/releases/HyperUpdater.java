package lol.hyper.hyperlib.releases;

import lol.hyper.hyperlib.HyperLib;
import lol.hyper.hyperlib.releases.github.GitHubDownload;
import lol.hyper.hyperlib.releases.github.GitHubPlugin;
import lol.hyper.hyperlib.releases.github.GitHubRelease;
import lol.hyper.hyperlib.releases.hangar.HangarPlugin;
import lol.hyper.hyperlib.releases.hangar.HangarRelease;
import lol.hyper.hyperlib.releases.modrinth.ModrinthDownload;
import lol.hyper.hyperlib.releases.modrinth.ModrinthPlugin;
import lol.hyper.hyperlib.releases.modrinth.ModrinthRelease;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class HyperUpdater {

    private final HyperLib hyperLib;
    private String hangarID = null;
    private String hangarLoader = null;

    private String modrinthID = null;

    private String organization = null;
    private String repository = null;

    public HyperUpdater(HyperLib hyperLib) {
        this.hyperLib = hyperLib;
    }

    public void setHangar(String id, String loader) {
        this.hangarID = id;
        this.hangarLoader = loader;
    }

    public void setModrinth(String projectID) {
        this.modrinthID = projectID;
    }

    public void setGitHub(String organization, String repository) {
        this.organization = organization;
        this.repository = repository;
    }

    public void check() {
        Bukkit.getAsyncScheduler().runNow(hyperLib.getPlugin(), scheduledTask -> {
            HangarPlugin hangarPlugin = new HangarPlugin(hangarID, hangarLoader);
            hangarPlugin.get();

            ModrinthPlugin modrinthPlugin = new ModrinthPlugin(modrinthID);
            modrinthPlugin.get();

            GitHubPlugin gitHubPlugin = new GitHubPlugin(organization, repository);
            gitHubPlugin.get();

            String pluginVersion = hyperLib.getPlugin().getPluginMeta().getVersion();
            HangarRelease currentHangar = hangarPlugin.getReleaseByVersion(pluginVersion);
            ModrinthRelease currentModrinth = modrinthPlugin.getReleaseByVersion(pluginVersion);
            GitHubRelease currentGitHub = gitHubPlugin.getReleaseByVersion(pluginVersion);

            // Make sure the version of the plugin is real.
            // This should only ever appear on dev versions.
            if (currentHangar == null) {
                HyperLib.getPluginLogger().warn("Unable to find plugin release for version {} on Hangar.", pluginVersion);
                return;
            }
            if (currentModrinth == null) {
                HyperLib.getPluginLogger().warn("Unable to find plugin release for version {} on Modrinth.", pluginVersion);
                return;
            }
            if (currentGitHub == null) {
                HyperLib.getPluginLogger().warn("Unable to find plugin release for version {} on GitHub.", pluginVersion);
                return;
            }

            // Get the plugin's jar.
            // Calculate the hash of the jar downloaded.
            // Then compare it to what's online.
            // Security!!!
            File pluginFile = new File(hyperLib.getPlugin().getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            String path = URLDecoder.decode(pluginFile.getPath(), StandardCharsets.UTF_8);
            String hash = sha512(Path.of(path));
            // If this is null, skip.
            if (hash != null) {
                ModrinthDownload currentVersionDownload = currentModrinth.getDownloads().getFirst();
                String onlineHash = currentVersionDownload.getSha512Hash();
                // In the event GitHub does not have the hash saved.
                if (onlineHash.equals("UNKNOWN")) {
                    HyperLib.getPluginLogger().warn("Unable to verify jar, GitHub has no hash to check.");
                } else {
                    if (!onlineHash.equalsIgnoreCase(hash)) {
                        HyperLib.getPluginLogger().error("======================================");
                        HyperLib.getPluginLogger().error("It looks like this jar file has been modified!");
                        HyperLib.getPluginLogger().error("Expected: {}", onlineHash);
                        HyperLib.getPluginLogger().error("Got: {}", hash);
                        HyperLib.getPluginLogger().error("For security, you should redownload this plugin.");
                        HyperLib.getPluginLogger().error("If the plugin was not modified, please report this issue.");
                        HyperLib.getPluginLogger().error("======================================");
                    } else {
                        HyperLib.getPluginLogger().info("Plugin has been verified!");
                    }
                }
            } else {
                HyperLib.getPluginLogger().error("======================================");
                HyperLib.getPluginLogger().error("Unable to calculate hash. This is a security check.");
                HyperLib.getPluginLogger().error("======================================");
            }

            // Check GitHub to see if we are outdated.
            // GitHub is source of versions, so rely on that for versions.
            // Things get posted to every site together, so this is fine.
            int buildsBehind = gitHubPlugin.buildsVersionsBehind(currentGitHub);
            if (buildsBehind == 0) {
                HyperLib.getPluginLogger().info("Awrff! You are running the latest version ({}).", pluginVersion);
                return;
            }

            // The plugin is not updated, send message to version pages in the console.
            HangarRelease latestHangar = hangarPlugin.getLatestRelease();
            ModrinthRelease latestModrinth = modrinthPlugin.getLatestRelease();
            GitHubRelease latestGitHub = gitHubPlugin.getLatestRelease();

            if (latestHangar == null) {
                HyperLib.getPluginLogger().warn("Unable to find latest release for plugin on Hangar.");
                return;
            }
            if (latestModrinth == null) {
                HyperLib.getPluginLogger().warn("Unable to find latest release for plugin on Modrinth.");
                return;
            }
            if (latestGitHub == null) {
                HyperLib.getPluginLogger().warn("Unable to find latest release for plugin on GitHub.");
                return;
            }

            HyperLib.getPluginLogger().warn("======================================");
            HyperLib.getPluginLogger().warn("There are updates available! You are {} version(s) behind.", buildsBehind);
            HyperLib.getPluginLogger().warn("See the links below for the latest version ({})", latestGitHub.getVersion());
            HyperLib.getPluginLogger().warn(latestGitHub.getVersionPage());
            HyperLib.getPluginLogger().warn(latestModrinth.getVersionPage());
            HyperLib.getPluginLogger().warn(latestHangar.getVersionPage());
            HyperLib.getPluginLogger().warn("======================================");
        });
    }

    public String sha512(Path path) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");

            try (InputStream is = Files.newInputStream(path)) {
                byte[] buffer = new byte[8192];
                int read;

                while ((read = is.read(buffer)) != -1) {
                    digest.update(buffer, 0, read);
                }
            }

            return java.util.HexFormat.of().formatHex(digest.digest());
        } catch (Exception exception) {
            HyperLib.getPluginLogger().error("Unable to get hash", exception);
            return null;
        }
    }
}
