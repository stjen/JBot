package util.Log;

public class ArrayStack<T> implements StackADT<T> {

    private static final int DEFAULT_CAPACITY = 100;
    private int top;                   // next index to use
    private T[] stack;

    public ArrayStack() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ArrayStack(int initialCapacity) {
        stack = (T[]) new Object[initialCapacity];
    }

    public String toString() {
        String returnStr = "{";
        for (int i = 0; i < top; i++) {
            returnStr += stack[top - 1 - i];
            if (i < top - 1) {
                returnStr += ", ";
            }
        }
        return returnStr.trim() + "}";

    }

    public void push(T element) {
        if (element == null) {
            throw new IllegalArgumentException();
        }
        if (top == stack.length) {
            expandCapacity();
            stack[top] = element;
            top++;
        } else {
            stack[top] = element;
            top++;

        }
    }

    @SuppressWarnings("unchecked")
    private void expandCapacity() {
        T[] temp = (T[]) new Object[stack.length * 2];
        for (int i = 0; i < stack.length; i++) {
            temp[i] = stack[i];
        }
        stack = temp;
    }

    public T pop() {
        if (top == 0) {
            throw new IllegalStateException();
        }
        top--;
        return stack[top];
    }

    public T peek() {
        if (top == 0) {
            throw new IllegalStateException();
        }
        return stack[top - 1];

    }

    public int indexOf(T element) {
        for (int i = 0; i < stack.length; i++) {
            if (stack[i] == element) {
                return top - 1 - i;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public int size() {
        return top;
    }

}
