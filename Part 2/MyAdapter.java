import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MyAdapter<T> extends FutureTask<T> {
    private int priority;
    private Callable<T> callable;

    public MyAdapter(Task<T> task) {
        super(task.getCallable());
        this.callable=task.getCallable();
        this.priority=task.getType().getPriorityValue();
    }

    public int getPriority(){
        return this.priority;
    }
}
