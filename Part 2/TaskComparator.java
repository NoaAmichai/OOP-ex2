import java.util.Comparator;
public class TaskComparator implements Comparator<Object> {

    /***
     *This method compares the two objects passed to it and returns an integer value indicating their relative ordering.
     * If both objects are TaskAdapter objects, it compares the taskAdapter types based on the priority value
     *  If either object is not a TaskAdapter object, it returns -1.
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first object is less than, equal to, or greater than the second object
     */
    @Override
    public int compare(Object o1, Object o2) {
        if(o1 instanceof MyAdapter<?> && o2 instanceof MyAdapter<?>){
            return Integer.compare(((MyAdapter<?>) o2).getPriority(),((MyAdapter<?>) o1).getPriority());}
        return -1;
    }
}