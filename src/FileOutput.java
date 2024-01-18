import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class FileOutput {
    private static BufferedWriter output = null;

    public FileOutput(String fileName) throws IOException {
        output = new BufferedWriter(new FileWriter(fileName));
    }



    public static void writeToFile(String text) throws IOException {
        output.write(text);
        output.newLine();
    }

    public static void close() throws IOException {
        output.close();
    }
}