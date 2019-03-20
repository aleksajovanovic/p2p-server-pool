import java.io.*;
import java.net.*;


class UDPServerTest {
    public static void main(String argv[]) throws Exception {

        UDPServer udpServer = new UDPServer(9876);

        // single threaded
        while (true) {

            // save memory space to store received data
            udpServer.newDatagramPacket();

            // wait for packet from client
            udpServer.receivePacket();
        }
    }
}
