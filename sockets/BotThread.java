package sockets;

import sockets.Exceptions.InvalidCTCPException;
import sockets.Exceptions.InvalidCommandException;
import sockets.Exceptions.InvalidServerCommandException;
import sockets.Handler.MessageHandler;

/**
 *  This file is part of Mambutu.
 *
 *  Mambutu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  Mambutu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Mambutu.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Created by mabool on 11/24/15.
 */
public class BotThread extends Thread {

    /**
     * TODO:
     * - Keep track of own nickname (might be force-changed by server)
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
        String outMsg = "NICK " + Config.BOT_NICK;
        outMsg += "\nUSER " + Config.BOT_USERNAME + " 8 * : " + Config.BOT_REALNAME;
        // Joins all channels
        for (int i = 0; i < Config.CHANNELS.split(",").length; i++) {
            outMsg += "\nJOIN " + Config.CHANNELS.split(",")[i].trim();
        }
        outMsg += "\nJOIN #coconuts";
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
            handleServer(what, incMsg);
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
                case "NOTICE": // Not handling notices for now
                    break;

            }
        }


        if (!outMsg.equals("")) { // If a message has been built, send it and append \n
            con.sendMessage(outMsg + "\n");
        }
    }

    private String handleServer(String message, String[] incMsg) {
        String outMsg;
        String code = incMsg[1].trim();
        try {
            outMsg = MessageHandler.server(code);
        } catch (InvalidServerCommandException e) {
            outMsg = "";
        }

        return outMsg;
    }

    /**
     * @param nick    The nick of the person initiating the command (Will be used for authentication at a later point?)
     * @param target  The target of the message (channel or nick)
     * @param message The message ie the part after the COMMAND_CHAR
     */
    private String handleCommand(String nick, String target, String message) {
        String outMsg = "PRIVMSG ";
        outMsg += target + " :"; // Send answer to same channel
        try {
            // Removes the command char from the message, to get the command
            String command = message.split("\\" + Character.toString(Config.COMMAND_CHAR))[1];
            if (Config.APPEND_NICK_CMD)
                outMsg += nick + ": ";
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
