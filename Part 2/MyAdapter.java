import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/***
 * MyAdapter task to adapt object to future.
 * It contains priority and a callable function
 * @param <T>
 */
public class MyAdapter<T> extends FutureTask<T> {
    private int priority;
    private Callable<T> callable;

    /***
     * constructor that gets a task and initialises priority and callable
     * and calls the super for futureTask's Constructor.
     * @param task
     */
    public MyAdapter(Task<T> task) {
        super(task.getCallable());
        this.callable=task.getCallable();
        this.priority=task.getType().getPriorityValue();
    }

    /***
     * Getter for priority.
     * @return returns priority.
     */
    public int getPriority(){
        return this.priority;
    }
}
