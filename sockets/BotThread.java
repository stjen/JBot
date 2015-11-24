package sockets;

import sockets.Exceptions.InvalidCTCPException;
import sockets.Exceptions.InvalidCommandException;
import sockets.Handler.MessageHandler;

/**
 * Created by mabool on 11/24/15.
 */
public class BotThread extends Thread {

    /**
     * TODO:
     * - Keep track of own nickname (might be force-changed)
     */

    public static final char CTCP_CHAR = '\u0001';

    private Controller con;

    public BotThread(Controller con) {
        this.con = con;
    }

    private String serverName = "";


    /**
     * Takes care of initial connection details, such as setting nick and identifying USER
     */
    public void initConnection() {
        String outMsg = "NICK " + Config.BOT_NICK + "\n";
        outMsg += "USER " + Config.BOT_USERNAME + " 8 * : " + Config.BOT_REALNAME;
        con.sendMessage(outMsg + "\n");
    }

    public void handleMessage(String what) {
        what = what.trim();
        String[] incMsg = what.split("\\s+"); // Split by empty space
        String code = incMsg[1].trim(); // Grabs the code from the string
        String server = incMsg[0];
        if (serverName.equals("")) { // set the servername the first time we see it
            serverName = server;
        }
        String outMsg = "";
        /**
         * Handles server commands
         */
        if (incMsg[0].trim().equals("PING")) {
            outMsg = "PONG " + serverName;
        } else if (server.equals(serverName)) {
            switch (code) {
                case "001":
                    outMsg = "JOIN #coconuts";
                    break;
            }
        } else { /** Handles everything else */
            switch (code) {
                case "PRIVMSG":
                    /* Channel message */
                    if (incMsg[2].charAt(0) == '#') {
                        String channel = incMsg[2]; // The channel that the message was received in
                        String nick = what.split("!")[0].split(":")[1]; // The nick that sent it
                        String message = what.split(":")[2]; // The message that was received
                        /* CTCP */
                        if (message.toCharArray()[0] == CTCP_CHAR) { // Ctcps are surrounded by '\u0001'
                            outMsg = handleCTCP(nick, message);
                        }
                        /* Commands */
                        if (message.toCharArray()[0] == Config.COMMAND_CHAR) {
                            outMsg = handleCommand(nick, channel, message);
                        }
                    } else {
                        /* Private message */
                        String nick = what.split("!")[0].split(":")[1]; // The nick that sent it
                        String message = what.split(":")[2]; // The message that was received
                        if (message.toCharArray()[0] == CTCP_CHAR) { // Ctcps are surrounded by '\u0001'
                            outMsg = handleCTCP(nick, message);
                        }
                        /* Commands */
                        if (message.toCharArray()[0] == Config.COMMAND_CHAR) {
                            outMsg = handleCommand(nick, nick, message);
                        }
                    }

                    break;
                case "NOTICE":
                    break;

            }
        }


        if (!outMsg.equals("")) { // If a message has been built, send it and append \n
            con.sendMessage(outMsg + "\n");
        }
    }

    /**
     * @param nick    The nick of the person initiating the command (Will be used for authentication at a later point?)
     * @param target  The target of the message (channel or nick)
     * @param message The message ie the part after the COMMAND_CHAR
     * @return
     */
    private String handleCommand(String nick, String target, String message) {
        String outMsg = "PRIVMSG ";
        outMsg += target + " :"; // Send answer to same channel
        try {
            String command = message.split("\\.")[1];
            outMsg += MessageHandler.command(command); // Finds the appropriate answer
            System.out.println("Command: " + command);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No command");
            outMsg = "";
        } catch (InvalidCommandException e) {
            System.out.println("Invalid command");
            outMsg = "";
        }
        return outMsg;
    }

    /**
     *
     * @param nick    The nick of the user to send the message back to
     * @param message The message ie the CTCP command
     */
    private String handleCTCP(String nick, String message) {
        String ctcp = message.split("\u0001")[1];
        String outMsg;
        try {
            outMsg = "NOTICE ";
            outMsg += nick + " :" + CTCP_CHAR + MessageHandler.ctcp(ctcp) + CTCP_CHAR;
        } catch (InvalidCTCPException e) {
            System.out.println("Invalid CTCP");
            outMsg = ""; // Clears msg so it doesnt get sent
        }
        return outMsg;
    }

    public void run() {

    }

}
