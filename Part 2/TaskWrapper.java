import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * A wrapper class for tasks that are submitted to the thread pool executor.
 */

public class TaskWrapper<T> extends FutureTask<T> {
    private final int priority;

    /**
     * Constructor which initializes the callable and priority fields
     *
     * @param callable the callable task
     * @param priority the priority of the task
     */
    public TaskWrapper(Callable<T> callable, int priority) {
        super(callable);
        this.priority = priority;
    }

    /**
     * Constructor which initializes the runnable, result and priority fields
     *
     * @param runnable the runnable task
     * @param result   the result of the task
     * @param priority the priority of the task
     */
    public TaskWrapper(Runnable runnable, T result, int priority) {
        super(runnable, result);
        this.priority = priority;
    }


    /**
     * @return the priority of the task
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @return a string representation of the class
     */
    @Override
    public String toString() {
        return "TaskWrapper {" +
                "priority=" + priority +
                '}';
    }

}
