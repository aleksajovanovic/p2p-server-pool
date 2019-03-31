package com.ats.serverpool.network.udp.client;

import java.io.*;
import java.net.*;
import com.ats.serverpool.Peer;
import com.ats.serverpool.Message;

class UDPClient {
    private static final int MAX_PACKET_LEN = 65508;
    private DatagramSocket socket;
    private Peer peer;

    public UDPClient(Peer peer) {
        this.peer = peer;

        try {
            this.socket = new DatagramSocket(peer.getPort());
        } catch (SocketException e) {
            System.out.println("Error initalizing DatagramSocket");
            System.out.println(e.getMessage());
        }
    }

    public Message receive() {
        byte[] buffer = new byte[UDPClient.MAX_PACKET_LEN];
        DatagramPacket packet = new DatagramPacket(buffer, UDPClient.MAX_PACKET_LEN);

        try {
            this.socket.receive(packet);
            Message msg = proccessMsg(new String(packet.getData()));

            return msg;
        } catch (IOException e) {
            System.out.println("Error receiving packet from server: ");
            System.out.println(e.getMessage());
        }

        return new Message();
    }

    public void sendPacket(String msg, InetAddress ip, int port) {
        byte[] data = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);

        try {
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("Error sending packet to server");
            System.out.println(e.getMessage());
        }
    }

    private Message proccessMsg(String string) {
        String[] data = string.split("\n");
        Message msg = new Message(data[0], data[1]);

        return msg;
    }
}