package main.java.com.ats.serverpool;

import com.ats.serverpool.Peer;
import com.ats.serverpool.network.udp.client.UDPClient;
import main.java.com.ats.serverpool.network.tcp.TCPCallback;
import main.java.com.ats.serverpool.network.tcp.client.*;

public class PeerManager implements TCPCallback {
    private Peer peer;
    private TCPClient tcpClient;
    private UDPClient udpClient;

    public PeerManager(Peer peer, TCPClient tcpClient, UDPClient udpClient) {
        this.peer = peer;
        this.tcpClient = tcpClient;
        this.udpClient = udpClient;
    }

    public void insertRecord(String key, String val) {
        // this.peer.insertRecord(key, val);
        System.out.println("rando shit dog");
    }

    public String removeRecord(String key) {
        return this.peer.removeRecord(key);
    }

    public boolean recordExists(String key) {
        return this.peer.recordExists(key);
    }
}