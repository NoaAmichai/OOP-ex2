public class Task {
    private TaskType type;

    public static void createTask(Object o, TaskType computational) {
    }


    public TaskType getType() {
        return type;
    }

    public int getPriorityValue() {
        return type.getPriorityValue();
    }


}
