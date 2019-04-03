package main.java.com.ats.serverpool.network.tcp.server;

import java.net.*;
import main.java.com.ats.serverpool.network.tcp.TCPCallback;
import java.io.*;

public class ConnectionRunnable implements Runnable {
    private Socket socket;
    private String msg;
    private TCPCallback callback;

    public ConnectionRunnable(Socket socket, String msg, TCPCallback callback) {
        this.socket = socket;
        this.msg = msg;
        this.callback = callback;
    }

    public void run() {
        try {
            // TODO:will have to do actions based on input
            // maybe have a manager class for the dht that will perform
            // record query, insert and update that can be called in here
            InputStream in  = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write((this.msg).getBytes());
            callback.insertRecord("rand", "rand");
        } catch (Exception e) {
            System.out.println("Error accepting TCP Connection");
            System.out.println(e.getMessage());
        }
    }
}