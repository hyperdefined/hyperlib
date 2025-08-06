package lol.hyper.hyperlib.utils;

import lol.hyper.hyperlib.HyperLib;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
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
        // If the input is empty, don't format it.
        if (input.isEmpty()) {
            return Component.empty();
        }

        Component component;
        // Match and format based on the format of the input.
        Matcher hexMatcher = HEX_PATTERN.matcher(input);
        Matcher colorMatcher = COLOR_CODES.matcher(input);
        if (hexMatcher.find() || colorMatcher.find()) {
            // Format them as legacy formatting.
            component = LegacyComponentSerializer.legacyAmpersand().deserialize(input);
        } else {
            // Format them as regular MiniMessage.
            component = hyperLib.getMiniMessage().deserialize(input);
        }

        return component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    /**
     * Format a given list of strings into a Component. Supports legacy and MiniMessage.
     *
     * @param lines The input strings.
     * @return All lines formatted into one Component with new lines between each string.
     */
    public Component formatMultiLine(List<String> lines) {
        Component component = Component.empty();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            // If it's the final line, don't add a new line.
            if (i == (lines.size() - 1)) {
                component = component.append(format(line));
            } else {
                // Make sure to add a new line to the end.
                component = component.append(format(line)).append(Component.newline());
            }
        }
        return component;
    }
}
