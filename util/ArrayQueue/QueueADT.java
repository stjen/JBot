package util.ArrayQueue;

public interface QueueADT<T> {
    public T dequeue();

    public void enqueue(T element);

    public T first();

    public T last();

    public int indexOf(T element);

    public boolean isEmpty();

    public int size();

}
