/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 6, 2023
 * This Java program implements a TCP-based remote variable server that allows clients
 * to perform operations (addition, subtraction and get) on variable sums associated with unique IDs.
 * The server listens for incoming client connections and handles client requests to perform
 * * various operations on remote variables identified by unique IDs. Supported operations
 * * include addition, subtraction, and retrieval of variable values. The server maintains a
 * * collection of variables and their current values using a TreeMap.
 * Usage:
 * 1. Compile and run the program.
 * 2. Enter the desired server port number when prompted.
 * 3. The server listens for incoming client connections and handles client requests.
 * 4. Clients can send requests in the form of "ID,operation,value", where:
 * - ID: Unique identifier.
 * - operation: Numeric code for the operation (1: addition, 2: subtraction, 3: retrieval).
 * - value: The value to be used in the operation (for addition and subtraction).
 * 5. The server processes the requests, updates variable values, and returns results to clients.
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.TreeMap;

public class RemoteVariableServerTCP {
    static TreeMap<Integer, Integer> sums = new TreeMap<Integer, Integer>();
    static Scanner in;
    static PrintWriter out;


    public static void main(String args[]) {
        // announce server started
        System.out.println("Server started");

        // ask for port number
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter the port number: ");
        int serverPort = Integer.parseInt(keyboard.nextLine());

        Socket clientSocket = null;


        try {
            // create a ServerSocket to listen for incoming connections
            ServerSocket listenSocket = new ServerSocket(serverPort);
            clientSocket = listenSocket.accept();
            // initialize input and output streams for communication with the client
            in = new Scanner(clientSocket.getInputStream());
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

            while (true) {

                // if client exit, keep the server running by accepting a new client connection
                if (!in.hasNextLine()) {
                    clientSocket = listenSocket.accept();
                    in = new Scanner(clientSocket.getInputStream());
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
                    continue;
                }



                String requestString = receive();
                // retrieve ID, choice and value from the client request
                String[] packetString = requestString.split(",");
                int ID = Integer.parseInt(packetString[0]);
                int choice = Integer.parseInt(packetString[1]);
                int value = 0;
                try {
                    value = Integer.parseInt(packetString[2]);
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }

                String[] operation = {"add", "subtract", "get"};

                // if ID not seen before, add the ID to the treemap with 0 as original value
                if (!sums.containsKey(ID)) {
                    sums.put(ID, 0);
                }
                // get the value before calculation
                int original = sums.get(ID);
                // calculate the result
                int result = getResult(choice, value, ID);

                // format the logs for calculation
                if (choice != 3) {
                    System.out.println("ID: " + ID + ", Operation: " + original + " " + operation[choice - 1] + " " + value + ", Result: " + result);
                } else {
                    System.out.println("ID: " + ID + ", Operation: " + operation[choice - 1] + ", Result: " + result);
                }

                // send the result to the client
                send(String.valueOf(result));
            }
            // Handle exceptions
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());

            // If quitting (typically by you sending quit signal) clean up sockets
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                // ignore exception on close
            }
        }
    }

    /**
     * getResult - Perform operations on variable sums based on client requests.
     *
     * @param choice The operation choice (1 for addition, 2 for subtraction, 3 for retrieval).
     * @param value  The value to be added or subtracted.
     * @param ID     The unique ID associated with the variable sum.
     * @return The updated variable sum.
     */
    public static int getResult(int choice, int value, int ID) {
        int sum;

        // get original value of the ID
        sum = sums.get(ID);
        // if choice = add or subtract, do operation and update the value in treemap
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
        // return the updated value
        return sum;
    }


    /**
     * send - Send a message to the client.
     *
     * @param message The message to be sent.
     */
    public static void send(String message) {
        out.println(message);
        out.flush();
    }

    /**
     * receive - Receive a request from the client.
     *
     * @return The received message as a String.
     */
    public static String receive() {
        return in.nextLine();
    }

}
