import java.util.function.Predicate;
import java.util.function.Function;

public interface Queue {
    //pre: element != null
    //post: size = size + 1,
    //      element inserted at the end of queue
    public void enqueue(Object element);

    //pre: size > 0
    //post: R = first element in queue;
    public Object element();

    //pre: size > 0;
    //post: size = size - 1,
    //      first element of queue deleted
    public Object dequeue();

    //post: R = size
    public int size();

    //post: R = (size == 0)
    public boolean isEmpty();

    //post: size = 0
    //      delete all queue elements
    public void clear();

    //post: R = queue of elements of class instance matching predicate
    public Queue filter(Predicate<Object> predicate);

    //post: R = queue of elements of class instance with func applied to them
    public Queue map(Function<Object, Object> func);
}