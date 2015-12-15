package util.Log;

public class LogTest {
    public static void main(String[] args) {


        Log log = Log.getInstance();
        Log log1 = Log.getInstance();

        log.add("A");
        log.add("B");
        log1.add("C");
        log1.add("D");
        StackADT<LogNode> s = log.getAll();
        System.out.println(s.size());
        int size = s.size();
        for (int i = 0; i < size; i++) {
            System.out.println(s.pop());
        }
    }

}
