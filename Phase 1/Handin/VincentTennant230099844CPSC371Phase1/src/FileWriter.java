import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * main.java
 * CPSC371
 * Vincent Tennnant
 * 230099844
 * 
 * A file writer. Writes to a file.
 */
public class FileWriter {

    private PrintWriter writer;

    public FileWriter(String name) {
        try {
            writer = new PrintWriter(name);
        } catch(FileNotFoundException e) {
            System.out.println("Could not make a new file   "+e);
        }
    }

    public void newLine() {
        writer.println();
    }

    public void writeToFile(String to_write) {
        writer.write(to_write);
    }

    public void closeWriter() {
        writer.flush();
        writer.close();
    }
}
