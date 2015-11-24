package sockets;

import sockets.Handler.MessageHandler;

/**
 * Created by mabool on 11/24/15.
 */
public class BotThread extends Thread {
    private Controller con;
    MessageHandler messageHandler;

    public BotThread(Controller con) {
        this.con = con;
    }

    private String SERVER_NAME = "";
    private final char COMMAND_CHAR = '.';

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
                case "020":
                    outMsg = "NICK testytest\n";
                    outMsg += "USER testies 8 * : TestRealName";
                    break;
                case "001":
                    outMsg = "JOIN #coconuts";
                    break;

            }
        } else { /** Handles everything else */
            switch (code) {
                case "PRIVMSG":
                    // Channel message
                    if (incMsg[2].charAt(0) == '#') {
                        String channel = incMsg[2]; // The channel that the message was received in
                        String message = what.split(":")[2]; // The message that was received
                        String nick = what.split("!")[0].split(":")[1]; // The nick that sent it
                        outMsg = "PRIVMSG " + channel + " :"; // Send answer to same channel
                        // Its a command!
                        if (message.toCharArray()[0] == COMMAND_CHAR) {
                            try {
                                String command = message.split("\\.")[1];
                                outMsg += MessageHandler.command(command); // Finds the appropriate answer
                                System.out.println("Command: " + command);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                // No command given
                                System.out.println("No command");
                            }
                        } else { // Its not a command!
                            outMsg += nick + " said " + message;
                        }
                    }
                    // Private user message
                    else {

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
