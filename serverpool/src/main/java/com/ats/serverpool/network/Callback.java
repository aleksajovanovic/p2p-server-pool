package main.java.com.ats.serverpool.network;
import java.net.InetAddress;

public interface Callback {
    public int getPeerId();
    public void insertRecord(String key, String val);
    public void removeRecord(String key, String val);
    public boolean recordExists(String key);
    public String getRecord(String key);
    public String getServerPool();
    public void tcpSendMsg(String msg);
    public void udpRespond(String msg, InetAddress addr, int port);
}