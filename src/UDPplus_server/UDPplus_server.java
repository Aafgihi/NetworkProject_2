package UDPplus_server;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.lang.*;
import java.util.*;

class UDPplus_server 
{

    public static void main(String args[]) throws Exception 
    {

        //Full Name + AccNumber  in Array
        String[] Client = {"Abdullah Ali", "15324", "Manal Abdullah", "90781",
            "Henry Markos", "88125", "Hisham Mansoor", "62044",
            "Asma Awal", "71825", "Osama Ahmed", "12818",
            "Alice Tarkood", "29502", "Mohammed Khalid", "19012",};
        String[] Handshak = {"Test"};

        DatagramSocket serverSocket = new DatagramSocket(9152);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        int index = 0;

        Random rand = new Random();
        //To make sure not to enter Down, only in Handshaking.
        int t = 1;

        while (true) 
        {

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData());

            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            //  Here, decrypt the name in the server.
            byte[] strAsByteArray = sentence.getBytes();
            byte[] result = new byte[strAsByteArray.length];
            for (int m = 0; m < strAsByteArray.length; m++) {
                result[m] = strAsByteArray[strAsByteArray.length - m - 1];
            }
            String message = new String(result);

            // Here is the case of the server if it is greater than 20 the good, if less than 20 becomes a busy state.
            int n = rand.nextInt(100) + 1;
            // Here is the case of the server if it is greater than 10 the good, if less than 10 becomes a time out.
            int a = rand.nextInt(100) + 1;

            if (n > 20 && a > 10) 
            {

                // Here work is loop to Client array.
                for (int i = 0; i <= Client.length - 1; i++) 
                {
                    if (message.contains(Client[i])) 
                    {
                        index = i;
                        String ClientAcc = Client[index + 1];

                        //Here, encrypted the account number in the server.
                        strAsByteArray = ClientAcc.getBytes();
                        result = new byte[strAsByteArray.length];
                        
                        for (int m = 0; m < strAsByteArray.length; m++) 
                        {
                            result[m] = strAsByteArray[strAsByteArray.length - m - 1];
                        }
                        ClientAcc = new String(result);
                        //Send the account number encrypted
                        sendData = ClientAcc.getBytes();

                        //"break" for Stand if you find the number and get out
                        break;
                    } 
                    else 
                    {
                        //If you do not find the required return 00000
                        String NoAcc = "00000";
                        sendData = NoAcc.getBytes();
                    }

                }
                //If the sender of the Client word "Test" in Index 0 returned to him this sentence that the handshake was done
                if (sentence.contains(Handshak[0])) 
                {
                    String Max = ("Handshak OK .. maximum number of messages server can handle is 1024");
                    sendData = Max.getBytes();
                }
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);

                //If the sender of the Clint word "Test" in Index 0 returned to him this sentence that the handshake was done,
                //with "Time out less than or equal 10" AND Server getter than  20%.
            } 
            else if (n > 20 && a <= 10) 
            {
                if (sentence.contains(Handshak[0])) 
                {
                    String Max = ("Handshak OK .. maximum number of messages server can handle is 1024");
                    sendData = Max.getBytes();

                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    serverSocket.send(sendPacket);
                    //Changed "t" to zero to ensure that he did not go the "else if".
                    t = 0;
                }
                //Go here if the server status is busy less than or equal to 20% and Time Out is greater than 10
                //"t" Make sure you do not go here except for the start of the handshake only
            } 
            else if (n <= 20 && a > 10 && t == 1) 
            {
                String Max = (" -1 ");
                sendData = Max.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            }

        }

    }
}
