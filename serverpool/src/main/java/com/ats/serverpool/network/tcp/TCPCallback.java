package main.java.com.ats.serverpool.network.tcp;
import java.net.InetAddress;

public interface TCPCallback {
    public int getPeerId();
    public void insertRecord(String key, String val);
    public void removeRecord(String key, String val);
    public boolean recordExists(String key);
    public String getRecord(String key);
    public void tcpSendMsg(String msg);
    public void udpRespond(String msg, InetAddress addr, int port);
}