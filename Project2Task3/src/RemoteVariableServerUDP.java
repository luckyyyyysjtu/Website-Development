/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 6, 2023
 * This Java program implements a basic UDP (User Datagram Protocol) Remote Variable Server.
 * The server listens on a specified port and handles requests from clients to perform
 * various operations on remote variables identified by unique IDs. Supported operations
 * include addition, subtraction, and retrieval of variable values. The server maintains a
 * collection of variables and their current values using a TreeMap.
 * Usage:
 * 1. Compile and run the program.
 * 2. Enter the desired server port number when prompted.
 * 3. The server starts listening on the specified port and waits for client requests.
 * 4. Clients can send requests in the form of "ID,operation,value", where:
 * - ID: Unique identifier.
 * - operation: Numeric code for the operation (1: addition, 2: subtraction, 3: retrieval).
 * - value: The value to be used in the operation (for addition and subtraction).
 * 5. The server processes the requests, updates variable values, and returns results to clients.
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.util.TreeMap;

public class RemoteVariableServerUDP {
    // treemap used to store the values of a specific ID
    static TreeMap<Integer, Integer> sums = new TreeMap<Integer, Integer>();
    public static DatagramSocket aSocket = null;
    public static int port;
    public static InetAddress inetAddress = null;


    public static void main(String args[]) {
        // announce server started
        System.out.println("Server started");

        // ask for the port number
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter the port number: ");
        int serverPort = Integer.parseInt(keyboard.nextLine());


        try {
            aSocket = new DatagramSocket(serverPort);

            while (true) {

                // receive the request string from user in the format of "ID,operation,value" ("ID,operation" if using get)
                String requestString = receive();

                // retrieve the values from the request string
                String[] packetString = requestString.split(",");
                int ID = Integer.parseInt(packetString[0]);
                int choice = Integer.parseInt(packetString[1]);
                int value = 0;
                // if using get, let the value to be default 0
                // else parse value from string
                try {
                    value = Integer.parseInt(packetString[2]);
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }

                // used to form operation details
                String[] operation = {"add", "subtract", "get"};

                // if ID not seen before, initialize the ID in treemap with 0 as original value
                if (!sums.containsKey(ID)) {
                    sums.put(ID, 0);
                }

                // get original value before operation
                int original = sums.get(ID);
                // get result after operation
                int result = getResult(choice, value, ID);

                // if not get, display the result
                if (choice != 3) {
                    System.out.println("ID: " + ID + ", Operation: " + original + " " + operation[choice - 1] + " " + value + ", Result: " + result);
                } else { // if get, display the result
                    System.out.println("ID: " + ID + ", Operation: " + operation[choice - 1] + ", Result: " + result);
                }

                // send result back to the client
                send(String.valueOf(result));
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } finally {
            if (aSocket != null) aSocket.close();
        }
    }

    /**
     * Performs the requested operation on the variable identified by the given ID.
     *
     * @param choice (int): Numeric code for the operation (1: addition, 2: subtraction, 3: retrieval).
     * @param value  (int): The value to be used in the operation (for addition and subtraction).
     * @param ID     (int): Unique identifier for the variable.
     * @return The result of the operation on the variable.
     */
    public static int getResult(int choice, int value, int ID) {
        int sum;

        // get the current value
        sum = sums.get(ID);

        // if add or subtract, do the operation and update the map
        switch (choice) {
            case 1 -> {
                sum += value;
                sums.replace(ID, sum);
            }
            case 2 -> {
                sum -= value;
                sums.replace(ID, sum);
            }
        }
        // return the result
        return sum;
    }

    /**
     * Receives a client request in the form of a string and extracts relevant information.
     *
     * @return The received client request as a string.
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
     * Sends a message (the updated value) to a client.
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
