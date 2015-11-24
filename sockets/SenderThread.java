package sockets;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by mabool on 11/24/15.
 * TODO:
 * - Throttled buffer for the output
 */
public class SenderThread extends Thread {

    Socket socket;
    OutputStream out;

    public SenderThread(Socket socket) throws IOException {
        this.socket = socket;
        out = socket.getOutputStream();
    }

    public void run() {

    }

    public void output(String what) {
        try {
            System.out.printf("> %s", what);
            out.write(what.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
