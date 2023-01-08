import java.util.Comparator;
import java.util.concurrent.*;


public class CustomExecutor{// extends ThreadPoolExecutor {
    private static final int MIN_PROCESSORS = getNumProcessors() / 2;
    private static final int MAX_PROCESSORS = getNumProcessors() - 1;
    private static final PriorityBlockingQueue<Runnable> tasksQueue = new PriorityBlockingQueue<>(MIN_PROCESSORS, Comparator.comparingInt(task -> ((Task) task).getType().getPriorityValue()));
    private int currentMaxPriority;
    private final ThreadPoolExecutor threadPoolExecutor;


    public CustomExecutor() {
        threadPoolExecutor = new ThreadPoolExecutor(MIN_PROCESSORS, MAX_PROCESSORS, 300, TimeUnit.MILLISECONDS, tasksQueue);
        //super(MIN_PROCESSORS, MAX_PROCESSORS, 300, TimeUnit.MILLISECONDS, tasksQueue);

        //this.currentMaxPriority = Integer.MIN_VALUE;
    }

    public static int getNumProcessors() {
        return Runtime.getRuntime().availableProcessors();
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

//    @Override
//    protected void afterExecute(Runnable r, Throwable t) {
//        super.afterExecute(r, t);
//        if (tasksQueue.peek() != null) {
//            this.currentMaxPriority = ((Task<?>) tasksQueue.peek()).getType().getPriorityValue();
//        } else {
//            this.currentMaxPriority = Integer.MAX_VALUE;
//        }
//    }

    public <V> Future<V> submit(Task<V> task) {
        tasksQueue.poll();
        return threadPoolExecutor.submit(task.getCallable());
    }

    public int getCurrentMax() {
        return currentMaxPriority;
    }

    public void gracefullyTerminate() {
        threadPoolExecutor.shutdown();
    }

}
