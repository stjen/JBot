package sockets;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by mabool on 11/24/15.
 */
public class ReceiverThread extends Thread {
    Socket socket;
    InputStream in;
    String data;
    Controller con;

    public ReceiverThread(Controller con, Socket socket) throws IOException {
        this.socket = socket;
        this.con = con;
        in = socket.getInputStream();
        data = new String();
    }

    public void run() {
        while (true) {
            try {
                data += Character.toString((char) in.read());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Keep building string until end of line
            if (data.substring(data.length() - 1).equals("\n")) {
                if (!data.substring(0).equals("\n")) { // Don't pass on an empty string
                    System.out.printf("< %s", data);
                    con.newInput(data);
                }
                data = new String();
            }

        }

    }


}
