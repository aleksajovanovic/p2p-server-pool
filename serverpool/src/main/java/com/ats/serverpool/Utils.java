package main.java.com.ats.serverpool;
import com.ats.serverpool.Message;

public class Utils {
    public static Message proccessMsg(String string) {
        String[] data = string.split("%");
        Message msg = new Message(data[0], data[1]);

        return msg;
    }

    public static int hash(String fileName) {
        char chars[] = new char[fileName.length()];
        fileName.getChars(0, fileName.length() - 1, chars, 0);
        int hash = 0;

        for (int i = 0; i < chars.length; i++) {
            hash += chars[i];
        }

        return hash;
    }

    public static int getPort() {
        return 20410;
    }
}