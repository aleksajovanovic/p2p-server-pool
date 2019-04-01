package com.ats.serverpool;

import java.util.Hashtable;
import java.net.ServerSocket;
import java.net.InetAddress;

public class Peer {
    private String masterId;
    private int id;
    private InetAddress ip;
    private Peer next;
    private int port;

    private Hashtable<String, String> hashtable;

    public Peer(String masterId, int id, InetAddress ip, Peer next, int port) {
        this.masterId = masterId;
        this.id = id;
        this.ip = ip;
        this.next = next;
        this.port = port;

        hashtable = new Hashtable<>();
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
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

    public void setHashTable(Hashtable<String, String> hashtable) {
        this.hashtable = hashtable;
    }

    public String getMasterId() {
        return this.masterId;
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

    public Hashtable<String, String> getHashTable() {
        return this.hashtable;
    }

}