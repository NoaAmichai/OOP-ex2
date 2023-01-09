import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;

/***
 * our class has two parameters ,filename that holds the file name and count that counts the lines.
 */
public class CountLinesThreadsPool implements Callable<Integer> {
    private final String fileName;
    private int count;

    /***
     * Constructor
     * @param fileName The name of the file we want to read.
     */
    public CountLinesThreadsPool(String fileName) {
        this.fileName = fileName;
        this.count = 0;
    }

    /***
     * Our function implements the call method from the callable interface,
     * it creates a bufferedReader for the file and keeps going to the
     * next line and adds to count till there are no more lines.Lastly it closes the reader,and returns count.
     * @return returns the count which holds the number of lines.
     * @throws Exception
     */
    @Override
    public Integer call() throws Exception {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while (reader.readLine() != null) {
                count++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

}
