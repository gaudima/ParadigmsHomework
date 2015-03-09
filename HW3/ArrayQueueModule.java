public class ArrayQueueModule {
    private static int size;
    private static int first;
    private static int last;
    private static Object[] elements = new Object[10];

    static void enshureCapacity(int capacity) {
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
    public static void enqueue(Object element) {
        assert element != null;
        enshureCapacity(size + 2);
        elements[last] = element;
        last = (last + 1) % elements.length;
        size++;
        //System.out.println(elements.length);
    }

    //pre: size > 0
    //post: R = elements[first]
    public static Object element() {
        assert size > 0;
        return elements[first];
    }

    //pre: size > 0
    //post: R = elements[first],
    //      first = (first + 1) % elements.length,
    //      size = size - 1 
    public static Object dequeue() {
        Object ret = element();
        elements[first] = null;
        first = (first + 1) % elements.length;
        size--;
        return ret;
    }

    //post: R = size
    public static int size() {
        return size;
    }

    //post: R = (size == 0)
    public static boolean isEmpty() {
        return (size == 0);
    }

    //post: size = 0,
    //      first = 0,
    //      last = 0
    public static void clear() {
        first = 0;
        last = 0;
        size = 0;
    }

    //pre: element != null
    //post: (first - 1 < 0) ? first = elements.length - 1 : first = first - 1,
    //      elements[first] = element,
    //      size = size + 1;
    public static void push(Object element) {
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
    public static Object peek() {
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
    public static Object remove() {
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