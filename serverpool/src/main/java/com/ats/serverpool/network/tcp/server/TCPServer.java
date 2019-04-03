package main.java.com.ats.serverpool.network.tcp.server;

import java.io.*;
import java.net.*;
import com.ats.serverpool.Message;
import main.java.com.ats.serverpool.network.tcp.TCPCallback;

public class TCPServer implements Runnable {
    private static final int BACKLOG = 4;
    private ServerSocket serverSocket;
    private int bindPort;
    private TCPCallback callback;

    public TCPServer(int bindPort) {
        this.bindPort = bindPort;

        try {
            this.serverSocket = new ServerSocket(bindPort, BACKLOG, InetAddress.getLocalHost());
        } catch (Exception e) {
            System.out.println("Error initalizing TCP Socket");
            System.out.println(e.getMessage());
        }
    }
    
    public void run() {
        System.out.println("TCPServer listening on port " + this.bindPort + "...");

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
            new Thread(new ConnectionRunnable(socket, "test message", callback)).start();
        }
    }

    public void initCallback(TCPCallback callback) {
        this.callback = callback;
    }

    private Message proccessMsg(String string) {
        String[] data = string.split("\n");
        Message msg = new Message(data[0], data[1]);

        return msg;
    }

}