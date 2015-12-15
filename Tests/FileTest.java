package Tests;

import mambutu.Config;
import util.Log.FileLog;

/**
 * Created by stefan on 12/15/15.
 */
public class FileTest {
    public static void main(String[] args) {
        FileLog.writeToFile(Config.LOG_FILE, "Test");
    }
}
