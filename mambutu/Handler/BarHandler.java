package mambutu.Handler;

/**
 * Created by stefan on 12/16/15.
 */
public class BarHandler extends Handler {

    /**
     * Sample handler file
     * For each entry in the variable "handles", there must be a corresponding method, with the signature:
     * public String entry(String args, String nick)
     * -- Note --
     */

    // Static because it is accessed before the handler is instanciated and it must be used for the superconstructor
    private static String handles = "bar, baz, foo";

    public BarHandler(MessageDistributor messageDistributor) {
        super(handles, messageDistributor);
        messageDistributor.registerCommandHandler(this); // This is a COMMAND handler
    }

    public String baz(String args, String nick) {
        System.out.println("In baz!");
        return "baz: nick: " + nick + " args: " + args;
    }

    public String foo(String args, String nick) {
        return "foo: nick: " + nick + " args: " + args;
    }

    public String bar(String args, String nick) {
        return "bar: nick: " + nick + " args: " + args;
    }

}
