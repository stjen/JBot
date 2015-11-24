package sockets;

import sockets.Exceptions.InvalidCTCPException;
import sockets.Exceptions.InvalidCommandException;
import sockets.Handler.MessageHandler;

/**
 * Created by mabool on 11/24/15.
 */
public class BotThread extends Thread {
    private Controller con;


    public BotThread(Controller con) {
        this.con = con;
    }

    private String SERVER_NAME = "";
    private final char COMMAND_CHAR = '.';

    /**
     * Takes care of initial connection details, such as setting nick and identifying USER
     */
    public void initConnection() {
        String outMsg = "NICK testytest\n";
        outMsg += "USER testies 8 * : TestRealName";
        con.sendMessage(outMsg + "\n");
    }

    public void handleMessage(String what) {
        what = what.trim();
        String[] incMsg = what.split("\\s+"); // Split by empty space
        String code = incMsg[1].trim(); // Grabs the code from the string
        String server = incMsg[0];
        if (SERVER_NAME.equals("")) { // set the servername the first time we see it
            SERVER_NAME = server;
        }
        //System.out.println("Code: " + code);
        String outMsg = "";
        /**
         * Handles server commands
         */
        if (incMsg[0].trim().equals("PING")) {
            outMsg = "PONG " + SERVER_NAME;
        } else if (server.equals(SERVER_NAME)) {
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
                        if (message.toCharArray()[0] == '\u0001') { // Ctcps are surrounded by '\u0001'
                            String ctcp = message.split("\u0001")[1];
                            try {
                                outMsg = "NOTICE ";
                                outMsg += nick + " :\u0001" + MessageHandler.ctcp(ctcp) + "\u0001";
                            } catch (InvalidCTCPException e) {
                                System.out.println("Invalid CTCP");
                                outMsg = ""; // Clears msg so it doesnt get sent
                            }
                        }
                        /* Commands */
                        if (message.toCharArray()[0] == COMMAND_CHAR) {
                            outMsg = "PRIVMSG ";
                            outMsg += channel + " :"; // Send answer to same channel
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



    public void run() {

    }

}
