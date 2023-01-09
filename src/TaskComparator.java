import java.util.Comparator;
public class TaskComparator implements Comparator<Object> {

    @Override
    public int compare(Object o1, Object o2) {
        if(o1 instanceof Task<?> && o2 instanceof Task<?>){
            return Integer.compare(((Task<?>) o1).getType().getPriorityValue(),((Task<?>) o2).getType().getPriorityValue());}
        return -1;
    }
}