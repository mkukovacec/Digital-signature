package hr.fer.nos.lab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * <p>Created: 2018-05-20 11:17:13 AM</p>
 *
 * @author marin
 */
public class FileUtils {

    public static byte[] readFile(final String path) throws IOException {
        final byte[] encoded = Files.readAllBytes(Paths.get(path));
        return encoded;
    }

    public static void writeToFile(final String path, final byte[] content) throws IOException {
        Files.write(Paths.get(path), content, StandardOpenOption.CREATE);
    }

}
