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
            while(last!=first) {
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
        //System.out.println(elements.length);
    }

    public Object element() {
        assert size > 0;
        return elements[first];
    }

    public Object dequeue() {
        Object ret = element();
        first = (first + 1) % elements.length;
        size--;
        return ret;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public Object[] elements() {
        return elements;
    }

    public void clear() {
        first = 0;
        last = 0;
        size = 0;
    }
}