package sockets.Handler;

import sockets.BotThread;
import sockets.Config;
import sockets.Exceptions.InvalidCTCPException;
import sockets.Exceptions.InvalidCommandException;
import sockets.Exceptions.InvalidServerCommandException;

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
        switch (c.toLowerCase()) {
            case "hello":
                return "Rude.";
            case "w":
                return "The weather forecast is: Fuck you";

        }
        throw new InvalidCommandException(c);
    }

    public static String server(String code) throws InvalidServerCommandException {
        switch (code) {
            case "001":
                return "";
        }
        throw new InvalidServerCommandException(code);
    }

}
