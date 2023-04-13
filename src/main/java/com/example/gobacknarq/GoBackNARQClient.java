package com.example.gobacknarq;
import javafx.scene.chart.PieChart;

import java.io.*;
import java.util.*;
import java.net.*;

public class GoBackNARQClient{
    public static void main(String[] args) throws IOException {
        InetAddress Ip = InetAddress.getByName("localhost");
        int port = 9876;
        DatagramSocket socket = new DatagramSocket();

        byte[] sendData;
        byte[] receiveData = new byte[1024];
        String sentence = "Hello from client";

        int sequenceNum = 0;
        int N = 4; //window size
        int base = 0;
        int nextSeqNum =0;
        int ackR;

        while(true){
            while(nextSeqNum < base + N && nextSeqNum < sentence.length()){
                String datapack = sentence.charAt(nextSeqNum) + "";
                String packseqnum = nextSeqNum + "";
                String packet = packseqnum + datapack;
                sendData = packet.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, Ip, port);
                socket.send(sendPacket);
                System.out.println("Sent packet with sequence number" + nextSeqNum);
                nextSeqNum++;

                socket.setSoTimeout(1000);
                try{
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);
                    String ackPacket = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    ackR = Integer.parseInt(ackPacket);
                    System.out.println("Received acknowledgement for packet with sequence number" + (ackR-1));
                    base = ackR;
                    if(base == sentence.length()){
                        break;
                    }
                }
                catch(SocketTimeoutException e ){
                    System.out.println("Timeout occured");
                    nextSeqNum = base;
                }
                socket.close();


            }
        }

    }
}