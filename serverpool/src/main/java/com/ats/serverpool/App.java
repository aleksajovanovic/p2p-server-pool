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
        System.out.print("Is this server a master server? (y/n) ");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        
        boolean isMaster = answer.equals("y") ? true : false;
        String masterIp = "";
        
        if (!isMaster) {
            System.out.print("\n What is the master servers ip? ");
            masterIp = scanner.next();
        }

        try {            
            Peer peer = new Peer(InetAddress.getByName(masterIp), 1, InetAddress.getByName("127.0.0.1"), null, 17603);
            PeerManager peerManager;
            UDPServer udpServer = new UDPServer(Utils.getPort());
            
            TCPServer tcpServer = new TCPServer(Utils.getPort());

            // ask for next node ip
            System.out.print("What is the next servers ip? ");
            String nextIp = scanner.next();
            //PORT can be global
            TCPClient tcpClient = new TCPClient(InetAddress.getByName(nextIp), Utils.getPort());
            peerManager = new PeerManager(peer, tcpClient, udpServer);
            tcpServer.initCallback(peerManager);
            udpServer.initCallback(peerManager);
            Thread thread = new Thread(tcpServer);
            
            udpServer.start();
            thread.start();
        
            tcpClient.sendMsg("poop");
            tcpClient.sendMsg("on");
            tcpClient.sendMsg("you");

        } catch (Exception e) {

        }
    }
}
