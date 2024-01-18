
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            return;
        }

        String file1 = args[0];
        String file2 = args[1];

        Commands2 commands2 = new Commands2(file1, file2);

    }
}
