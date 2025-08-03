package lol.hyper.hyperlib.utils;

import lol.hyper.hyperlib.HyperLib;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

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
     * Format a string into a Component. Supports legacy and MiniMessage.
     *
     * @param input The message to format.
     * @return Formatting Component.
     */
    public Component format(String input) {
        // if the config message is empty, don't send it
        if (input.isEmpty()) {
            return null;
        }

        // the final component for this lore
        Component component;
        // if we match the old color codes, then format them as so
        Matcher hexMatcher = HEX_PATTERN.matcher(input);
        Matcher colorMatcher = COLOR_CODES.matcher(input);
        if (hexMatcher.find() || colorMatcher.find()) {
            component = LegacyComponentSerializer.legacyAmpersand().deserialize(input);
        } else {
            // otherwise format them normally
            component = hyperLib.getMiniMessage().deserialize(input);
        }

        return component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }
}
