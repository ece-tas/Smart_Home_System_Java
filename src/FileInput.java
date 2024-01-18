import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileInput {
    static String[] readFile(String path) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            List<String> nonEmptyLines = new ArrayList<>();

            for (String line : lines) {
                if (!line.trim().isEmpty()) { // check if line is not empty after trimming whitespace
                    nonEmptyLines.add(line);
                }
            }

            String[] results = new String[nonEmptyLines.size()];
            nonEmptyLines.toArray(results);

            return results;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}