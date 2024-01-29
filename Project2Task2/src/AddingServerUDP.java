/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 6, 2023
 * This program implements a basic UDP Adding Server.
 * The server listens on a specified port, receives integer values from clients,
 * adds them to a running total, and returns the updated total to the clients.
 * The server continues to run indefinitely, processing client requests.
 * Usage:
 * 1. Compile and run the program.
 * 2. Enter the desired server port number when prompted.
 * 3. The server will start listening on the specified port.
 * 4. Clients can send integer values to the server.
 * 5. The server adds the received values to a running total and returns the updated total to clients.
 * 6. The server continues to process client requests.
 */

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class AddingServerUDP {
    public static int total = 0;
    public static int serverPort;
    public static DatagramSocket aSocket = null;
    public static int port;
    public static InetAddress inetAddress = null;

    public static void main(String args[]) {
        // Announce server started
        System.out.println("Server started");

        // Let user enter the port number
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter the port number: ");
        serverPort = Integer.parseInt(keyboard.nextLine());


        try {
            // Create a DatagramSocket to listen on the specified port
            aSocket = new DatagramSocket(serverPort);

            while (true) {

                // Receive a value from a client
                String requestString = receive();

                // Add the received value to the running total
                System.out.println("Adding: " + requestString + " to " + total);
                add(Integer.parseInt(requestString));

                // Return the updated total to the client
                System.out.println("Returning sum of " + total + " to client");
                send(String.valueOf(total));
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } finally {
            if (aSocket != null) aSocket.close();
        }
    }

    /**
     * Adds the given integer value to the running total.
     *
     * @param i (int): The integer value to add to the total.
     */
    public static void add(int i) {
        total += i;
    }

    /**
     * Receives an integer value as a message from a client.
     * Extracts the message from the received DatagramPacket.
     *
     * @return A String representation of the received integer value.
     */
    public static String receive() {
        byte[] buffer = new byte[1000];
        DatagramPacket request = new DatagramPacket(buffer, buffer.length);
        try {
            aSocket.receive(request);
            port = request.getPort();
            inetAddress = request.getAddress();

        } catch (SocketException e) {
            System.out.println("Socket error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error " + e.getMessage());
        }

        // Extract message from the request
        byte[] byteArray = new byte[request.getLength()];
        System.arraycopy(request.getData(), 0, byteArray, 0, request.getLength());

        // return the String representation fo the request
        return new String(byteArray);
    }

    /**
     * Sends a message (the updated total) to a client.
     *
     * @param message (String): The message to send to the client.
     */
    public static void send(String message) {
        byte[] m = message.getBytes();
        DatagramPacket packet = new DatagramPacket(m, m.length, inetAddress, port);
        try {
            // send the message to the client as bytes array
            aSocket.send(packet);
        } catch (SocketException e) {
            System.out.println("Socket error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error " + e.getMessage());
        }

    }
}
