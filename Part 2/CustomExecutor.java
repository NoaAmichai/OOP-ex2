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
     * the function gets a task ,makes sure it's not null(otherwise throws null pointer exception).Our function then
     * increases our Array (that holds what priorities we have in the queue) by 1 to the location of the specific
     * priority ,next it updates max priority based on the new array. After that, it uses MyAdapter to adapt the task
     * to a future(MyAdapter is of type future) and executes the adapter task and returns the MyAdapter object.
     * (that is possible because it is of type future)
     * @param task a generic task
     * @return returns MyAdapter (future) after it is sent to the pool
     * @param <V> the return is generic, so we can use all types.
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
            MyAdapter<V> taskAdapter=new MyAdapter<>(task);
            super.execute(taskAdapter);
            return taskAdapter;
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
     * This method is called before a Thread in the ThreadPoolExecutor begins execution of a Runnable task.
     * If the given Runnable task is an instance of MyAdapter, it decreases the value at the index of the
     * priority of the Task in the maxArray AtomicIntegerArray. Then, it iterates through the maxArray
     * and looks for the first index with a non-zero value, and sets the currentMaxPriority to that index.
     * Finally, it calls the superclass's beforeExecute() method.
     *
     * @param t the Thread that will run the task
     * @param r the Runnable task that the Thread will execute
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        if (r instanceof FutureTask<?>) {
            maxArray.getAndDecrement(((MyAdapter<?>) r).getPriority());
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
     * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted.
     */
    public void gracefullyTerminate() {
        super.shutdown();
    }

}
