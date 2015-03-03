public class ArrayQueueADT {
    private int size;
    private int first;
    private int last;
    private Object[] elements = new Object[10];

    static void enshureCapacity(ArrayQueueADT queue, int capacity) {
        assert queue != null;
        int len = queue.elements.length;
        if (capacity > len) {
            Object[] newElements = new Object[queue.elements.length * 2];
            int i = 0;
            while (queue.last!=queue.first) {
                newElements[i] = queue.elements[queue.first];
                queue.first = (queue.first + 1) % len;
                i++;
            }
            queue.first = 0;
            queue.last = len - 1;
            queue.elements = newElements;
        }
    }

    //pre: element != null,
    //     queue != null
    //post: elements[last] = element,
    //      last = (last + 1) % elements.length,
    //      size = size + 1
    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert queue != null;
        assert element != null;
        enshureCapacity(queue, queue.size + 2);
        queue.elements[queue.last] = element;
        queue.last = (queue.last + 1) % queue.elements.length;
        queue.size++;
        //System.out.println(elements.length);
    }

    //pre: queue != null,
    //     size > 0
    //post: R = elements[first]
    public static Object element(ArrayQueueADT queue) {
        assert queue != null;
        assert queue.size > 0;
        return queue.elements[queue.first];
    }

    //pre: queue != null,
    //     size > 0
    //post: R = elements[first],
    //      first = (first + 1) % elements.length,
    //      size = size - 1
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue != null;
        Object ret = element(queue);
        queue.first = (queue.first + 1) % queue.elements.length;
        queue.size--;
        return ret;
    }

    //pre: queue != null
    //post: R = size
    public static int size(ArrayQueueADT queue) {
        assert queue != null;
        return queue.size;
    }

    //pre: queue != null
    //post: R = (size == 0)
    public static boolean isEmpty(ArrayQueueADT queue) {
        assert queue != null;
        return (queue.size == 0);
    }

    //pre: queue != null
    //post: size = 0,
    //      first = 0,
    //      last = 0
    public static void clear(ArrayQueueADT queue) {
        assert queue != null;
        queue.first = 0;
        queue.last = 0;
        queue.size = 0;
    }

    //pre: queue != null,
    //     element != null
    //post: (first - 1 < 0) ? first = elements.length - 1 : first = first - 1,
    //      elements[first] = element,
    //      size = size + 1;
    public static void push(ArrayQueueADT queue, Object element) {
        assert queue != null;
        assert element != null;
        enshureCapacity(queue, queue.size + 2);
        queue.first = queue.first - 1;
        if (queue.first < 0) {
            queue.first = queue.elements.length - 1;
        }
        queue.elements[queue.first] = element;
        queue.size++;
    }

    //pre: queue != null,
    //     size > 0;
    //post: R = elements[(last - 1 < 0) ? elements.length - 1 : last - 1]
    public static Object peek(ArrayQueueADT queue) {
        assert queue != null;
        assert queue.size > 0;
        int llast = queue.last - 1;
        if (llast < 0) {
            llast = queue.elements.length - 1;
        }
        return queue.elements[llast];
    }

    //pre: queue!= null,
    //     size > 0;
    //post: (last - 1 < 0) ? last = elements.length - 1 : last = last - 1,
    //      R = elements[last]   
    public static Object remove(ArrayQueueADT queue) {
        assert queue != null;
        assert queue.size > 0;
        queue.last = queue.last - 1;
        if (queue.last < 0) {
            queue.last = queue.elements.length - 1;
        }
        queue.size--;
        return queue.elements[queue.last];
    }
}