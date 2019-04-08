package com.ats.serverpool.network.udp.server;

import java.io.*;
import java.net.*;
import com.ats.serverpool.Message;
import main.java.com.ats.serverpool.Utils;
import main.java.com.ats.serverpool.network.Callback;

public class UDPServer extends Thread {
    private static final int MAX_PACKET_LEN = 65508;
    private DatagramSocket socket;
    private int bindPort;
    private Callback callback;

    public UDPServer(int bindPort) {
        this.bindPort = bindPort;

        try {
            this.socket = new DatagramSocket(this.bindPort);
        } catch (SocketException e) {
            System.out.println("Error initalizing DatagramSocket");
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        System.out.println("UDPServer listening on port " + this.bindPort + "...");
        
        while (true) {
            this.receive();
        }
    }

    public void initCallback(Callback callback) {
        this.callback = callback;
    }

    private void receive() {
        byte[] buffer = new byte[UDPServer.MAX_PACKET_LEN];
        DatagramPacket packet = new DatagramPacket(buffer, UDPServer.MAX_PACKET_LEN);

        try {
            this.socket.receive(packet);
            Message msg = Utils.proccessMsg(new String(packet.getData()));
            respond(msg, packet.getAddress(), packet.getPort());
        } catch (IOException e) {
            System.out.println("Error receiving packet from client: ");
            System.out.println(e.getMessage());
        }
    }

    private void respond(Message msg, InetAddress ip, int port) {
        String message[] = msg.getMessageType().split(",");

        switch (msg.getMessageType()) {
        case "init":
            init(ip, port);
            break;
        case "informAndUpdate":
            informAndUpdate(message, ip, port);
            break;
        case "query":
            query(message, ip, port);
            break;
        case "exit":
            exit(message, ip, port);
            break;
        default:
            System.out.println("Type of client request not recognized: " + msg.getMessageType());
            break;
        }
    }

    public void sendPacket(String msg, InetAddress ip, int port) {
        byte[] data = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);

        try {
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("Error sending packet response to client");
            System.out.println(e.getMessage());
        }
    }

    /**x
     * each p2p client knows IP address of directory server (ID=1) starting with
     * this IP the p2p client needs to ask DHT for IP addresses of remaining servers
     * and get them.
     */
    private void init(InetAddress ip, int port) {
        String servers = callback.getServerPool();
        String response = "init%" + "OK" + "," + servers;
        sendPacket(response, ip, port);
    }

    /**
     * p2p client needs to perdorm hashing of content name into server id contact
     * target server to store the recorde (content name, client IP) keep the local
     * recorde (content name, DHT server, server' IP)
     */
    private void informAndUpdate(String[] msg, InetAddress ip, int port) {
        if (Utils.hash(msg[0]) % 4 + 1 == callback.getPeerId()) {
            callback.insertRecord(msg[0], ip.toString());
            sendPacket("informAndUpdate%OK", ip, port);
        }
        else {
            String tcpMsg = "insert" + "%" + msg[0] + "," + ip.toString() + "," + callback.getPeerId() + "," + String.valueOf(port);
            callback.tcpSendMsg(tcpMsg);
        }
    }

    /**
     * requires p2p client to: - perform the hashing of the content's name into
     * server id - contact server DHT to find the IP of client with required content
     * name (after init all IP addresses of servers in DHT are known) - if content
     * does not exist in the network DHTm return code "404 content not found"
     */
    private void query(String[] msg, InetAddress ip, int port) {
        String response = "query%list of ip addresses with content received";
        sendPacket(response, ip, port);
    }

    /**
     * p2p client request has to be dispersed across all servers in DHT inform them
     * to remove the records related to the contnet stored DHT server chosen as the
     * entry can be any of the 4 and request as to be passed over the ring to delete
     * all the recorded owned by the client who wants to exist
     */
    private void exit(String[] msg, InetAddress ip, int port) {
        String response = "exit%Peer successfully removed";
        sendPacket(response, ip, port);
    }

}