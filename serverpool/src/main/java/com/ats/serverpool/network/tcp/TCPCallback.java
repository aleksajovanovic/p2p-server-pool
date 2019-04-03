package main.java.com.ats.serverpool.network.tcp;

public interface TCPCallback {
    public void insertRecord(String key, String val);
    public String removeRecord(String key);
    public boolean recordExists(String key);
}