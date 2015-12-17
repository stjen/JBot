package Tests;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by stefan on 12/17/15.
 */
public class ReflectionTest {
    public static void main(String[] args) {
        ReflectionTest r = new ReflectionTest();
        r.run();
    }

    public void run() {
        java.lang.reflect.Method method = null;
        try {
            method = this.getClass().getMethod("foo", String.class);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            method.invoke(this, "hello");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void foo(String arg) {
        System.out.println("We are in the method! " + arg);
    }
}
