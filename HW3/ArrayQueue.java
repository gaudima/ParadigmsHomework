public class ArrayQueue {
    private int size;
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

    //pre: element != null
    //post: elements[last] = element,
    //      last = (last + 1) % elements.length,
    //      size = size + 1 
    public void enqueue(Object element) {
        assert element != null;
        enshureCapacity(size + 2);
        elements[last] = element;
        last = (last + 1) % elements.length;
        size++;
        //System.out.println(elements.length);
    }

    
    //pre: size > 0
    //post: R = elements[first]
    public Object element() {
        assert size > 0;
        return elements[first];
    }

    //pre: size > 0
    //post: R = elements[first],
    //      first = (first + 1) % elements.length,
    //      size = size - 1 
    public Object dequeue() {
        Object ret = element();
        elements[first] = null;
        first = (first + 1) % elements.length;
        size--;
        return ret;
    }

    //post: R = size
    public int size() {
        return size;
    }

    //post: R = (size == 0)
    public boolean isEmpty() {
        return (size == 0);
    }

    //post: size = 0,
    //      first = 0,
    //      last = 0,
    public void clear() {
        first = 0;
        last = 0;
        size = 0;
    }

    //pre: element != null
    //post: (first - 1 < 0) ? first = elements.length - 1 : first = first - 1,
    //      elements[first] = element,
    //      size = size + 1;
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

    //pre: size > 0;
    //post: R = elements[(last - 1 < 0) ? elements.length - 1 : last - 1]
    public Object peek() {
        assert size > 0;
        int llast = last - 1;
        if (llast < 0) {
            llast = elements.length - 1;
        }
        return elements[llast];
    }

    //pre: size > 0;
    //post: (last - 1 < 0) ? last = elements.length - 1 : last = last - 1,
    //      R = elements[last]   
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
}