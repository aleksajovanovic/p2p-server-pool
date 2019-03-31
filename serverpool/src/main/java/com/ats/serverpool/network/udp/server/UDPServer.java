package com.ats.serverpool.network.udp.server;

import java.io.*;
import java.net.*;
import com.ats.serverpool.Peer;

class UDPServer extends Thread {
    private static final int MAX_PACKET_LEN = 65508; 
    private DatagramSocket socket;
    private Peer peer;

    public UDPServer(Peer peer) {
        this.peer = peer;

        try {
            this.socket = new DatagramSocket(peer.getPort());
        } catch(SocketException e) {
            System.out.println("Error initalizing DatagramSocket");
            System.out.println(e.getMessage());
        }
    }
    
    public void run() {
        UDPServer udpServer = new UDPServer(this.peer.getPort());

        while (true) {
            udpServer.receivePacket();
        }
    }

    private void receive() {
        byte[] buffer = new byte[UDPServer.MAX_PACKET_LEN];
        DatagramPacket packet = new DatagramPacket(buffer, UDPServer.MAX_PACKET_LEN);
        
        try {
            this.socket.receive(packet);
            Message msg = processMessageReceived(new String(receivePacket.getData()));
            respond(msg, packet.getAddress(), packet.getPort());
        } catch(IOException e) {
            System.out.println("Error receiving packet from client: ");
            System.out.println(e.getMessage());
        }
    }

    private void respond(Message msg, InetAddress ip, int port) {
        switch (msg.getMessageType()) {
            case "init":
                init(msg.getMessage(), ip, port);
                break;
            case "informAndUpdate":
                informAndUpdate(msg.getMessage(), ip, port);
                break;
            case "query":
                query(msg.getMessage(), ip, port);
                break;
            case "exit":
                exit(msg.getMessage(), ip, port);
                break;
            default:
                System.out.println("Type of client request not recognized: "  + msg.getMessageType());
                break;
        }
    }

    private Message proccessMsg(String string) {
        String[] data = string.split("\n");
        Message msg = new Message(data[0], data[1]);

        return msg;
    }

    private void sendPacket(String msg, InetAddress ip, int port) {
        byte[] data = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        
        try {
            socket.send(packet);
        } catch(IOException e) {
            System.out.println("Error sending packet response to client");
            System.out.println(e.getMessage());
        }
    }

    /** each p2p client knows IP address of directory server (ID=1)
     *  starting with this IP the p2p client needs to ask DHT for
     *  IP addresses of remaining servers and get them.
     */
    private void init(String msg, InetAddress ip, int port) {
         String response = "init\nlist of DHT servers";
         sendPacket(response, ip, port);
    }

    /** p2p client needs to perdorm hashing of content name into server id
     *  contact target server to store the recorde (content name, client IP)
     *  keep the local recorde (content name, DHT server, server' IP)
     */
    private void informAndUpdate(String msg, InetAddress ip, int port) {
        String response = "informAndUpdate\nnew foto added to DHT";
        sendPacket(response, ip, port);
    }
    
    /** requires p2p client to:
     *  - perform the hashing of the content's name into server id
     *  - contact server DHT to find the IP of client with required content name
     *    (after init all IP addresses of servers in DHT are known)
     *  - if content does not exist in the network DHTm return code "404 content not found"
    */
    private void query(String msg, InetAddress ip, int port) {
        String response = "query\nlist of ip addresses with content received";
        sendPacket(response, ip, port);
    }

    /** p2p client request has to be dispersed across all servers in DHT
     *  inform them to remove the records related to the contnet stored 
     *  DHT server chosen as the entry can be any of the 4 and request
     *  as to be passed over the ring to delete all the recorded owned
     *  by the client who wants to exist
     */
    private void exit(String msg, InetAddress ip, int port) {
        String response = "exit\nPeer successfully removed";
        sendPacket(response, ip, port);
    }

}