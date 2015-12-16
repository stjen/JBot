package mambutu.Handler;

/**
 * Created by stefan on 12/16/15.
 */
public abstract class FooHandler implements Handler {

    public final String handles = "foo";

    /**
     * Should return true if the handler can handle that command
     *
     * @param command
     * @return
     */
    public boolean handles(String command) {
        String[] arrayHandles = handles.split(",");
        for (int i = 0; i < arrayHandles.length; i++) {
            if (arrayHandles[i].equals(command))
                return true;
        }
        return false;
    }




}
