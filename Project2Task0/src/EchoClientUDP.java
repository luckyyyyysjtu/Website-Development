/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 6, 2023
 * This program implements a basic UDP Echo Client that sends user-entered messages
 * to a UDP Echo Server running on the same machine ("localhost").
 * The client can send messages to the server, and the server echoes them back.
 * To stop the client, enter "halt!" as a message.
 * Usage:
 * - Compile and run the program.
 * - Enter the desired server port number when prompted.
 * - Enter messages to send to the server.
 * - To stop the client, enter "halt!".
 */

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class EchoClientUDP {
    public static void main(String args[]) {
        // Announce the client is running
        System.out.println("The UDP client is running.");
        // Initialize a DatagramSocket for communication
        DatagramSocket aSocket = null;

        // prompt user to enter the port number, user the scanner to read input
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter the port number: ");
        int serverPort = Integer.parseInt(keyboard.nextLine());

        try {
            // Resolve the server hostname (In this case, it's "localhost")
            InetAddress aHost = InetAddress.getByName("localhost");
            // Create a DatagramSocket for sending and receiving data
            aSocket = new DatagramSocket();
            String nextLine;
            // Read input from the user
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));

            while ((nextLine = typed.readLine()) != null) {
                // Convert the user input to bytes
                byte[] m = nextLine.getBytes();
                // Create a DatagramPacket with the message to send
                DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
                // Send the packet to the server
                aSocket.send(request);

                // Prepare a buffer for receiving the server's reply
                byte[] buffer = new byte[1000];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                // Receive the server's reply
                aSocket.receive(reply);

                // Extract the message from the received packet
                byte[] byteArray = new byte[reply.getLength()];
                // Reply data is copied to an array with the correct number of bytes
                System.arraycopy(reply.getData(), 0, byteArray, 0, reply.getLength());

                // Check if the message is a termination command
                // if yes, exit
                if (new String(byteArray).equals("halt!")) {
                    aSocket.close();
                    aSocket = null;
                    System.out.println("Reply from server: " + new String(byteArray));
                    System.out.println("UDP Client side quitting");
                    break;
                } else { // if no, display the server's reply
                    System.out.println("Reply from server: " + new String(byteArray));
                }

            }

        } catch (SocketException e) {
            System.out.println("Socket Exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        } finally {
            if (aSocket != null) aSocket.close();
        }
    }
}