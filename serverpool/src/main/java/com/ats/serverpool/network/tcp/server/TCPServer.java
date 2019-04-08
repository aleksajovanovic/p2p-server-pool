package main.java.com.ats.serverpool.network.tcp.server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import com.ats.serverpool.Message;
import main.java.com.ats.serverpool.network.Callback;
import main.java.com.ats.serverpool.Utils;

public class TCPServer implements Runnable {
    private static final int BACKLOG = 4;
    private ServerSocket serverSocket;
    private int bindPort;
    private Callback callback;
    private HashMap<String, Thread> connections;

    public TCPServer(InetAddress hostIp, int bindPort) {
        this.bindPort = bindPort;
        this.connections = new HashMap<>();

        try {
            this.serverSocket = new ServerSocket(bindPort, BACKLOG, hostIp);
        } catch (Exception e) {
            System.out.println("Error initalizing TCP Socket");
            System.out.println(e.getMessage());
        }
    }
    
    public void run() {
        System.out.println("TCPServer listening on port " + this.bindPort + "...");

        while (true) {
            Socket socket = null;
            String remoteAddress = "";

            try {
                socket = this.serverSocket.accept();
                remoteAddress = socket.getRemoteSocketAddress().toString();
            } catch (Exception e) {
                System.out.println("Error accepting TCP Connection");
                System.out.println(e.getMessage());

                return;
            }

            if (!connections.containsKey(remoteAddress)) {
                System.out.println(remoteAddress);
                Thread connection = new Thread(new ConnectionRunnable(socket, callback));
                connection.setName(remoteAddress);
                connection.start();
                connections.put(remoteAddress, connection);
            }
        }
    }

    public void initCallback(Callback callback) {
        this.callback = callback;
    }
}