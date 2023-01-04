import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class CustomExecutor{ // implements Executor {
    BlockingDeque<Task> queue;


    public void gracefullyTerminate() {
    }

    public String getCurrentMax() {
    }
}
