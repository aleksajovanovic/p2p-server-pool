package main.java.com.ats.serverpool.network.tcp.client;

import java.io.*;
import java.net.*;
import com.ats.serverpool.Message;

class TCPClient {
    private Socket socket;

    public TCPClient(InetAddress serverAddr, int serverPort) {
        try {
            this.socket = new Socket(serverAddr, serverPort);
        } catch(Exception e) {
            System.out.println("Error initalizing Socket");
            System.out.println(e.getMessage());
        }
    }

    public void sendMsg(String msg) {
        try {
            InputStream in = this.socket.getInputStream();
            OutputStream out = this.socket.getOutputStream();
            out.write(msg.getBytes());
            out.close();

            BufferedReader buff = new BufferedReader(
                    new InputStreamReader(in));
            
            String received = buff.readLine();
            //do action based on received message
        } catch (Exception e) {
            System.out.println("Error sending msg (TCP)");
            System.out.println(e.getMessage());
        }
    }

    private Message proccessMsg(String string) {
        String[] data = string.split("\n");
        Message msg = new Message(data[0], data[1]);

        return msg;
    }
}