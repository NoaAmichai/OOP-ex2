import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class Task<V> extends FutureTask<V> implements Callable<V>, Comparable<Task<V>>{
    private final Callable<V> callable;
    private final TaskType type;
    public boolean isDone;

    private Task(Callable<V> callable, TaskType type) {
        super(callable);
        this.callable = callable;
        this.type = type;
        this.isDone = false;
    }

    private Task(Callable<V> callable) {
        super(callable);
        this.callable = callable;
        this.type = TaskType.OTHER; //default TaskType
        this.isDone = false;
    }

    public static <V> Task<V> createTask(Callable<V> callable, TaskType type) {
        return new Task<>(callable, type);
    }

    public static <V> Task<V> createTask(Callable<V> callable) {
        return new Task<>(callable);
    }

    @Override
    public V call() throws Exception {
        return callable.call();
    }

    public TaskType getType() {
        return type;
    }

    public Callable<V> getCallable() {
        return callable;
    }

    @Override
    public int compareTo(Task<V> task) {
        return Integer.compare(this.type.getPriorityValue(), task.getType().getPriorityValue());
    }
}
