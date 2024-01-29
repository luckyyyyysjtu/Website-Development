/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 6, 2023
 * This program simulates an Eavesdropper UDP application.
 * It listens on a specified port, intercepts messages from clients, and forwards them to a
 * target server while altering specific content in the messages(changing like in string to dislike).
 * The eavesdropper then captures and echoes the server's responses back to the original client.
 * Usage:
 * 1. Compile and run the program.
 * 2. Enter the port number on which the malicious player (eavesdropper) will listen.
 * 3. Enter the port number of the server that the malicious player is masquerading as.
 * 4. The eavesdropper will intercept client messages and replace instances of "like" with "dislike"
 * before forwarding them to the server.
 * 5. The eavesdropper will capture and echo server responses back to the original client.
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EavesdropperUDP {
    public static void main(String args[]) {
        // Announce the EavesdropperUDP is running
        System.out.println("EavesdropperUDP is running.");

        // Ask for the port number on which the malicious player (eavesdropper) will listen
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter the port the malicious player will listen on: ");
        // Store in listenPort
        int listenPort = Integer.parseInt(keyboard.nextLine());
        // Ask for the port number of the server that the malicious player is masquerading as
        System.out.print("Enter the port of the server that the malicious player is masquerading as: ");
        // Store in serverPort
        int serverPort = Integer.parseInt(keyboard.nextLine());

        DatagramSocket aSocket = null;
        byte[] buffer = new byte[1000];

        try {
            // Create a DatagramSocket to listen on the specified port
            aSocket = new DatagramSocket(listenPort);
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);

            while (true) {
                // Receive a client message
                aSocket.receive(request);

                // Extract the message from the received packet
                byte[] byteArray = new byte[request.getLength()];
                // Request data is copied to an array with the correct number of bytes
                System.arraycopy(request.getData(), 0, byteArray, 0, request.getLength());
                String requestString = new String(byteArray);
                // Display the client message
                System.out.println("Client Message: " + requestString);

                // Replace "like" with "dislike" in the message
                // Use regular expression to find the word like
                String regex = "\\blike\\b";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(requestString);
                // Replace with dislike
                String forwardString = matcher.replaceAll("dislike");

                // Forward the modified message to the target server
                byte[] m = forwardString.getBytes();
                DatagramPacket forwardRequest = new DatagramPacket(m,
                        m.length, InetAddress.getByName("localhost"), serverPort);
                aSocket.send(forwardRequest);

                // Receive reply from the server
                buffer = new byte[1000];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(reply);

                // Extract the message from the received reply
                byteArray = new byte[reply.getLength()];
                System.arraycopy(reply.getData(), 0, byteArray, 0, reply.getLength());
                // Display the server message
                System.out.println("Server Message: " + new String(byteArray));

                // Echo the server's response back to the original client
                DatagramPacket forwardReply = new DatagramPacket(byteArray,
                        byteArray.length, request.getAddress(), request.getPort());
                aSocket.send(forwardReply);

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
