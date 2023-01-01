import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CountLinesThread extends Thread {
    private final String fileName;
    private int count;

    public CountLinesThread(String fileName) {
        this.fileName = fileName;
        this.count = 0;
    }

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

    public int getCount() {
        return count;
    }
}
