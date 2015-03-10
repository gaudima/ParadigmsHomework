import java.util.function.Predicate;
import java.util.function.Function;

public class LinkedQueue extends AbstractQueue implements Queue {
    private ListNode head;
    private ListNode tail;

    public void enqueue(Object element) {
        assert element != null;
        head = new ListNode(element, null, head);
        if(head.next != null) {
            head.next.prev = head;
        }
        if (size == 0) {
            tail = head;
        }
        size++;
    }

    public Object element() {
        assert size > 0;
        return tail.value;
    }
 
    public Object dequeue() {
        Object ret = element();
        if (tail.prev != null) {
            tail = tail.prev;
            tail.next = null;
        } else {
            tail = null;
        }
        size--;
        return ret;
    }

    public void clear() {
        ListNode node = head;
        while(node != null) {
            ListNode newNode = node.next;
            node = null;
            node = newNode;
        }
        size = 0;
    }

    public void push(Object element) {
        assert element != null;
        tail = new ListNode(element, tail, null);
        if(tail.prev != null) {
            tail.prev.next = tail;
        }
        if (size == 0) {
            head = tail;
        }
        size++;
    }

    public Object peek() {
        assert size > 0;
        return head.value;
    }

    public Object remove() {
        assert size > 0;
        Object ret = peek();
        if (head.next != null) {
            head = head.next;
            head.prev = null;
        } else {
            head = null;
        }
        size--;
        return ret;
    }

    public LinkedQueue filter(Predicate<Object> predicate) {
        LinkedQueue ret = new LinkedQueue();
        ListNode node = tail;
        while(node != null) {
            if (predicate.test(node.value)) {
                ret.enqueue(node.value);
            }
            node = node.prev;
        }
        return ret;
    }

    public LinkedQueue map(Function<Object, Object> func) {
        LinkedQueue ret = new LinkedQueue();
        ListNode node = tail;
        while(node != null) {
            ret.enqueue(func.apply(node.value));
            node = node.prev;
        } 
        return ret;
    }
}