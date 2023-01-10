import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/***
 * MyAdapter task to adapt object to futureTask.
 * @param <T>
 */
public class MyAdapter<T> extends FutureTask<T> {

    private final Task<T> task;

    /***
     * Calls the super for futureTask's Constructor.
     * @param task gets a Task
     */
    public MyAdapter(Task<T> task) {
        super(task);
        this.task = task;
    }

    /***
     * Getter for priority.
     * @return returns priority.
     */
    public int getPriority(){
        return this.task.getType().getPriorityValue();
    }
}
