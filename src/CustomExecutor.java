import java.util.concurrent.*;


public class CustomExecutor extends ThreadPoolExecutor{
    private static final int MIN_PROCESSORS = getNumProcessors() / 2;
    private static final int MAX_PROCESSORS = getNumProcessors() -1;
    private static PriorityBlockingQueue<Runnable> tasksQueue = new PriorityBlockingQueue<>(MIN_PROCESSORS,(task1,task2) -> ((Task)task1).compareTo((Task) task2));
    private CustomExecutor executor;
    private int currentMaxPriority;


    public CustomExecutor() {
        super(MIN_PROCESSORS,MAX_PROCESSORS, 300, TimeUnit.MILLISECONDS, tasksQueue);
        this.currentMaxPriority = 0;
    }

    public static int getNumProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    public <V> Future<V> submit(Callable<V> callable, TaskType type) {
        Task<V> task = Task.createTask(callable, type);
        currentMaxPriority = Math.max(currentMaxPriority, task.getType().getPriorityValue());
        tasksQueue.add((Runnable) task);
        return submit(task);
    }

    public <V> Future<V> submit(Callable<V> callable) {
        Task<V> task = Task.createTask(callable);
        currentMaxPriority = Math.max(currentMaxPriority, task.getType().getPriorityValue());
        tasksQueue.add((Runnable) task);
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
