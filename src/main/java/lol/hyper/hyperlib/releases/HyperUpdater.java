package lol.hyper.hyperlib.releases;

import lol.hyper.hyperlib.HyperLib;
import lol.hyper.hyperlib.releases.github.GitHubPlugin;
import lol.hyper.hyperlib.releases.github.GitHubRelease;
import lol.hyper.hyperlib.releases.hangar.HangarPlugin;
import lol.hyper.hyperlib.releases.hangar.HangarRelease;
import lol.hyper.hyperlib.releases.modrinth.ModrinthPlugin;
import lol.hyper.hyperlib.releases.modrinth.ModrinthRelease;
import org.bukkit.Bukkit;

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
            GitHubRelease currentGitHub = gitHubPlugin.getReleaseByVersion(pluginVersion);

            // The plugin version is not on GitHub
            // Since it's not released, don't check for updates.
            if (currentGitHub == null) {
                HyperLib.getPluginLogger().warn(("Plugin version is not on GitHub!"));
                return;
            }

            // Check GitHub to see if we are outdated.
            int buildsBehind = gitHubPlugin.buildsVersionsBehind(currentGitHub);
            if (buildsBehind == 0) {
                HyperLib.getPluginLogger().info("Yay! You are running the lates version!");
                return;
            }

            // The plugin is not updated, send message to version pages in the console.
            HangarRelease latestHangar = hangarPlugin.getLatestRelease();
            ModrinthRelease latestModrinth = modrinthPlugin.getLatestRelease();
            GitHubRelease latestGitHub = gitHubPlugin.getLatestRelease();

            HyperLib.getPluginLogger().warn("There are updates available! You are {} version(s) behind.", buildsBehind);
            HyperLib.getPluginLogger().warn("See the links below for the latest version ({})", latestGitHub.getVersion());
            HyperLib.getPluginLogger().warn(latestGitHub.getVersionPage());
            HyperLib.getPluginLogger().warn(latestModrinth.getVersionPage());
            HyperLib.getPluginLogger().warn(latestHangar.getVersionPage());
        });
    }
}
