package sockets.Handler;

import sockets.BotThread;
import sockets.Config;
import sockets.Exceptions.InvalidCTCPException;
import sockets.Exceptions.InvalidCommandException;
import sockets.Exceptions.InvalidServerCommandException;
import sockets.Plugins.Weather;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
        String[] incMsg = c.split("\\s+");
        switch (incMsg[0].toLowerCase()) {
            case "hello":
                return "Rude.";
            case "w":
                return Weather.getWeather(c);

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
                return "NICK " + Config.BOT_NICK + ThreadLocalRandom.current().nextInt(0,100);

        }
        throw new InvalidServerCommandException(code);
    }

}
