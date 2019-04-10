package main.java.com.ats.serverpool.network.tcp.server;

import java.net.*;
import com.ats.serverpool.Message;
import main.java.com.ats.serverpool.network.Callback;
import main.java.com.ats.serverpool.network.tcp.client.TCPClient;
import main.java.com.ats.serverpool.Utils;
import java.io.*;

public class ConnectionRunnable implements Runnable {
    private Socket socket;
    private Callback callback;
    private final int NUMBER_OF_SERVERS;

    public ConnectionRunnable(Socket socket, Callback callback) {
        this.socket = socket;
        this.callback = callback;
        this.NUMBER_OF_SERVERS = callback.getServerPoolCount();
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

        } catch (IOException e) {
            System.out.println(e.getCause().toString());
            System.out.println(e.getMessage());
        }
    }

    private void action(Message msg) {
        String messageType = msg.getMessageType();
        String message[] = msg.getMessage().split(",");
        String tcpMsg;

        // check count here
        // if (!messageType.equals("insert")) {
        //     int count = messageType.equals("remove") ? Integer.valueOf(message[2]) : Integer.valueOf(message[4]);

        //     if (count == callback.getServerPoolCount()) {
        //         return;
        //     }
        // }

        switch (messageType) {
            case "insert":
                if ((Utils.hash(message[0]) % NUMBER_OF_SERVERS + 1) == callback.getPeerId()) {
                    System.out.println("Record inserted at peer " + callback.getPeerId());
                    callback.insertRecord(message[0], message[1]);
                    tcpMsg = "ok%" + message[2] + "," + message[1] + "," + message[3];
                    callback.tcpSendMsg(tcpMsg);
                }
                else {
                    tcpMsg = messageType + "%" + message[0] + "," + message[1];
                    callback.tcpSendMsg(tcpMsg);;
                }

                break;

            case "remove":
                if ((Utils.hash(message[0]) % NUMBER_OF_SERVERS + 1) == callback.getPeerId() && callback.recordExists(message[0])) {
                    System.out.println("Record removed at peer " + callback.getPeerId());
                    callback.removeRecord(message[0], message[1]);
                }
                else {
                    tcpMsg = messageType + "%" + message[0] + "," + message[1] + "," + message[3];
                    callback.tcpSendMsg(tcpMsg);
                }

                break;

            case "query":
                if (callback.recordExists(message[0])) {
                    System.out.println("Record found at peer " + callback.getPeerId());
                    
                    int requestorId = Integer.valueOf(message[1]);

                    if (requestorId == callback.getPeerId()) {
                        int port = Integer.valueOf(message[3]);

                        try {
                            InetAddress p2pNodeAddress = InetAddress.getByName(message[2].substring(1));
                            callback.udpRespond("query%ERR", p2pNodeAddress, port);
                            System.out.println("UDP response at peer " + callback.getPeerId());
                        } catch (Exception e) {
                            System.out.println("Error parsing node address");
                            System.out.println(e.getMessage());
                        }
                    }
                    else {
                        tcpMsg = "pass%" + callback.getRecord(message[0]) + "," + message[1] + "," + message[2] + "," + message[3];
                        callback.tcpSendMsg(tcpMsg);
                    }
                }
                else {
                    tcpMsg = messageType + "%" + callback.getRecord(message[0]) + "," + message[1] + "," + message[2] + "," + message[3];
                    callback.tcpSendMsg(tcpMsg);
                }  

                break;

            case "pass":
                if (message[1].equals(String.valueOf(callback.getPeerId()))) {
                    String recordLocation = callback.getRecord(message[0]);
                    int port = Integer.valueOf(message[3]);

                    try {
                        InetAddress p2pNodeAddress = InetAddress.getByName(message[2].substring(1));
                        callback.udpRespond(recordLocation, p2pNodeAddress, port);
                        System.out.println("UDP response at peer " + callback.getPeerId());
                    } catch (Exception e) {
                        System.out.println("Error parsing node address");
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    tcpMsg = messageType + "%" + callback.getRecord(message[0]) + "," + message[1] + "," + message[2] + "," + message[3];
                    callback.tcpSendMsg(tcpMsg);
                }
    
                break;
        
            case "ok":
                if (message[0].equals(String.valueOf(callback.getPeerId()))) {
                    int port = Integer.valueOf(message[2]);

                    try {
                        InetAddress p2pNodeAddress = InetAddress.getByName(message[1].substring(1));
                        callback.udpRespond("informAndUpdate%OK", p2pNodeAddress, port);
                    } catch (Exception e) {
                        System.out.println("Error parsing node address");
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    tcpMsg = messageType + "%" + message[0] + "," + message[1] + "," + message[2];
                    callback.tcpSendMsg(tcpMsg);
                }
                break;
        }
    }
}