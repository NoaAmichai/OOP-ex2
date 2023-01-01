import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;

public class CountLinesThreadsPool implements Callable<Integer> {
    private final String[] fileNames;
    private int count;
    private final int index;

    public CountLinesThreadsPool(String[] fileNames,int index) {
        this.fileNames = fileNames;
        this.count = 0;
        this.index = index;
    }

    @Override
    public Integer call() throws Exception {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileNames[index]));
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
