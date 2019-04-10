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
                System.out.println("TCP being received: " + line);
                Message msg = Utils.proccessMsg(line);
                action(msg);
            }

        } catch (IOException e) {
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
                    System.out.println("INSERT TCP CALL");
                    tcpMsg = messageType + "%" + message[0] + "," + message[1];
                    callback.tcpSendMsg(tcpMsg);;
                }

                break;

            case "query":
                if (callback.recordExists(message[0])) {
                    System.out.println("Record found at peer " + callback.getPeerId());
                    
                    int requestorId = Integer.valueOf(message[2]);

                    if (requestorId == callback.getPeerId()) {
                        int port = Integer.valueOf(message[2]);

                        try {
                            InetAddress p2pNodeAddress = InetAddress.getByName(message[1].substring(1));
                            callback.udpRespond("query%ERR", p2pNodeAddress, port);
                            System.out.println("UDP response at peer " + callback.getPeerId());
                        } catch (Exception e) {
                            System.out.println("Error parsing node address");
                            System.out.println(e.getMessage());
                        }
                    }
                    else {
                        System.out.println("QUERY&PASS TCP CALL");
                        tcpMsg = "pass%" + message[0] + "," + message[1] + "," + message[2] + "," + message[3];
                        callback.tcpSendMsg(tcpMsg);
                    }
                }
                else {
                    if (Integer.valueOf(message[2]) == callback.getPeerId()) {
                        int port = Integer.valueOf(message[2]);

                        try {
                            InetAddress p2pNodeAddress = InetAddress.getByName(message[1].substring(1));
                            callback.udpRespond("query%ERR", p2pNodeAddress, port);
                            System.out.println("UDP response at peer " + callback.getPeerId());
                        } catch (Exception e) {
                            System.out.println("Error parsing node address");
                            System.out.println(e.getMessage());
                        }
                    }
                    else {
                        System.out.println("QUERY TCP CALL");
                        tcpMsg = messageType + "%" + message[0] + "," + message[1] + "," + message[2] + "," + message[3];
                        callback.tcpSendMsg(tcpMsg);
                    }
                }  

                break;

            case "pass":
                if (Integer.valueOf(message[2]) == callback.getPeerId()) {
                    String recordLocation = message[1];
                    int port = Integer.valueOf(message[3]);

                    try {
                        InetAddress p2pNodeAddress = InetAddress.getByName(message[1].substring(1));
                        callback.udpRespond("query%OK," + recordLocation, p2pNodeAddress, port);
                        System.out.println("UDP response at peer " + callback.getPeerId());
                    } catch (Exception e) {
                        System.out.println("Error parsing node address");
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    System.out.println("PASS TCP CALL");
                    tcpMsg = messageType + "%" + message[0] + "," + message[1] + "," + message[2] + "," + message[3];
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
                    System.out.println("OK TCP CALL");
                    tcpMsg = messageType + "%" + message[0] + "," + message[1] + "," + message[2];
                    callback.tcpSendMsg(tcpMsg);
                }
                break;

            case "exit": 
                if (Integer.valueOf(message[0]) == callback.getPeerId()) {
                    int port = Integer.valueOf(message[2]);

                    try {
                        InetAddress p2pNodeAddress = InetAddress.getByName(message[1].substring(1));
                        callback.udpRespond("exit%OK", p2pNodeAddress, port);
                    } catch (Exception e) {
                        System.out.println("Error parsing node address");
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    System.out.println("Exiting user " + message[1] + " from peer " + callback.getPeerId());
                    callback.exit(message[1]);
                    System.out.println("Keys in hashtable AFTER EXIT are: " + callback.getRecordTable().entrySet());
                    callback.tcpSendMsg("exit%" + message[0] + "," + message[1] + "," + message[2]);
                }

                break;
        }
    }
}