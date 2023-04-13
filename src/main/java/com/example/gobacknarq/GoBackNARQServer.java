package com.example.gobacknarq;
import java.net.*;
import java.io.*;
public class GoBackNARQServer {
    public static void main(String[] args) throws Exception {

        int port = 9876;
        DatagramSocket socket = new DatagramSocket(port);

        byte[] receiveData = new byte[1024];
        byte[] sendData;
        int expectedSequenceNumber = 0;
        int N = 4;  // Window size

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            String packetData = new String(receivePacket.getData(), 0, receivePacket.getLength());
            int sequenceNumber = Integer.parseInt(packetData.substring(0, 1));
            String message = packetData.substring(1);

            if (sequenceNumber == expectedSequenceNumber) {
                System.out.println("Received packet with sequence number " + sequenceNumber + ": " + message);
                expectedSequenceNumber++;

                String ackPacket = (sequenceNumber + 1) + "";
                sendData = ackPacket.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                socket.send(sendPacket);
                System.out.println("Sent ACK for packet with sequence number " + sequenceNumber);

                if (expectedSequenceNumber == N) {
                    expectedSequenceNumber = 0;
                }
            } else {
                System.out.println("Discarded packet with sequence number " + sequenceNumber);
            }
        }
    }
}


