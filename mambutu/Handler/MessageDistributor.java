package mambutu.Handler;

import mambutu.Bot;
import mambutu.Config;
import mambutu.Exceptions.InvalidCTCPException;
import mambutu.Exceptions.InvalidCommandException;
import mambutu.Exceptions.InvalidServerCommandException;
import mambutu.Plugins.Weather;
import util.Log.FileLog;
import util.Log.Log;

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



    Bot bot;


    public MessageDistributor(Bot bot) {
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

    public static String command(String nick, String target, String c) throws InvalidCommandException {
        String[] incMsg = c.split("\\s+");
        String location;
        switch (incMsg[0].toLowerCase()) {
            case "hello":
                return "Rude.";
            case "w":
                location = c.replaceFirst("w", "").trim();
                return Weather.getWeather(location);
            case "fc":
                /* Format the input string */
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
        }
        throw new InvalidCommandException(c);
    }

    // https://tools.ietf.org/html/rfc1459#section-6

    public static String server(String code) throws InvalidServerCommandException {
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

    public boolean handles(String command) {
        String[] arrayHandles = handles.split(",");
        for (int i = 0; i < arrayHandles.length; i++) {
            if (arrayHandles[i].equals(command))
                return true;
        }
        return false;
    }



}
