package main.java.com.ats.serverpool.network.tcp.client;

import java.io.*;
import java.net.*;
import com.ats.serverpool.Message;

public class TCPClient {
    private Socket socket;

    public TCPClient(String serverAddr, int serverPort) {
        try {
            this.socket = new Socket(serverAddr, serverPort);
        } catch(Exception e) {
            System.out.println("Error initalizing Socket");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
    }

    public void sendMsg(String msg) {
        try {
            System.out.println("TCP msg being sent: " + msg);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(msg);
        } catch (Exception e) {
            System.out.println("Error sending msg (TCP)");
            System.out.println(e.getMessage());
        }
    }
}