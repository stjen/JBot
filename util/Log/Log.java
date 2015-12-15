package util.Log;

public class Log {
    private StackADT<LogNode> stack = new ArrayStack<>();
    private static Log instance;
    private boolean printout;

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log(true);
        }
        return instance;
    }

    private Log(boolean printout) {
        this.printout = printout;
    }

    public void add(String item) {
        stack.push(new LogNode(item));
    }

    public StackADT<LogNode> getAll() {
        // if (printout)
        return stack;
        // return null;
    }

}
