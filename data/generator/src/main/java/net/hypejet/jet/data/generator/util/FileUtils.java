package net.hypejet.jet.data.generator.util;

import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Utilities for file management.
 *
 * @since 1.0
 */
public final class FileUtils {

    private FileUtils() {}

    /**
     * Deletes the specified path if exists. If the path is a directory, child paths are deleted using the same method.
     *
     * @param path the path to remove
     * @throws IOException if an I/O error occurs
     * @since 1.0
     */
    public static void deleteRecursively(@NonNull Path path) throws IOException {
        if (!Files.exists(path)) return;

        if (Files.isDirectory(path)) {
            try (Stream<Path> files = Files.list(path)) {
                for (Path file : files.toArray(Path[]::new)) {
                    deleteRecursively(file);
                }
            }
        }

        Files.deleteIfExists(path);
    }
}