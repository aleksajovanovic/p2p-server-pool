package com.ats.serverpool;

import java.util.Hashtable;
import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.InetAddress;

public class Peer {
    private InetAddress masterIp;
    private int id;
    private InetAddress ip;
    private Peer next;
    private int port;

    private int serverPoolIndex;
    private ArrayList<Peer> serverPool;
    private Hashtable<String, String> recordTable;

    public Peer(InetAddress masterIp, int id, InetAddress ip, Peer next, int port) {
        this.masterIp = masterIp;
        this.id = id;
        this.ip = ip;
        this.next = next;
        this.port = port;
        this.serverPoolIndex = 0;

        serverPool = new ArrayList<>();
        recordTable = new Hashtable<>();
    }

    public void setMasterIp(InetAddress masterIp) {
        this.masterIp = masterIp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public void setNext(Peer next) {
        this.next = next;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setRecordTable(Hashtable<String, String> recordTable) {
        this.recordTable = recordTable;
    }
    
    public void setServerPool(ArrayList<Peer> serverPool) {
        this.serverPool = serverPool;
    }

    public InetAddress getMasterIp() {
        return this.masterIp;
    }

    public int getId() {
        return this.id;
    }

    public InetAddress getIp() {
        return this.ip;
    }

    public Peer getPeer() {
        return this.next;
    }

    public int getPort() {
        return this.port;
    }

    public Hashtable<String, String> getRecordTable() {
        return this.recordTable;
    }
    
    public ArrayList<Peer> getServerPool() {
        return this.serverPool;
    }

    public void insertRecord(String key, String value) {
        this.recordTable.put(key, value);
    }

    public String removeRecord(String key) {
        return this.recordTable.remove(key);
    }
    
    public boolean insertServer(Peer peer) {
        serverPoolIndex++;
        return this.serverPool.add(peer);
    }

    public Peer removeServer(int index) {
        serverPoolIndex = serverPoolIndex == 0 ? 0 : serverPoolIndex - 1;
        return this.serverPool.remove(index);
    }
}