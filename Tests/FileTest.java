package Tests;

import mambutu.Config;
import util.Log.FileLog;

import java.io.FileNotFoundException;

/**
 * Created by stefan on 12/15/15.
 */
public class FileTest {
    public static void main(String[] args) throws FileNotFoundException {
        FileLog.writeToFile(Config.LOG_FILE, "Test");
    }
}
