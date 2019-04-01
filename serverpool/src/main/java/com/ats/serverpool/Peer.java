package com.ats.serverpool;

import java.util.HashTable;
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
    private HashTable<String, String> recordTable;

    public Peer(InetAddress masterIp, int id, InetAddress ip, Peer next, int port) {
        this.masterIp = masterIp;
        this.id = id;
        this.ip = ip;
        this.next = next;
        this.port = port;
        this.serverPoolIndex = 0;

        serverPool = new ArrayList<>();
        recordTable = new HashTable<>();
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

    public void setRecordTable(HashTable<String, String> recordTable) {
        this.recordTable = recordTable;
    }
    
    public void setServerPool(LinkedList<String> serverPool) {
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

    public HashTable<String, String> getRecordTable() {
        return this.recordTable;
    }
    
    public LinkedList<String> getServerPool() {
        return this.serverPool;
    }

    public void addRecord(String key, String value) {
        this.recordTable.put(key, value);
    }

    public boolean removeRecord(String key) {
        return this.recordTable.remove(key);
    }
    
    public boolean addServer(Peer peer) {
        serverPoolIndex++;
        return this.serverPool.add(peer);
    }

    public Peer removeServer(int index) {
        serverPoolIndex = serverPoolIndex == 0 ? 0 : serverPoolIndex - 1;
        return this.recordTable.remove(index);
    }
}