package main;

import java.util.HashTable;
import java.net.ServerSocket;

public class Peer {
    private String masterIp;    
    private int id;
    private String ip;
    private Peer next;
    private int port;

    private HashTable<String, String> hashtable;

    public Peer(String masterIp, int id, String ip, Peer next, int port) {
        this.masterIp = masterIp;
        this.id = id;
        this.ip = ip;
        this.next = next;
        this.port = port;

        hashtable = new HashTable<>();
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public void setNext(Peer next) {
        this.next = next;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public void setHashTable(HashTable<String, String> hashtable) {
        this.hashtable = hashtable;
    }

    public String getMasterId() {
        return this.masterId;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getIp() {
        return this.ip;
    }
    
    public Peer getPeer() {
        return this.peer;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public HashTable<String, String> getHashTable() {
        return this.hashtable;
    }

}