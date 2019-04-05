package main.java.com.ats.serverpool;
import com.ats.serverpool.Message;

public class Utils {
    public static Message proccessMsg(String string) {
        String[] data = string.split("%");
        Message msg = new Message(data[0], data[1]);

        return msg;
    }
}