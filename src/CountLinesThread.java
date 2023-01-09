import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/***
 * our class has two parameters ,filename that holds the file name and count that counts the lines.
 */
public class CountLinesThread extends Thread {
    private final String fileName;
    private int count;

    /***
     * Constructor
     * @param fileName
     */
    public CountLinesThread(String fileName) {
        this.fileName = fileName;
        this.count = 0;
    }

    /***
     * Our method implements the run method from the Runnable interface.
     * it creates a bufferedReader for the file and keeps going to the
     * next line and adds to count till there are no more lines and lastly closes the reader.
     */
    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while (reader.readLine() != null) {
                count++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Getter for count
     * @return returns count
     */
    public int getCount() {
        return count;
    }
}
