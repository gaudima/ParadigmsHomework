public class ArrayQueueADT {
    private int size;
    private int first;
    private int last;
    private Object[] elements = new Object[10];

    static void enshureCapacity(ArrayQueueADT queue, int capacity) {
        int len = queue.elements.length;
        if (capacity > len) {
            Object[] newElements = new Object[queue.elements.length * 2];
            int i = 0;
            while(queue.last!=queue.first) {
                newElements[i] = queue.elements[queue.first];
                queue.first = (queue.first + 1) % len;
                i++;
            }
            queue.first = 0;
            queue.last = len - 1;
            queue.elements = newElements;
        }
    }

    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;
        enshureCapacity(queue, queue.size + 2);
        queue.elements[queue.last] = element;
        queue.last = (queue.last + 1) % queue.elements.length;
        queue.size++;
        //System.out.println(elements.length);
    }

    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[queue.first];
    }

    public static Object dequeue(ArrayQueueADT queue) {
        Object ret = element(queue);
        queue.first = (queue.first + 1) % queue.elements.length;
        queue.size--;
        return ret;
    }

    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    public static boolean isEmpty(ArrayQueueADT queue) {
        return (queue.size == 0);
    }

    public static void clear(ArrayQueueADT queue) {
        queue.first = 0;
        queue.last = 0;
        queue.size = 0;
    }
}