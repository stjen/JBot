package mambutu;

import mambutu.Exceptions.InvalidCTCPException;
import mambutu.Exceptions.InvalidCommandException;
import mambutu.Exceptions.InvalidServerCommandException;
import mambutu.Handler.MessageDistributor;

/**
 * This file is part of Mambutu.
 * <p/>
 * Mambutu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Mambutu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with Mambutu.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * <p/>
 * Created by mabool on 11/24/15.
 */
public class Bot {

    /**
     * TODO:
     * - Keep track of own nickname (might be force-changed by server)
     */

    public static final char CTCP_CHAR = '\u0001';
    public static final char MSG_INC_CHAR = '<';
    public static final char MSG_OUT_CHAR = '>';


    private Controller con;
    private MessageDistributor messageDistributor;

    public Bot(Controller con) {
        this.con = con;
        messageDistributor = new MessageDistributor(this);
    }

    private String serverName = "";
    private String sessionBotNick = "";

    public MessageDistributor getMessageDistributor() {
        return messageDistributor;
    }

    /**
     * Takes care of initial connection details, such as setting nick and identifying USER
     */
    public void initConnection() {
        String outMsg = "USER " + Config.BOT_USERNAME + " 8 * : " + Config.BOT_REALNAME;
        outMsg += "\nNICK " + Config.BOT_NICK;
        con.sendMessage(outMsg + "\n");
    }

    public void handleMessage(String what) {
        what = what.trim();
        String[] incMsg = what.split("\\s+"); // Split by empty space
        int incMsgLength = what.split("\\s+").length;
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
            outMsg = handleServer(what, incMsg);
        } else if (incMsgLength > 2) { /** Handles everything else */
            switch (code) {
                case "PRIVMSG":
                        String receiver = incMsg[2]; // The receiver of the message (channel / nick)
                        String nick = what.split("!")[0].split(":")[1]; // The nick that sent it
                        String message = what.split(receiver + " :", 2)[1]; // The message that was received
                        //System.out.println("Receiver: " + receiver + " sender: " + nick + " message: " + message);
                        if (message.length() > 0) {
                        /* CTCP */
                            if (message.toCharArray()[0] == CTCP_CHAR) { // Ctcps are surrounded by '\u0001'
                                outMsg = handleCTCP(nick, message);
                            }
                        /* Commands */
                            if (message.toCharArray()[0] == Config.COMMAND_CHAR) {
                                  if (incMsg[2].charAt(0) == '#') { // Received from channel
                                      outMsg = handleCommand(nick, receiver, message);
                                  } else { // Received from user
                                      outMsg = handleCommand(nick, nick, message);
                                  }
                            }
                    //    }
                    }
                    break;
                case "NOTICE": // Not handling notices for now
                    break;
                case "MODE":
                    break;
                case "KICK":
                    if (incMsg[3].trim().equals(sessionBotNick)) { // Bot was kicked
                        if (Config.AUTO_REJOIN_CHANNEL) {
                            String channel = incMsg[2];
                            outMsg += "JOIN " + channel;
                        }
                        // Rejoin or dont
                    }
                    break;

            }
        } else {
            // Do nothing
        }


        if (!outMsg.equals("")) { // If a message has been built, send it and append \n
            con.sendMessage(outMsg + "\n");
        }
    }

    private String handleServer(String message, String[] incMsg) {
        String outMsg;
        String code = incMsg[1].trim();
        try {
            outMsg = messageDistributor.server(code);
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
            outMsg += messageDistributor.command(nick, target, command); // Finds the appropriate answer
            //System.out.println("Command: " + command);
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
            outMsg += nick + " :" + CTCP_CHAR + MessageDistributor.ctcp(ctcp) + CTCP_CHAR;
        } catch (InvalidCTCPException e) {
            System.out.println("Invalid CTCP");
            outMsg = ""; // Clears msg so it doesnt get sent
        }
        return outMsg;
    }

    public void run() {

    }

}
