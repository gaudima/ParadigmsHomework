import java.util.function.Predicate;
import java.util.function.Function;

public interface Queue {
    
    public void enqueue(Object element);

    public Object element();

    public Object dequeue();

    public int size();

    public boolean isEmpty();

    public void clear();

    public Queue filter(Predicate<Object> predicate);

    public Queue map(Function<Object, Object> func);
}