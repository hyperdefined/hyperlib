package lol.hyper.hyperlib;

import lol.hyper.hyperlib.releases.hangar.HangarDownload;
import lol.hyper.hyperlib.releases.hangar.HangarPlugin;
import lol.hyper.hyperlib.releases.hangar.HangarRelease;
import lol.hyper.hyperlib.releases.modrinth.ModrinthPlugin;
import lol.hyper.hyperlib.releases.modrinth.ModrinthRelease;

import java.util.logging.Logger;

public class TestHangar {

    public static void main(String[] args) throws InterruptedException {
        Logger logger = Logger.getLogger("TestHangar");

        HangarPlugin plugin = new HangarPlugin("ToolStats", "paper");
        plugin.get();

        logger.info(plugin.getProjectPage());

        HangarRelease release = plugin.getReleaseByVersion("1.9.2-hotfix-2");
        logger.info(release.getVersionPage());
        for (HangarDownload download : release.getDownloads()) {
            logger.info(download.getDownloadUrl());
        }
    }
}
