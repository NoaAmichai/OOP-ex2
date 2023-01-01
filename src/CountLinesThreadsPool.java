import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;

public class CountLinesThreadsPool implements Callable<Integer> {
    private final String fileName;
    private int count;

    public CountLinesThreadsPool(String fileName) {
        this.fileName = fileName;
        this.count = 0;
    }

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
