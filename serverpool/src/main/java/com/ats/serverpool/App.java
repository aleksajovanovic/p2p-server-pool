package com.ats.serverpool;

import com.ats.serverpool.network.udp.client.*;
import com.ats.serverpool.network.udp.server.*;

import main.java.com.ats.serverpool.PeerManager;
import main.java.com.ats.serverpool.network.tcp.client.*;
import main.java.com.ats.serverpool.network.tcp.server.*;
import java.net.InetAddress;

public class App {
    public static void main(String[] args) {
        try {            
            Peer peer = new Peer(InetAddress.getByName("127.0.0.1"), 1, InetAddress.getByName("127.0.0.1"), null, 17603);
            PeerManager peerManager;
            UDPClient udpClient = new UDPClient(17600);
            UDPServer udpServer = new UDPServer(17603);
            
            udpServer.start();

            // udpClient.sendPacket("init\npoop", InetAddress.getLocalHost(), 17603);
            // Message msg = udpClient.receive();

            // System.out.println(msg.getMessage());

            TCPServer tcpServer = new TCPServer(17603);
            TCPClient tcpClient = new TCPClient(InetAddress.getLocalHost(), 17603);
            peerManager = new PeerManager(peer, tcpClient, udpClient);
            tcpServer.initCallback(peerManager);
            Thread thread = new Thread(tcpServer);
            
            thread.start();
        
            tcpClient.sendMsg("poop");

        } catch (Exception e) {

        }
    }
}
