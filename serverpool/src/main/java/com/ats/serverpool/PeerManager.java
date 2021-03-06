package main.java.com.ats.serverpool;

import com.ats.serverpool.Peer;
import com.ats.serverpool.network.udp.server.UDPServer;
import main.java.com.ats.serverpool.network.Callback;
import main.java.com.ats.serverpool.network.tcp.client.*;
import java.net.InetAddress;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Set;

public class PeerManager implements Callback {
    private Peer peer;
    private TCPClient tcpClient;
    private UDPServer udpServer;

    public PeerManager(Peer peer, UDPServer udpServer) {
        this.peer = peer;
        this.udpServer = udpServer;
    }

    public void initTCPClient(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    public int getPeerId() {
        return this.peer.getId();
    }

    public Hashtable<Integer, ArrayList<String>> getRecordTable() {
        return this.peer.getRecordTable();
    }

    public String getRecord(String key) {
        return this.peer.getRecord(key);
    }

    public void insertRecord(String key, String val) {
        this.peer.insertRecord(key, val);
    }

    public void exit(String value) {
        Set<Integer> keys = this.peer.getRecordTable().keySet();

        for (Integer key: keys) {
            this.peer.removeValue(key, value);
        }
    }

    public boolean recordExists(String key) {
        return this.peer.recordExists(key);
    }

    public String getServerPool() {
        String servers = "";

        for (int i = 0; i < this.peer.getServerPool().size(); i++) {
            servers += this.peer.getServerPool().get(i) + ",";
        }

        return servers.substring(0, servers.length() - 1);
    }

    public int getServerPoolCount() {
        return this.peer.getServerPoolCount();
    }

    public void tcpSendMsg(String msg) {
        tcpClient.sendMsg(msg);
    }

    public void udpRespond(String msg, InetAddress addr, int port) {
        udpServer.sendPacket(msg, addr, port);
    }
}