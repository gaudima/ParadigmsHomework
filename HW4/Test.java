public class Test {
    public static void main(String[] args) {
        LinkedQueue queue = new LinkedQueue();
        queue.enqueue("Hello");
        queue.push("World");
        System.out.println(queue.remove());
        System.out.println(queue.remove());
    }
}