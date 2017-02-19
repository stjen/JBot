package mambutu.Plugins;

import mambutu.Config;
import mambutu.Handler.Handler;
import mambutu.Handler.MessageDistributor;
import util.Log.FileLog;
import util.Log.Log;

import java.io.FileNotFoundException;

/**
 * Created by stefan on 12/16/15.
 */
public class BugHandler extends Handler {

    /**
     * Sample handler file
     * For each entry in the variable "handles", there must be a corresponding method, with the signature:
     * public String entry(String args, String nick)
     * -- Note --
     */

    // Static because it is accessed before the handler is instantiated and it must be used for the superconstructor
    private static String handles = "bug";

    public BugHandler(MessageDistributor messageDistributor) {
        super(handles, messageDistributor);
        messageDistributor.registerCommandHandler(this); // This is a COMMAND handler
    }

    public String bug(String args, String nick) {
        if (args.equals("")) { // args is an empty string
            return "The bug log can be found at: " + Config.BUG_LOG_FILE_URL;
        }
        try {
            FileLog.writeToFile(Config.BUG_LOG_FILE_URL, System.currentTimeMillis() + "-" + nick + ": " + args);
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to log file: " + e);
            return "Internal error adding bug";
        }
        return "Added \"" + args + "\" to the bug log";
    }

}
