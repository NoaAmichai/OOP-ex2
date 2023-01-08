import java.util.Comparator;
import java.util.concurrent.Callable;

public class Task<V> implements Callable<V> ,Comparable<Task<V>>, Runnable{
    private final Callable<V> callable;
    private final TaskType type;

    private Task(Callable<V> callable, TaskType type) {
        this.callable = callable;
        this.type = type;
    }

    private Task(Callable<V> callable) {
        this.callable = callable;
        this.type = TaskType.OTHER; //default TaskType
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
    public int compareTo(Task other) {
        return Integer.compare(this.type.getPriorityValue(), other.getType().getPriorityValue());
    }

    @Override
    public void run() {
        try {
            callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
