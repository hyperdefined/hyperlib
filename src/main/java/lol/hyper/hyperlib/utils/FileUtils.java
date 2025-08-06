package lol.hyper.hyperlib.utils;

import lol.hyper.hyperlib.HyperLib;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileUtils {

    /**
     * Read contents of a file.
     *
     * @param file The file to read.
     * @return The data from the file.
     */
    public static String readFile(File file) {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(file.toPath());
        } catch (IOException exception) {
            HyperLib.getPluginLogger().error("Unable to read file", exception);
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }

    /**
     * Write a file.
     *
     * @param data The data to write. This will be turned into a String.
     * @param file The file to write to.
     */
    public static void writeFile(Object data, File file) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(data.toString());
            writer.close();
        } catch (IOException exception) {
            HyperLib.getPluginLogger().error("Unable to write file", exception);
        }
    }
}
