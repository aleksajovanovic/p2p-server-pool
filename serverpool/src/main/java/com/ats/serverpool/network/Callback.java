package main.java.com.ats.serverpool.network;
import java.net.InetAddress;
import java.util.Hashtable;
import java.util.ArrayList;

public interface Callback {
    public int getPeerId();
    public void insertRecord(String key, String val);
    public void exit(String value);
    public boolean recordExists(String key);
    public Hashtable<Integer, ArrayList<String>> getRecordTable();
    public String getRecord(String key);
    public String getServerPool();
    public int getServerPoolCount();
    public void tcpSendMsg(String msg);
    public void udpRespond(String msg, InetAddress addr, int port);
}