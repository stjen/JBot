package util.ArrayQueue;

public class CircularArrayQueue<T> implements QueueADT<T> {

    private final static int DEFAULT_CAPACITY = 100;
    private int rear;
    private int front;
    private T[] queue;

    public CircularArrayQueue() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public CircularArrayQueue(int initialCapacity) {
        rear = 0;
        front = 0;
        queue = (T[]) new Object[initialCapacity];
    }

    public void enqueue(T element) throws IllegalArgumentException {
        if (element == null) {
            throw new IllegalArgumentException();
        }
        if (rear == queue.length) {
            expandCapacity();
        }
        // System.out.println("deb");
        queue[rear] = element;
        rear++;
    }

    @SuppressWarnings("unchecked")
    private void expandCapacity() {
        T[] temp = (T[]) new Object[queue.length * 2];
        for (int i = 0; i < queue.length; i++) {
            temp[i] = queue[i];
        }
        queue = temp;
    }

    public T dequeue() {
        //  T temp = queue[rear];
        //   queue[rear] = null;
        front++;
        return queue[front - 1];
    }

    public T first() {
        return queue[front];
    }

    public T last() {
        return queue[rear];
    }

    public int indexOf(T element) {
        for (int i = 0; i < queue.length; i++) {
            if (queue[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return rear == 0;
    }

    public int size() {
        return rear;
    }

    public String toString() {
        String msg = "{";
        for (int i = front; i < rear; i++) {
            msg += queue[i];
            if (i < rear - 1) {
                msg += ",";
            }
        }
        return msg + "}";
    }
}
