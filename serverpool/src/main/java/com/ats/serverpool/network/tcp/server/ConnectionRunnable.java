package main.java.com.ats.serverpool.network.tcp.server;

import java.net.*;
import com.ats.serverpool.Message;
import main.java.com.ats.serverpool.network.tcp.TCPCallback;
import main.java.com.ats.serverpool.Utils;
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
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                Message msg = Utils.proccessMsg(line);
                action(msg);
            }

        } catch (Exception e) {
            System.out.println("Error accepting TCP Connection");
            System.out.println(e.getMessage());
        }
    }

    private void action(Message msg) {
        String messageType = msg.getMessageType();
        String message[] = msg.getMessage().split(",");
        String tcpMsg;

        switch (messageType) {
            case "insert":
                //pass if not correct hash
                callback.insertRecord(message[0], message[1]);
                break;

            case "remove":
                //only remove if hash says its here
                if (callback.recordExists(message[0]))
                    callback.removeRecord(message[0], message[1]);
                break;

            case "query":
                if (callback.recordExists(message[0])) {
                    tcpMsg = "pass%" + callback.getRecord(message[0]) + "," + message[1] + "," + message[2] + "," + message[3];
                }
                else {
                    tcpMsg = "query%" + callback.getRecord(message[0]) + "," + message[1] + "," + message[2] + "," + message[3];
                }  

                callback.tcpSendMsg(tcpMsg);

                break;

            case "pass":
                if (message[1].equals(String.valueOf(callback.getPeerId()))) {
                    String recordLocation = callback.getRecord(message[0]);
                    int port = Integer.valueOf(message[3]);

                    try {
                        InetAddress p2pNodeAddress = InetAddress.getByName(message[2]);
                        callback.udpRespond(recordLocation, p2pNodeAddress, port);
                    } catch (Exception e) {
                        System.out.println("Error parsing node address");
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    tcpMsg = "pass%" + callback.getRecord(message[0]) + "," + message[1] + "," + message[2] + "," + message[3];
                    callback.tcpSendMsg(tcpMsg);
                }
    
                break;
        }
    }
}