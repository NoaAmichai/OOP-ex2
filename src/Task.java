import java.util.Comparator;
import java.util.concurrent.Callable;

public class Task<T> implements Callable<T>, Comparator<Task<T>> {
    private final Callable<T> callable;
    private final TaskType type;

    private Task(Callable<T> callable, TaskType type) {
        this.callable = callable;
        this.type = type;
    }

    private Task(Callable<T> callable) {
        this.callable = callable;
        this.type = TaskType.OTHER; //default TaskType
    }

    public static <T> Task<T> createTask(Callable<T> callable, TaskType type) {
        return new Task<>(callable, type);
    }

    public static <T> Task<T> createTask(Callable<T> callable) {
        return new Task<>(callable);
    }

    @Override
    public T call() throws Exception {
        return callable.call();
    }

    public TaskType getType() {
        return type;
    }


    @Override
    public int compare(Task<T> o1, Task<T> o2) {
        return Integer.compare(o1.getType().getPriorityValue(), o2.getType().getPriorityValue());
    }
}
