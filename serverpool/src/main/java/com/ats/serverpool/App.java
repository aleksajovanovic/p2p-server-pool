package com.ats.serverpool;

import com.ats.serverpool.network.udp.client.*;
import com.ats.serverpool.network.udp.server.*;

import main.java.com.ats.serverpool.PeerManager;
import main.java.com.ats.serverpool.Utils;
import main.java.com.ats.serverpool.network.tcp.client.*;
import main.java.com.ats.serverpool.network.tcp.server.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.print("Is this server a master server? (y/n) ");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next(); 
        
        boolean isMaster = answer.equals("y") ? true : false;
        int id = 1;
        String dht = "";
        
        if (isMaster) {
            System.out.print("\n What is the dht? ");
            dht = scanner.next();
        }
        else {
            System.out.print("\n What is the server id? ");
            id = Integer.valueOf(scanner.next());
        }

        String[] dhtArr = dht.split(",");
        ArrayList<String> dhtArrList = new ArrayList<>();
        
        for (int i = 0; i < dhtArr.length; i++) {
            dhtArrList.add(dhtArr[i]);
        }

        try {            
            Peer peer = new Peer(id, 17604);
            peer.setServerPool(dhtArrList);
            PeerManager peerManager;
            UDPServer udpServer = new UDPServer(Utils.getPort());

            TCPServer tcpServer = new TCPServer(17604);
            peerManager = new PeerManager(peer, udpServer);
            tcpServer.initCallback(peerManager);
            udpServer.initCallback(peerManager);
            Thread thread = new Thread(tcpServer);
            
            // udpServer.start();
            thread.start();
            Thread.currentThread().sleep(2000);

            System.out.print("What is the next servers ip? ");
            String nextIp = scanner.next();
            scanner.close();

            TCPClient tcpClient = new TCPClient(nextIp, 17603);
            System.out.println("CONNECTED");
            peerManager.initTCPClient(tcpClient);
        } catch (Exception e) {

        }
    }
}
