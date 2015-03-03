public class Test {
	public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            ArrayQueueModule.enqueue(i);

            for(Object j: ArrayQueueModule.elements()) {
                System.out.print(j);
                System.out.print(" ");
            }
            System.out.println(" ");
/*            if (i % 2 ==0) {
                System.out.println(ArrayQueueModule.dequeue());
            }*/
        }
        for (int i = 0; i < 50; i++) {
            System.out.println(ArrayQueueModule.dequeue());
        }
	}
}