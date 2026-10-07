package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reusable utility for filesystem operations (directory creation, cleaning, path validation).
 */
public final class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {
        // Prevent instantiation
    }

    /**
     * Ensures that the specified directory exists, creating all parent directories if necessary.
     *
     * @param dirPath Directory path to create/verify
     * @return File object representing the directory
     */
    public static File ensureDirectoryExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                LOGGER.info("Created directory: {}", dir.getAbsolutePath());
            }
        }
        return dir;
    }

    /**
     * Cleans all files within a directory without deleting the directory itself.
     *
     * @param dirPath Directory path to clean
     */
    public static void cleanDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            try {
                FileUtils.cleanDirectory(dir);
                LOGGER.info("Cleaned directory contents: {}", dirPath);
            } catch (IOException e) {
                LOGGER.warn("Failed to clean directory {}: {}", dirPath, e.getMessage());
            }
        }
    }

    /**
     * Deletes a directory and its entire tree recursively.
     */
    public static void deleteDirectoryRecursively(String dirPath) {
        Path path = Paths.get(dirPath);
        if (Files.exists(path)) {
            try {
                Files.walk(path)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                LOGGER.info("Deleted directory recursively: {}", dirPath);
            } catch (IOException e) {
                LOGGER.warn("Failed to recursively delete {}: {}", dirPath, e.getMessage());
            }
        }
    }
}
