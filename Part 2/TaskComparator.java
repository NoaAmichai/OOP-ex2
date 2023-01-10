import java.util.Comparator;
public class TaskComparator implements Comparator<Object> {

    /***
     *This method compares the two objects passed to it and returns an integer value indicating their relative ordering.
     * If both objects are Task objects, it compares their task types based on the priority values of their TaskType enum values.
     *  If either object is not a Task object, it returns -1.
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first object is less than, equal to, or greater than the second object
     */
    @Override
    public int compare(Object o1, Object o2) {
        if(o1 instanceof MyAdapter<?> && o2 instanceof MyAdapter<?>){
            return Integer.compare(((MyAdapter<?>) o1).getPriority(),((MyAdapter<?>) o2).getPriority());}
        return -1;
    }
}