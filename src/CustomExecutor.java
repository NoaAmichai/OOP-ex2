import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.*;


public class CustomExecutor {
    private PriorityQueue<Task> tasksQueue;
    private final ExecutorService executor;
    private int currentMaxPriority;

    public CustomExecutor() {
        this.tasksQueue = new PriorityQueue<>();
        int numProcessors = getNumProcessors();
//        this.executor = new ThreadPoolExecutor(numProcessors / 2, numProcessors - 1, 300, TimeUnit.MILLISECONDS,
//                new PriorityBlockingQueue<>());
        this.executor = Executors.newFixedThreadPool(getNumProcessors() / 2);
        this.currentMaxPriority = 0;
    }

    public int getNumProcessors() {
        int numProcessors = Runtime.getRuntime().availableProcessors();
        return numProcessors;
    }

    public <V> Future<V> submit(Callable<V> callable, TaskType type) {
        Task<V> task = Task.createTask(callable, type);
        currentMaxPriority = Math.max(currentMaxPriority, task.getType().getPriorityValue());
        tasksQueue.add(task);
        return submit(task);
    }

    public <V> Future<V> submit(Callable<V> callable) {
        Task<V> task = Task.createTask(callable);
        currentMaxPriority = Math.max(currentMaxPriority, task.getType().getPriorityValue());
        tasksQueue.add(task);
        return submit(task);
    }

    public <V> Future<V> submit(Task<V> task) {
        tasksQueue.poll();
        return executor.submit(task);
    }


    public int getCurrentMax() {
        return currentMaxPriority;
    }

    public void gracefullyTerminate() {

    }

}
