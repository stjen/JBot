package util.ArrayQueue;

import mambutu.Exceptions.EmptyCollectionException;

public interface QueueADT<T> {
    public T dequeue() throws EmptyCollectionException;

    public void enqueue(T element);

    public T first() throws Exception;

    public T last();

    public boolean isEmpty();

    public int size();

}
