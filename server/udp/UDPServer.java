import java.io.*;
import java.net.*;

class UDPServer {
    private DatagramSocket serverSocket;
    private DatagramPacket receivePacket;
    private byte[] receiveData;
    private byte[] sendData;

    UDPServer(int port) {
        this.receiveData = new byte[1024];
        this.sendData = new byte[1024];

        try {
            this.serverSocket = new DatagramSocket(port);
        } catch(SocketException e) {
            System.out.println("Error initalizing DatagramSocket");
            System.out.println(e.getMessage());
        }
    }

    public DatagramSocket getServerSocket() {
        return this.serverSocket;
    }
    public byte[] getRevieveData() {
        return this.receiveData;
    }
    public byte[] getSendData() {
        return this.receiveData;
    }

    // save memory space to received data
    public void newDatagramPacket() {
        this.receivePacket = new DatagramPacket(receiveData, receiveData.length);
    }

    public void receivePacket() {

        try {
            this.serverSocket.receive(this.receivePacket);
            Message msgReceived = processMessageReceived(new String(receivePacket.getData()));
            InetAddress ip = receivePacket.getAddress();
            int port = receivePacket.getPort();

            switch (msgReceived.getMessageType()) {
                case "init":
                    init(msgReceived.getMessage(), ip, port);
                    break;
                case "informAndUpdate":
                    informAndUpdate(msgReceived.getMessage(), ip, port);
                    break;
                case "query":
                    query(msgReceived.getMessage(), ip, port);
                    break;
                case "exit":
                    exit(msgReceived.getMessage(), ip, port);
                    break;
                default:
                    System.out.println("Type of client request not recognized: "  + msgReceived.getMessageType());
                    break;
            }
        } catch(IOException e) {
            System.out.println("Error receiving packet request from client");
            System.out.println(e.getMessage());
        }
    }

    public Message processMessageReceived(String dataReceived) {
        String[] data = dataReceived.split("\n");
        Message msgReceived = new Message(data[0], data[1]);
        return msgReceived;
    }

    public void sendPacket(InetAddress ip, int port) {
        try {
            // create datagram packet to send response to client
            DatagramPacket sendPacket = new DatagramPacket(this.sendData, sendData.length, ip, port);
            serverSocket.send(sendPacket);
        } catch(IOException e) {
            System.out.println("Error sending packet response from server");
            System.out.println(e.getMessage());
        }
    }

    /** each p2p client knows IP address of directory server (ID=1)
     *  starting with this IP the p2p client needs to ask DHT for
     *  IP addresses of remaining servers and get them.
     */
    private void init(String msg, InetAddress ip, int port) {
         String response = "init\nlist of DHT servers";
         this.sendData = response.getBytes();
         sendPacket(ip, port);
    }

    /** p2p client needs to perdorm hashing of content name into server id
     *  contact target server to store the recorde (content name, client IP)
     *  keep the local recorde (content name, DHT server, server' IP)
     */
    private void informAndUpdate(String msg, InetAddress ip, int port) {
        String response = "informAndUpdate\nnew foto added to DHT";
        this.sendData = response.getBytes();
        sendPacket(ip, port);
    }
    
    /** requires p2p client to:
     *  - perform the hashing of the content's name into server id
     *  - contact server DHT to find the IP of client with required content name
     *    (after init all IP addresses of servers in DHT are known)
     *  - if content does not exist in the network DHTm return code "404 content not found"
    */
    private void query(String msg, InetAddress ip, int port) {
        String response = "query\nlist of ip addresses with content received";
        this.sendData = response.getBytes();
        sendPacket(ip, port);
    }

    /** p2p client request has to be dispersed across all servers in DHT
     *  inform them to remove the records related to the contnet stored 
     *  DHT server chosen as the entry can be any of the 4 and request
     *  as to be passed over the ring to delete all the recorded owned
     *  by the client who wants to exist
     */
    private void exit(String msg, InetAddress ip, int port) {
        String response = "exit\nPeer successfully removed";
        this.sendData = response.getBytes();
        sendPacket(ip, port);
    }
}