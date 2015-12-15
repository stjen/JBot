package Tests;

import sockets.Config;
import util.Log.FileLog;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * Created by stefan on 12/15/15.
 */
public class FileTest {
    public static void main(String[] args) {
        FileLog.writeToFile(Config.LOG_FILE, "Test");
    }
}
