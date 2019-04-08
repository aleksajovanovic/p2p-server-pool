package main.java.com.ats.serverpool.network.tcp.client;

import java.io.*;
import java.net.*;
import com.ats.serverpool.Message;

public class TCPClient {
    private Socket socket;

    public TCPClient(InetAddress serverAddr, int serverPort) {
        try {
            System.out.println(serverAddr);
            System.out.println(serverPort);
            this.socket = new Socket(serverAddr, serverPort);
        } catch(Exception e) {
            System.out.println("Error initalizing Socket");
            System.out.println(e.getMessage());
        }
    }

    public void sendMsg(String msg) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(msg);
        } catch (Exception e) {
            System.out.println("Error sending msg (TCP)");
            System.out.println(e.getMessage());
        }
    }
}