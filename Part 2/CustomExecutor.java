import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/***
 * customExecutor that extends ThreadPoolExecutor.it's parameters are:
 * 1,2) min and max amount of processors that our pool will use.
 * 3) our priority blocking queue (its safe for threads and also organized based on priority).
 * 4) A parameter that holds our max priority. 
 * 5) Array that helps us know our priorities that are in the queue.
 */
public class CustomExecutor extends ThreadPoolExecutor {
    private static final int MIN_PROCESSORS = getNumProcessors() / 2;
    private static final int MAX_PROCESSORS = getNumProcessors() - 1;
    private static final PriorityBlockingQueue<Runnable> tasksQueue = new PriorityBlockingQueue<>(11, new TaskComparator());
    private final AtomicInteger currentMaxPriority;
    private final AtomicIntegerArray maxArray;

    /***
     * constructor
     * uses ThreadPoolExecutors constructor with our params and initializes our array and max priority.
     */
    public CustomExecutor() {
        super(MIN_PROCESSORS,
                MAX_PROCESSORS,
                300,
                TimeUnit.MILLISECONDS,
                tasksQueue);
        this.maxArray = new AtomicIntegerArray(11);
        this.currentMaxPriority = new AtomicInteger(Integer.MAX_VALUE);
    }


    /***
     * calculates our available processors
     * @return number of available processors
     */
    public static int getNumProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    /***
     * gets a callable and taskType ,turns them into a task and returns the Submit value of the function submit(task)
     * @param callable a callable function.
     * @param type a taskType that lets us know the priority.
     * @return returns the Submit value of the function submit(task)
     * @param <V> the return is generic so we can use all types
     */
    public <V> Future<V> submit(Callable<V> callable, TaskType type) {
        Task<V> task = Task.createTask(callable, type);
        return submit(task);
    }

    /***
     * gets a callable ,turns it into a task with a default taskType and returns the Submit value of the
     * function submit(task)
     * @param callable a callable function.
     * @return returns the Submit value of the function submit(task)
     * @param <V> the return is generic,so we can use all types
     */
    public <V> Future<V> submit(Callable<V> callable) {
        Task<V> task = Task.createTask(callable);
        return submit(task);
    }

    /***
     * Submits a task of type Task<V> to the thread pool executor.
     * It increments the value of the element at the index equal to the priority of the task in the maxArray AtomicIntegerArray,
     * then iterates through the maxArray to find the current maximum priority. It then sets the value of currentMaxPriority to this value.
     * It creates a new TaskWrapper<V> object, which wraps the task's callable and the task's priority.
     * Then it calls the execute method of the parent class, ThreadPoolExecutor, to submit the task to the thread pool.
     * Finally, it returns the Future object associated with the TaskWrapper<V> object.
     *
     * @param task the task to be submitted
     * @return a Future representing pending completion of the task
     * @throws NullPointerException if task is null
     */
    public <V> Future<V> submit(Task<V> task) {
        if (task != null) {
            maxArray.getAndIncrement(task.getType().getPriorityValue());
            for (int i = 1; i < maxArray.length(); i++) {
                if (maxArray.get(i) != 0) {
                    currentMaxPriority.set(i);
                    break;
                }
            }
            TaskWrapper<V> taskWrapper = new TaskWrapper<>(task.getCallable(), task.getType().getPriorityValue());
            super.execute(taskWrapper);
            return taskWrapper;
        } else throw new NullPointerException();
    }

    /***
     * function to get current max priority
     * @return returns max priority
     */
    public AtomicInteger getCurrentMax() {
        return currentMaxPriority;
    }


    /***
     * This method is called before a task is executed by the thread pool.
     * If the task is a FutureTask, it decrements the value of the element at the index equal to the priority of the task in the maxArray AtomicIntegerArray,
     * and then iterates through the maxArray to find the current maximum priority. It then sets the value of currentMaxPriority AtomicInteger to this value.
     *
     * @param t the thread that will run task r
     * @param r the task that will be executed
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        if (r instanceof FutureTask<?>) {
            maxArray.getAndDecrement(((TaskWrapper<?>) r).getPriority());
            for (int i = 1; i < maxArray.length(); i++) {
                if (maxArray.get(i) != 0) {
                    currentMaxPriority.set(i);
                    break;
                }
            }
        }
        super.beforeExecute(t, r);
    }

    /***
     * Terminates this executor in a graceful way.
     * It calls the shutdown method of the parent class, ThreadPoolExecutor, and waits for termination to complete.
     * If the termination is interrupted, a runtime exception is thrown.
     */
    public void gracefullyTerminate() {
        try {
            super.shutdown();
            super.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
