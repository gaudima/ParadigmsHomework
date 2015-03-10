public class AbstractQueue {
	protected int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size == 0);
    }
}