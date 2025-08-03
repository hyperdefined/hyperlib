package lol.hyper.hyperlib;

import lol.hyper.hyperlib.releases.modrinth.ModrinthPlugin;
import lol.hyper.hyperlib.releases.modrinth.ModrinthRelease;

import java.util.logging.Logger;

public class TestModrinth {

    public static void main(String[] args) throws InterruptedException {
        Logger logger = Logger.getLogger("TestModrinth");
        ModrinthPlugin plugin = new ModrinthPlugin("ToolStats");
        plugin.get();
        logger.info(plugin.getProjectPage());
        logger.info(plugin.getLatestRelease().toString());

        ModrinthRelease release = plugin.getReleaseByVersion("1.8.5");
        logger.info(release.getVersionPage());
        logger.info(release.getReleaseDate().toString());
        logger.info(release.getReleaseType());
    }
}
