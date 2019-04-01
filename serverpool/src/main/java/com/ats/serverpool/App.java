package com.ats.serverpool;

import com.ats.serverpool.network.udp.client.*;
import com.ats.serverpool.network.udp.server.*;
import java.net.InetAddress;

public class App {
    public static void main(String[] args) {
        try {
            Peer p2pNode = new Peer("", 1, InetAddress.getLocalHost(), null, 53);
            Peer serverNode = new Peer("", 1, InetAddress.getLocalHost(), null, 69);
            
            UDPClient client = new UDPClient(p2pNode);
            UDPServer server = new UDPServer(serverNode);
            
            server.run();

            client.sendPacket("init\npoop", InetAddress.getLocalHost(), 69);
            Message msg = client.receive();

            System.out.println(msg.getMessage());
        } catch (Exception e) {

        }
    }
}
