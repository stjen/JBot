package util.Log;

import java.io.*;

/**
 * Created by stefan on 12/15/15.
 */
public class FileLog {
    public static void writeToFile(String file, String content) {
        PrintWriter wr = null;
        try {
            wr = new PrintWriter(new FileOutputStream(new File(file), true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        wr.println(content);
        wr.close();
    }
}
