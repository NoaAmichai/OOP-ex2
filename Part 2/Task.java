import java.util.concurrent.Callable;

public class Task<V> implements Callable<V>, Comparable<Task<V>> {
    private final Callable<V> callable;
    private final TaskType type;

    /***
     *The constructor gets callable and TaskType stores them in the corresponding fields.
     * @param callable  the Callable object to be wrapped by the Task object
     * @param type  the task type of the Task object
     */
    private Task(Callable<V> callable, TaskType type) {
        this.callable = callable;
        this.type = type;
    }

    /***
     *This constructor gets a callable parameter and sets the task type to the default value
     *TaskType.OTHER.
     * @param callable the Callable object to be wrapped by the Task object
     */
    private Task(Callable<V> callable) {
        this.callable = callable;
        this.type = TaskType.OTHER; //default TaskType
    }

    /***
     *This static factory method creates a new Task object and passes the specified Callable object and task type to its constructor.
     * @param callable the Callable object to be wrapped by the Task object
     * @param type  the task type of the Task object
     * @return a new Task object with the specified Callable object and task type
     */
    public static <V> Task<V> createTask(Callable<V> callable, TaskType type) {
        return new Task<>(callable, type);
    }

    /***
     *This static factory method creates a new Task object and passes the specified Callable object to its constructor.
     * @param callable the Callable object to be wrapped by the Task object
     * @return a new Task object with the specified Callable object
     */
    public static <V> Task<V> createTask(Callable<V> callable) {
        return new Task<>(callable);
    }

    /***
     *This method overrides the call method of the Callable interface and simply calls the call method of the Callable object.
     * @return the result of the Callable object's call method
     * @throws Exception if the Callable object's call method throws an exception
     */
    @Override
    public V call() throws Exception {
        return callable.call();
    }

    /***
     * @return type of the task
     */
    public TaskType getType() {
        return type;
    }

    /***
     * This method is implemented by comparing the priority values of the tasks.
     * @param task the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Task<V> task) {
        return Integer.compare(this.type.getPriorityValue(), task.getType().getPriorityValue());
    }

    /***
     * getter for callable
     * @return callable
     */
    public Callable<V> getCallable() {
        return this.callable;
    }
}
