package mambutu.Handler;

/**
 * Created by stefan on 12/16/15.
 */
public interface Handler {
    /**
     * Should return all the commands handled by the handler, in comma seperated format
     * @return
     */
    public String handles();

    /**
     * Handles that specific command
     * @param command the unformatted command string, with the first word being the .command it was called with
     * @return the output from the command, can be an empty string if no output
     */
    public String handle(String command);

}
