import java.util.Comparator;

public class TaskComparator implements Comparator<Object> {

    /**
     * Compares two TaskWrapper objects based on their priority.
     *
     * @param o1 the first TaskWrapper to be compared
     * @param o2 the second TaskWrapper to be compared
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     * If o1 and o2 are not both instances of TaskWrapper, returns -1
     */
    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof TaskWrapper<?> && o2 instanceof TaskWrapper<?>) {
            return Integer.compare(((TaskWrapper<?>) o2).getPriority(), ((TaskWrapper<?>) o1).getPriority());
        }
        return -1;
    }
}