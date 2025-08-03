package lol.hyper.hyperlib.utils;

import lol.hyper.hyperlib.HyperLib;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    private final HyperLib hyperLib;

    public final Pattern COLOR_CODES = Pattern.compile("[&§]([0-9a-fk-or])");
    public final Pattern HEX_PATTERN = Pattern.compile("[&§]#([A-Fa-f0-9]{6})");

    public TextUtils(HyperLib hyperLib) {
        this.hyperLib = hyperLib;
    }

    /**
     * Format a string from the config.
     *
     * @param configName The config to format.
     * @return Formatted string, null if the configName doesn't exist.
     */
    public Component format(YamlConfiguration config, String configName) {
        String message = config.getString(configName);
        if (message == null) {
            HyperLib.getPluginLogger().warn("Unable to find config message for: {}", configName);
            return null;
        }

        // if the config message is empty, don't send it
        if (message.isEmpty()) {
            return null;
        }

        // the final component for this lore
        Component component;
        // if we match the old color codes, then format them as so
        Matcher hexMatcher = HEX_PATTERN.matcher(message);
        Matcher colorMatcher = COLOR_CODES.matcher(message);
        if (hexMatcher.find() || colorMatcher.find()) {
            component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
        } else {
            // otherwise format them normally
            component = hyperLib.getMiniMessage().deserialize(message);
        }

        return component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }
}
