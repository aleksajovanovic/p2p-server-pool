package main.java.com.ats.serverpool.network.tcp.client;

import java.io.*;
import java.net.*;
import com.ats.serverpool.Message;

public class TCPClient {
    private Socket socket;

    public TCPClient(String serverAddr, int serverPort) {
        try {
            this.socket = new Socket(serverAddr, serverPort, InetAddress.getByName("141.117.232.192"), serverPort);
        } catch(Exception e) {
            System.out.println("Error initalizing Socket");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
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