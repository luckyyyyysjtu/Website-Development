/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 6, 2023
 * This program implements a basic UDP Echo Server that listens on a specified port and
 * echoes back any messages it receives from clients. The server continues to run
 * until a special command ("halt!") is sent, at which point it terminates.
 * Usage:
 * - Compile and run the program.
 * - Enter the desired port number when prompted.
 * - The server will start listening on the specified port.
 * - Clients can send messages to the server, and it will echo them back.
 * - To stop the server, send the command "halt!" from a client.
 */

import java.net.*;
import java.io.*;
import java.util.Scanner;

// Cite: Code based on EchoServerUDP.java
public class EchoServerUDP {
    public static void main(String args[]) {
        // Announce the server is running
        System.out.println("The UDP server is running.");

        // prompt user to enter the port number, use the scanner to read input
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter the port number: ");
        int serverPort = Integer.parseInt(keyboard.nextLine());

        DatagramSocket aSocket = null;
        byte[] buffer = new byte[1000];
        try {
            // Create a DatagramSocket and bind it to the specified port
            aSocket = new DatagramSocket(serverPort);
            // Create a DatagramPacket to receive client requests
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            while (true) {
                // Receive a client request
                aSocket.receive(request);
                // Create a DatagramPacket to send the reply back to the client
                DatagramPacket reply = new DatagramPacket(request.getData(),
                        request.getLength(), request.getAddress(), request.getPort());

                // Extract the message from the received packet
                byte[] byteArray = new byte[request.getLength()];
                // Request data is copied to an array with the correct number of bytes
                System.arraycopy(request.getData(), 0, byteArray, 0, request.getLength());
                // Form a string of the request using the byte array
                String requestString = new String(byteArray);

                // if the request is halt!, respond to the client with "halt!" and then exit
                if (requestString.equals("halt!")) {
                    aSocket.send(reply);
                    aSocket.close();
                    aSocket = null;
                    System.out.println("Echoing: " + requestString);
                    System.out.println("UDP Server side quitting");
                    break;
                } else { // else echo back the request data
                    System.out.println("Echoing: " + requestString);
                    aSocket.send(reply);
                }
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null) aSocket.close();
        }
    }
}
