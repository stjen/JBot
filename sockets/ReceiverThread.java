package sockets;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 *  This file is part of Jbot.
 *
 *  Jbot is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  Jbot is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Jbot.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
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
        data = "";
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
