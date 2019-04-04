package main.java.com.ats.serverpool.network.tcp.server;

import java.net.*;
import main.java.com.ats.serverpool.network.tcp.TCPCallback;

import java.io.*;

public class ConnectionRunnable implements Runnable {
    private Socket socket;
    private TCPCallback callback;

    public ConnectionRunnable(Socket socket, TCPCallback callback) {
        this.socket = socket;
        this.callback = callback;
    }

    public void run() {
        try {
            // TODO:will have to do actions based on input
            // maybe have a manager class for the dht that will perform
            // record query, insert and update that can be called in here
            // OutputStream out = socket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Exception e) {
            System.out.println("Error accepting TCP Connection");
            System.out.println(e.getMessage());
        }
    }
}