package util.Log;

import java.io.*;

/**
 * Created by stefan on 12/15/15.
 */
public class FileLog {
    public static void writeToFile(String file, String content) throws FileNotFoundException {
        PrintWriter wr = null;
        wr = new PrintWriter(new FileOutputStream(new File(file), true));
        wr.println(content);
        wr.close();
    }
}
