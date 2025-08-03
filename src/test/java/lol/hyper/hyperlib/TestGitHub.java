package lol.hyper.hyperlib;

import lol.hyper.hyperlib.releases.github.GitHubPlugin;

import java.util.logging.Logger;

public class TestGitHub {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("TestGitHub");
        GitHubPlugin plugin = new GitHubPlugin("hyperdefined", "ToolStats");
        plugin.get();

        logger.info(plugin.getProjectPage());
        logger.info(plugin.getLatestRelease().toString());
    }
}
