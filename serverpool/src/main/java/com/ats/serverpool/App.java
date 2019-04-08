package com.ats.serverpool;

import com.ats.serverpool.network.udp.client.*;
import com.ats.serverpool.network.udp.server.*;

import main.java.com.ats.serverpool.PeerManager;
import main.java.com.ats.serverpool.Utils;
import main.java.com.ats.serverpool.network.tcp.client.*;
import main.java.com.ats.serverpool.network.tcp.server.*;
import java.net.InetAddress;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {            
            //Peer peer = new Peer(InetAddress.getByName(masterIp), 1, InetAddress.getByName("127.0.0.1"), null, 17603);
            PeerManager peerManager;
            UDPServer udpServer = new UDPServer(Utils.getPort());

            TCPServer tcpServer = new TCPServer(Utils.getPort());
            //peerManager = new PeerManager(peer, udpServer);
            //tcpServer.initCallback(peerManager);
            //udpServer.initCallback(peerManager);
            Thread thread = new Thread(tcpServer);
            
            udpServer.start();
            thread.start();
            Thread.currentThread().sleep(2000);
            // ask for next node ip
            // System.out.print("What is the next servers ip? ");
            // String nextIp = scanner.next();
            // nextIp += "\n";
            // scanner.close();
            //PORT can be global
            //TCPClient tcpClient = new TCPClient(InetAddress.getByName("141.117.232.193"), Utils.getPort());
            System.out.println("CONNECTED");
            //peerManager.initTCPClient(tcpClient);
        } catch (Exception e) {

        }
    }
}
