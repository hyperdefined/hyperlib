package lol.hyper.hyperlib;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.Plugin;

public class HyperLib {

    private final Plugin plugin;
    private static ComponentLogger pluginLogger;

    private MiniMessage miniMessage;

    public HyperLib(Plugin plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        // setup MiniMessage
        miniMessage = MiniMessage.miniMessage();

        // hook into the plugin logger
        pluginLogger = plugin.getComponentLogger();
    }

    public static ComponentLogger getPluginLogger() {
        if (pluginLogger == null) {
            throw new IllegalStateException("Logger has not been set, please run setup()");
        }
        return pluginLogger;
    }

    public Plugin getPlugin() {
        if (plugin == null) {
            throw new IllegalStateException("Plugin has not been set, please run setup()");
        }
        return plugin;
    }

    public MiniMessage getMiniMessage() {
        if (miniMessage == null) {
            throw new IllegalStateException("MiniMessage has not been set, please run setup()");
        }
        return miniMessage;
    }
}
