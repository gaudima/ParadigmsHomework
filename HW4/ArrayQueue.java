import java.util.function.Predicate;
import java.util.function.Function;

public class ArrayQueue extends AbstractQueue implements Queue {
    private int first;
    private int last;
    private Object[] elements = new Object[10];

    private void enshureCapacity(int capacity) {
        int len = elements.length;
        if (capacity > len) {
            Object[] newElements = new Object[elements.length * 2];
            int i = 0;
            while (last!=first) {
                newElements[i] = elements[first];
                first = (first + 1) % len;
                i++;
            }
            first = 0;
            last = len - 1;
            elements = newElements;
        }
    }

    public void enqueue(Object element) {
        assert element != null;
        enshureCapacity(size + 2);
        elements[last] = element;
        last = (last + 1) % elements.length;
        size++;
    }

    public Object element() {
        assert size > 0;
        return elements[first];
    }

    public Object dequeue() {
        Object ret = element();
        elements[first] = null;
        first = (first + 1) % elements.length;
        size--;
        return ret;
    }

    public void clear() {
        first = 0;
        last = 0;
        size = 0;
    }

    public void push(Object element) {
        assert element != null;
        enshureCapacity(size + 2);
        first = first - 1;
        if (first < 0) {
            first = elements.length - 1;
        }
        elements[first] = element;
        size++;
    }

    public Object peek() {
        assert size > 0;
        int llast = last - 1;
        if (llast < 0) {
            llast = elements.length - 1;
        }
        return elements[llast];
    }
   
    public Object remove() {
        assert size > 0;
        last = last - 1;
        if (last < 0) {
            last = elements.length - 1;
        }
        Object ret = elements[last];
        elements[last] = null;
        size--;
        return ret;
    }

    public ArrayQueue filter(Predicate<Object> predicate) {
        ArrayQueue ret = new ArrayQueue();
        int index = first;
        while (last != index) {
            if (predicate.test(elements[index])) {
                ret.enqueue(elements[index]);
            }
            index = (index + 1) % elements.length;
        }
        return ret;
    }

    public ArrayQueue map(Function<Object, Object> func) {
        ArrayQueue ret = new ArrayQueue();
        int index = first;
        while (last != index) {
            ret.enqueue(func.apply(elements[index]));
            index = (index + 1) % elements.length;
        }
        return ret;
    }
}