package InputOutput;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * FileWriter.java
 * CPSC371
 * Vincent Tennant
 * 230099844
 *
 * A simple class that can be used to create, an write to a file. The file type that's created is specified by the name
 * of the file passed to the constructor.
 *
 * Eg. "this_is_a_file.txt" creates a .txt file.
 *     "this_is_a_file_also.csv" creates a .csv file for use in excel(or excel like software).
 *
 * FileWriter is not capable of reading, or modifying existing file. The sole purpose of FileWriter id to create, and
 * write to a new file.
 * NOTE: By using a name of a file that already exists the old file of the same name will be
 * overwritten.
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
