import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class CustomExecutor extends ThreadPoolExecutor {
    private static final int MIN_PROCESSORS = getNumProcessors() / 2;
    private static final int MAX_PROCESSORS = getNumProcessors() - 1;
    private static final PriorityBlockingQueue<Runnable> tasksQueue = new PriorityBlockingQueue<>(MIN_PROCESSORS,new TaskComparator());
    private final AtomicInteger currentMaxPriority;
    private final AtomicIntegerArray maxArray;


    public CustomExecutor() {
        super(MIN_PROCESSORS,
                MAX_PROCESSORS,
                300,
                TimeUnit.MILLISECONDS,
                tasksQueue);
        this.maxArray = new AtomicIntegerArray(11);
        this.currentMaxPriority = new AtomicInteger(Integer.MAX_VALUE);
    }

    public static int getNumProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    public <V> Future<V> submit(Callable<V> callable, TaskType type) {
        Task<V> task = Task.createTask(callable, type);
        return submit(task);
    }

    public <V> Future<V> submit(Callable<V> callable) {
        Task<V> task = Task.createTask(callable);
        return submit(task);
    }

    public <V> Future<V> submit(Task<V> task) {
        if(task != null) {
            maxArray.getAndIncrement(task.getType().getPriorityValue());
            for (int i = 1; i < maxArray.length(); i++) {
                if (maxArray.get(i) != 0) {
                    currentMaxPriority.getAndIncrement();
                    //System.out.println(maxArray);
                    break;
                }
            }
            super.execute(task);
            return task;
        }
        else throw new NullPointerException();
    }

    public AtomicInteger getCurrentMax() {
        return currentMaxPriority;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if (r instanceof FutureTask<?>) {
//            System.out.println(((FutureTask<?>) r).isDone());
            maxArray.getAndDecrement(((Task<?>) r).getType().getPriorityValue());
        }
        for (int i = 1; i < maxArray.length(); i++) {
            if (maxArray.get(i) != 0) {
                currentMaxPriority.set(i);
                break;
            }
        }
        super.afterExecute(r, t);
    }

//    @Override
//    protected void beforeExecute(Thread t, Runnable r) {
//        if (r instanceof Task<?>) {
//            ((Task<?>) r).isDone = true;
//        }
//        super.beforeExecute(t, r);
//    }

    public void gracefullyTerminate() {
        super.shutdown();
    }

}
