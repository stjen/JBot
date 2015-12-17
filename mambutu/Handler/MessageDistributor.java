package mambutu.Handler;

import mambutu.Bot;
import mambutu.Config;
import mambutu.Exceptions.InvalidCTCPException;
import mambutu.Exceptions.InvalidCommandException;
import mambutu.Exceptions.InvalidServerCommandException;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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
public class MessageDistributor {

    /**
     * TODO: Special handlers for connection stuff like PING/PONG, authentication to nickserv etc
     */

    Bot bot;

    private ArrayList<Handler> CTCPHandlers;
    private ArrayList<Handler> CommandHandlers;
    private ArrayList<Handler> MessageHandlers;


    public MessageDistributor(Bot bot) {
        CTCPHandlers = new ArrayList<>();
        CommandHandlers = new ArrayList<>();
        MessageHandlers = new ArrayList<>();
        this.bot = bot;
    }

    public void registerCTCPHandler(Handler theHandler) {
        CTCPHandlers.add(theHandler);
    }

    public void registerCommandHandler(Handler theHandler) {
        CommandHandlers.add(theHandler);
    }

    public void registerMessageHandler(Handler theHandler) {
        MessageHandlers.add(theHandler);
    }

    // Removes the handler from any list
    public void unregisterHandler(Handler theHandler) {
        CTCPHandlers.remove(theHandler);
        CommandHandlers.remove(theHandler);
        MessageHandlers.remove(theHandler);
    }

    public static String ctcp(String c) throws InvalidCTCPException {
        String preText = c.toUpperCase() + " ";
        switch (c.toLowerCase()) {
            case "version":
                return preText + Config.PROGRAM_NAME + " " + Config.VERSION;
        }
        throw new InvalidCTCPException(c);
    }

    public String command(String nick, String target, String c) throws InvalidCommandException {
        String[] command = c.split("\\s+");
        String arg = c.replace(command[0], ""); // Removes the command from the string

        /* Loop through all the registered command handlers, for each of them loop through all the commands
         they are able to handle, and see if they match the command in question
         If they do, call their handle(..) method */
        System.out.println(command[0]);
        for (int i = 0; i < CommandHandlers.size(); i++) {
            if (CommandHandlers.get(i).handles(command[0]))
                return CommandHandlers.get(i).handle(command[0], arg, nick);
        }
        // TODO: Convert weather/bug to the new handler system


     /*   String location;
        switch (incMsg[0].toLowerCase()) {
            case "hello":
                return "Rude.";
            case "w":
                location = c.replaceFirst("w", "").trim();
                return Weather.getWeather(location);
            case "fc":
                 Format the input string
                location = c.replaceFirst("fc", "").trim();
                return Weather.getForecast(location);
            case "bug":
                if (Config.BUG_LOG_ENABLED) {
                    Log.getInstance().add(c);
                    c = c.replaceFirst("bug", "").trim();
                    if (c.equals("")) { // the remainder is an empty string
                        return "The bug log can be found at: " + Config.BUG_LOG_FILE_URL;
                    }
                    FileLog.writeToFile(Config.BUG_LOG_FILE, System.currentTimeMillis() + "-" + nick + "@" + target + ": " + c);
                    return "Added \"" + c + "\" to the bug log";
                }
                return "";
        }*/
        throw new InvalidCommandException(c);
    }

    // https://tools.ietf.org/html/rfc1459#section-6

    public String server(String code) throws InvalidServerCommandException {
        switch (code) {
            case "001": // We are connected
                String outMsg = "";
                // Joins all channels
                for (int i = 0; i < Config.CHANNELS.split(",").length; i++) {
                    outMsg += "\nJOIN " + Config.CHANNELS.split(",")[i].trim();
                }
                return outMsg;
            case "433": // ERR_NICKNAMEINUSE
                return "NICK " + Config.BOT_NICK + ThreadLocalRandom.current().nextInt(0, 100);

        }
        throw new InvalidServerCommandException(code);
    }


}
