package mambutu.Handler;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by stefan on 12/17/15.
 */
public abstract class Handler {
    /**
     * TODO: Check on startup that all handler methods in the child exists, if not, disable that handler,
     * + ie unregister it in the messagedistributor
     */

    String handles;
    MessageDistributor messageDistributor;

    public Handler(String handles, MessageDistributor messageDistributor) {
        this.handles = handles;
        this.messageDistributor = messageDistributor;
        messageDistributor.registerCommandHandler(this);
    }

    /**
     * TODO: Handle all the exceptions!
     * Using reflection, we call the method with the name of the command being called
     * @Precondition: handles(arg) has been called and returned true for arg
     * @param command the unformatted command string, with the first word being the .command it was called with
     * @return
     */
    public String handle(String command, String args, String nick) {
        java.lang.reflect.Method method = null;
        try {
            method = this.getClass().getMethod(command, String.class, String.class);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            return (String) method.invoke(this, args, nick);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Should call the method with the name "command"
        return "";
    }

    public boolean handles(String command) {
        String[] tempHandles = handles.split(",");
        System.out.println(tempHandles.toString());
        for (int j = 0; j < tempHandles.length; j++) {
            if (tempHandles[j].trim().equals(command))
                return true;
        }
        return false;
    }
}
