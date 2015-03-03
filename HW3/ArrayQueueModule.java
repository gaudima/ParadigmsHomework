public class ArrayQueueModule {
    static int size;
    static int first;
    static int last;
    static Object[] elements = new Object[10];

    static void enshureCapacity(int capacity) {
        int len = elements.length;
        if (capacity > len) {
            Object[] newElements = new Object[elements.length * 2];
            //newElements[0] = elements[0];
            for (int i = 0; i < elements.length; i++) {
                newElements[elements.length + i] = elements[i];
            }
            elements = newElements;
            first = first + len;
            last = 0;
            System.out.println(first);
        }
    }

    public static void enqueue(Object element) {
        assert element != null;
        enshureCapacity(size + 1);
        elements[last] = element;
        last = (last + 1) % elements.length;
        size++;
        //System.out.println(elements.length);
    }

    public static Object element() {
        assert size > 0;
        return elements[first];
    }

    public static Object dequeue() {
        Object ret = element();
        first = (first + 1) % elements.length;
        size--;
        return ret;
    }

    public static int size() {
        return size;
    }

    public static boolean isEmpty() {
        return (size == 0);
    }

    public static Object[] elements() {
        return elements;
    }

    public static void clear() {
        first = 0;
        last = 0;
        size = 0;
    }
}