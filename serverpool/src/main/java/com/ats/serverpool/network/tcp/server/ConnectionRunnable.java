package main.java.com.ats.serverpool.network.tcp.server;

import java.net.*;
import java.io.*;

public class ConnectionRunnable implements Runnable {
    private Socket socket;
    private String msg;

    public ConnectionRunnable(Socket socket, String msg) {
        this.socket = socket;
        this.msg = msg;
    }

    public void run() {
        try {
            // TODO:will have to do actions based on input
            // maybe have a manager class for the dht that will perform
            // record query, insert and update that can be called in here
            InputStream in  = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write((this.msg).getBytes());
            out.close();
            in.close();
        } catch (Exception e) {
            System.out.println("Error accepting TCP Connection");
            System.out.println(e.getMessage());
        }
    }
}