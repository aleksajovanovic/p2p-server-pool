package com.ats.serverpool;

import java.util.Hashtable;

import main.java.com.ats.serverpool.Utils;

import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.InetAddress;

public class Peer {
    private String masterIp;
    private int id;
    private InetAddress ip;
    private Peer next;
    private int port;

    private int serverPoolCount;
    private ArrayList<String> serverPool;
    private Hashtable<Integer, ArrayList<String>> recordTable;
    
    public Peer(int id, int port) {
        // this.masterIp = masterIp;
        this.serverPoolCount = 2;
        this.id = id;
        this.port = port;

        serverPool = new ArrayList<>();
        recordTable = new Hashtable<>();
    }

    public void setMasterIp(String masterIp) {
        this.masterIp = masterIp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setRecordTable(Hashtable<Integer, ArrayList<String>> recordTable) {
        this.recordTable = recordTable;
    }
    
    public void setServerPool(ArrayList<String> serverPool) {
        this.serverPool = serverPool;
    }

    public String getMasterIp() {
        return this.masterIp;
    }

    public int getId() {
        return this.id;
    }

    public int getPort() {
        return this.port;
    }

    public Hashtable<Integer, ArrayList<String>> getRecordTable() {
        return this.recordTable;
    }
    
    public ArrayList<String> getServerPool() {
        return this.serverPool;
    }
    
    public int getServerPoolCount() {
        return this.serverPoolCount;
    }

    public void insertRecord(String key, String value) {
        // if record already has value, then add to array
        int keyNum = Utils.hash(key);
        if (recordExists(key)) {
            this.recordTable.get(keyNum).add(value);
            return;
        }

        ArrayList<String> values = new ArrayList<>();
        values.add(value);
        this.recordTable.put(keyNum, values);
    }

    public void removeRecord(String key, String value) {
        ArrayList<String> values = this.recordTable.get(key);
        if (values.size() < 1)  
            this.recordTable.remove(key);
        else 
            values.remove(value);
    }

    public boolean recordExists(String key) {
        return this.recordTable.containsKey(key);
    }

    public String getRecord(String key) {
        int keyNum = Utils.hash(key);
        // System.out.println(this.recordTable.get(key));
        System.out.println("(PEER) Keys in hashtable are: " + this.getRecordTable().entrySet());
        return this.recordTable.get(keyNum).get(0);
    }
    
    public boolean insertServer(String peerIp) {
        serverPoolCount++;
        return this.serverPool.add(peerIp);
    }

    public String removeServer(int index) {
        serverPoolCount = serverPoolCount == 0 ? 0 : serverPoolCount - 1;
        return this.serverPool.remove(index);
    }
}