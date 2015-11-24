package sockets;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by mabool on 11/24/15.
 */
public class Controller {
    Socket socket;
    ReceiverThread rt;
    SenderThread st;
    BotThread bot;


    public Controller() {
        try {
            socket = new Socket("upx.dk", 6667);
            rt = new ReceiverThread(this, socket);
            st = new SenderThread(socket);
            bot = new BotThread(this);
            st.start();
            rt.start();
            bot.start();
        } catch (IOException e) {
            System.out.printf("Connection not possible");
        }
    }

    public void sendMessage(String msg) {
        st.output(msg);
    }

    public void newInput(String msg) {
        bot.handleMessage(msg);
    }


}
