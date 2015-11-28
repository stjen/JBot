package sockets;

import sockets.Exceptions.EmptyCollectionException;
import util.ArrayQueue.CircularArrayQueue;
import util.ArrayQueue.QueueADT;
import util.Log.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by mabool on 11/24/15.
 */
public class SenderThread extends Thread {

    Socket socket;
    BufferedThread buffer;
    Log log;

    public SenderThread(Socket socket) throws IOException {
        this.socket = socket;
        buffer = new BufferedThread(socket);
        buffer.start();
        log = Log.getInstance();
    }

    public void run() {
    }


    public void output(String what) {

        System.out.printf("> %s", what);
        log.add(">" + what);
        buffer.addToQueue(what.getBytes());

    }

    /**
     * Making another sending thread so we dont block the main buffer thread each time we have data queued
     * TODO: Make it smarter instead of a constant wait time..
     */
    class BufferedThread extends Thread {
        OutputStream out;
        QueueADT<byte[]> sendQueue;

        public BufferedThread(Socket socket) throws IOException {
            out = socket.getOutputStream();
            sendQueue = new CircularArrayQueue<>();
        }

        public void run() {
            final long waitTime = 2000;
            while (true) {
                /* If there is something in the queue, send it */
                if (!sendQueue.isEmpty()) {
                    try {
                        send(sendQueue.dequeue());
                    } catch (EmptyCollectionException e) {
                        System.out.println("Empty queue " + e);
                    }
                }
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void addToQueue(byte[] what) {
            sendQueue.enqueue(what);
        }

        private void send(byte[] what) {
            try {
                out.write(what);
                out.flush();
            } catch (IOException e) {
                System.out.println("Error writing to socket!");
                System.exit(-1);
            }
        }
    }

}
