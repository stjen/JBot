package sockets.Handler;

import sockets.BotThread;

/**
 * Created by mabool on 11/24/15.
 */
public class MessageHandler {
    BotThread bot;

    public MessageHandler(BotThread bot) {
        this.bot = bot;
    }

    public static String command(String c) {
        switch (c.toLowerCase()) {
            case "hello":
                return "Rude.";

        }
        return "k";
    }



}
