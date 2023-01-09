import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/***
 * customExecutor that extends ThreadPoolExecutor.it's parameters are the 1,2)min and max amount of processors that our
 * pool will use,3)our priority blocking queue(its safe for threads and also organized based on priority,4) A parameter
 * that holds our max priority and 5)Finally our Array that helps us know our priorities that are in the queue.
 */
public class CustomExecutor extends ThreadPoolExecutor {
    private static final int MIN_PROCESSORS = getNumProcessors() / 2;
    private static final int MAX_PROCESSORS = getNumProcessors() - 1;
    private static final PriorityBlockingQueue<Runnable> tasksQueue = new PriorityBlockingQueue<>(MIN_PROCESSORS,new TaskComparator());
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
     * the function gets a task ,makes sure it's not null(otherwise throws null pointer exception).Our function then
     * increases our Array (that holds what priorities we have in the queue) by 1 to the location of the specific
     * priority ,next it updates max priority based on the new array. Finally, it executes tasks Task and returns it
     * (that is possible because it is of type future)
     * @param task a generic task
     * @return returns task (future) after it is sent to the pool
     * @param <V> the return is generic, so we can use all types.
     */
    public <V> Future<V> submit(Task<V> task) {
        if(task != null) {
            maxArray.getAndIncrement(task.getType().getPriorityValue());
            for (int i = 1; i < maxArray.length(); i++) {
                if (maxArray.get(i) != 0) {
                    currentMaxPriority.set(i);
                    break;
                }
            }
            super.execute(task);
            return task;
        }
        else throw new NullPointerException();
    }

    /***
     * function to get current max priority
     * @return returns max priority
     */
    public AtomicInteger getCurrentMax() {
        return currentMaxPriority;
    }

    /***
     * Our function Overrides the afterExecute of ThreadPoolExecutor,first decreases the counter in our priority array
     * for this task, then updates max priority (if queue is empty it will leave max priority as the previous one).
     * then the function uses ThreadPoolExecutors afterExecute.
     * @param r the runnable that has completed
     * @param t the exception that caused termination, or null if
     * execution completed normally
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if(t==null) {
            if (r instanceof FutureTask<?>) {
                maxArray.getAndDecrement(((Task<?>) r).getType().getPriorityValue());
                for (int i = 1; i < maxArray.length(); i++) {
                    if (maxArray.get(i) != 0) {
                        currentMaxPriority.set(i);
                        break;
                    }
                }
            }
        }
        super.afterExecute(r, t);
    }

    /***
     * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted.
     */
    public void gracefullyTerminate() {
        super.shutdown();
    }

}
