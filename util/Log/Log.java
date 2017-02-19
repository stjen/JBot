package util.Log;

import mambutu.Bot;

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

    public void add(String item, char direction) {
        stack.push(new LogNode(Bot.MSG_INC_CHAR + item));
        System.out.print(Bot.MSG_INC_CHAR + " " + item);
    }

    public StackADT<LogNode> getAll() {
        // if (printout)
        return stack;
        // return null;
    }

}
