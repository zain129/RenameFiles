import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zain I.
 **/

public class RenameFiles {

    private static final String CURRENT_STRING = "current";
    private static final String CURRENT_USER_DIRECTORY_PROP = "user.dir";
    private static String dirString;       // Directory String
    private static String srcString;       // Source String
    private static String destString;      // Destination String

    public static void main(String[] args) throws IOException {
        try {
            setData(args);
            renameFiles();
        } catch (Exception ex) {
            System.err.println("********** EXCEPTION CAUGHT **********");
            System.err.println(ex.getMessage());
            System.err.println("********** STACK TRACE **********");
            ex.printStackTrace();
        }
    }

    private static void renameFiles() throws IOException {
        Path path = Paths.get(dirString);

        List<File> files = Files.list(path)
                .filter(Files::isRegularFile)               // Ignoring the sub-directories
                .map(Path::toFile)
                .collect(Collectors.toList());

//        files.forEach(System.out::println);                 // Printing all files names
        System.out.println("\n\nTotal no. of files found: " + files.size());
        System.out.println("\nStarted renaming the files...");
        System.out.println("\nReplacing '" + srcString + "' with '" + destString + "'...");
        System.out.println("\nPlease wait until the program is finished...");

        files.forEach(file -> {
            try {
                Path source = Paths.get(dirString + "//" + file.getName());
                Files.move(source,
                        source.resolveSibling(source.getFileName().toString().replace(srcString, destString)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        System.out.println("\nFile renaming completed. Please check the directory...");
    }

    private static void setData(String[] args) throws IOException {
        if (args.length == 3) {
                /* args[0] = directory
                /* args[1] = word which needs to be replaced
                /* args[2] = replacing word
                */
            dirString = args[0];
            if (dirString.trim().equalsIgnoreCase(CURRENT_STRING)) {
                dirString = System.getProperty(CURRENT_USER_DIRECTORY_PROP);
            }
            System.out.println(args[0]);
            srcString = args[1].trim();
            destString = args[2].trim();
        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("\nPlease enter the Directory/Path of the files\n");
            dirString = reader.readLine();

            System.out.println("\nPlease enter the word(s) which needs to be replaced\n");
            srcString = reader.readLine();

            System.out.println("\nPlease enter the replacing word(s)\n");
            destString = reader.readLine();

            reader.close();
        }
    }
}
