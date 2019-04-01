package main.java.com.ats.serverpool.network.tcp.server;

import java.io.*;
import java.net.*;
import com.ats.serverpool.Peer;
import com.ats.serverpool.Message;

class TCPServer implements Runnable {
    private static final int BACKLOG = 4;
    private ServerSocket serverSocket;
    private Peer peer;

    public TCPServer(Peer peer) {
        this.peer = peer;
        
        try {
            this.serverSocket = new ServerSocket(this.peer.getPort(), BACKLOG, InetAddress.getLocalHost());
        } catch (Exception e) {
            System.out.println("Error initalizing TCP Socket");
            System.out.println(e.getMessage());
        }
    }
    
    public void run() {
        while (true) {
            Socket socket = null;

            try {
                socket = this.serverSocket.accept();
            } catch (Exception e) {
                System.out.println("Error accepting TCP Connection");
                System.out.println(e.getMessage());

                return;
            }
            // the test message will have to be a global later 
            // on so that we can pass records and such
            new Thread(new ConnectionRunnable(socket, "test message")).start();
        }
    }

    private Message proccessMsg(String string) {
        String[] data = string.split("\n");
        Message msg = new Message(data[0], data[1]);

        return msg;
    }

}