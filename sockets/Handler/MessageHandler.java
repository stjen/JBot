package sockets.Handler;

import org.omg.CORBA.DynAnyPackage.Invalid;
import sockets.BotThread;
import sockets.Config;
import sockets.Exceptions.InvalidCTCPException;
import sockets.Exceptions.InvalidCommandException;

/**
 * Created by mabool on 11/24/15.
 */
public class MessageHandler {
    BotThread bot;

    public MessageHandler(BotThread bot) {
        this.bot = bot;
    }

    public static String ctcp(String c) throws InvalidCTCPException {
        String preText = c.toUpperCase() + " ";
        switch (c.toLowerCase()) {
            case "version":
                return preText + Config.PROGRAM_NAME + " " + Config.VERSION;
        }
        throw new InvalidCTCPException(c);
    }

    public static String command(String c) throws InvalidCommandException {
        switch (c.toLowerCase()) {
            case "hello":
                return "Rude.";

        }
        throw new InvalidCommandException(c);
    }


}
