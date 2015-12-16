package mambutu.Handler;

/**
 * Created by stefan on 12/16/15.
 */
public interface Handler {
    /**
     * Should return true if the handler can handle that command
     * @param command
     * @return
     */
    public boolean handles(String command);

    /**
     * Handles that specific command
     * @param command
     * @return the output from the command, can be an empty string if no output
     */
    public String handle(String command);

}
