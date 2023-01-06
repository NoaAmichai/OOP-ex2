import java.util.concurrent.Callable;

public class Task<T> implements Callable<T>, Comparable<Task<T>> {
    private Callable<T> callable;
    private TaskType type;

    private Task(Callable<T> callable, TaskType type) {
        this.callable = callable;
        this.type = type;
    }

    private Task(Callable<T> callable) {
        this.callable = callable;
        this.type = TaskType.OTHER; //default TaskType
    }

    public static final <T> Task<T> createTask(Callable<T> callable, TaskType type) {
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
    public int compareTo(Task<T> other) {
        return this.type.getPriorityValue() - other.getType().getPriorityValue();
    }


}
