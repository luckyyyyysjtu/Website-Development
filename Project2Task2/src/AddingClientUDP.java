/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 6, 2023
 * This program implements a basic UDP Adding Client.
 * The client allows users to enter integer values that are sent to a remote Adding Server.
 * The client receives the server's response, which is typically the result of adding
 * the sent value to a running total on the server. The client can continue to send
 * values until the user enters "halt!" to terminate the client.
 * Usage:
 * 1. Compile and run the program.
 * 2. Enter the server's port number when prompted.
 * 3. Enter integer values to send to the server.
 * 4. The server responds with the result of adding the sent value to the total.
 * 5. To stop the client, enter "halt!".
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

public class AddingClientUDP {
    public static DatagramSocket aSocket = null;
    public static int serverPort;

    public static void main(String args[]) {
        // Announce client is running
        System.out.println("The client is running.");

        // Ask for the port number
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Please enter server port: ");
        serverPort = Integer.parseInt(keyboard.nextLine());

        try {

            aSocket = new DatagramSocket();
            String nextLine;
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));

            // read user input line by line
            while ((nextLine = typed.readLine()) != null) {
                // if user enters halt!, exit client
                if (nextLine.equals("halt!")) {
                    aSocket.close();
                    aSocket = null;
                    System.out.println("Client side quitting");
                    break;
                } else { // else send the value to the server and receive back the total
                    int answer = add(Integer.parseInt(nextLine));
                    // print the received total from server
                    System.out.println("The server returned " + answer + ".");
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

    /**
     * Sends an integer value to the remote Adding Server and receives the server's response.
     *
     * @param i (int): The integer value to send to the server.
     * @return The result received from the server (the updated total).
     * @throws IOException if an I/O error occurs while sending or receiving data.
     */
    public static int add(int i) throws IOException {
        // convert the String of the sent integer to byte array
        byte[] m = String.valueOf(i).getBytes();
        InetAddress aHost = InetAddress.getByName("localhost");
        DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
        // sent to the server
        aSocket.send(request);

        // receive the running total reply from server
        byte[] buffer = new byte[1000];
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        aSocket.receive(reply);

        // build an array to record the reply data with its actual length
        byte[] byteArray = new byte[reply.getLength()];
        System.arraycopy(reply.getData(), 0, byteArray, 0, reply.getLength());
        // form the reply data into a string
        String replyString = new String(byteArray);
        return Integer.parseInt(replyString);
    }
}
