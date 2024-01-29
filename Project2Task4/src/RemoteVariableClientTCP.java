/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 6, 2023
 * This program implements a basic TCP (Transmission Control Protocol) Remote Variable Client.
 * The client allows users to interact with a remote variable server by performing operations
 * such as addition, subtraction, and retrieval of variable values associated with unique IDs.
 * Users can select operations and provide necessary input values through a menu interface.
 * Usage:
 * 1. Compile and run the program.
 * 2. Enter the server's port number when prompted.
 * 3. Use the menu to select the desired operation:
 * - 1: Add a value to your sum
 * - 2: Subtract a value from your sum
 * - 3: Get your sum
 * - 4: Exit the client
 * 4. Follow the on-screen prompts to input values and IDs as required by the selected operation.
 * 5. The client communicates with the remote variable server and displays the operation results.
 * 6. To exit the client, choose option 4 from the menu.
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class RemoteVariableClientTCP {
    public static int serverPort;
    public static Socket clientSocket;

    public static void main(String args[]) {
        // announce client is running
        System.out.println("The client is running.");

        // ask for port number
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Please enter server port: ");
        serverPort = Integer.parseInt(keyboard.nextLine());

        // initialize variables to store user input
        int choice = 0, value = 0, ID = 0, result = 0;

        try {

            // create a socket to connect to the server
            clientSocket = new Socket("localhost", serverPort);


            while (true) {
                // display the menu
                getMenu();
                // get the user choice of operation
                choice = keyboard.nextInt();

                // if choose 1, ask for value to add
                if (choice == 1) {
                    System.out.println("Enter value to add:");
                    value = keyboard.nextInt();
                } else if (choice == 2) { // if choose 2, ask for value to subtract
                    System.out.println("Enter value to subtract:");
                    value = keyboard.nextInt();
                } else if (choice == 4) { // if choose 4, client exit
                    System.out.println("Client side quitting. The remote variable server is still running.");
                    clientSocket.close();
                    clientSocket = null;
                    break;
                }

                // ask for user ID
                System.out.println("Enter your ID:");
                ID = keyboard.nextInt();

                // get result from the server and display
                result = getResult(choice, value, ID);
                System.out.println("The result is: " + result);
            }

        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
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
     * Sends a client request to the remote variable server and receives the server's response.
     *
     * @param choice (int): The selected operation (1: add, 2: subtract, 3: get).
     * @param value  (int): The value to use in the operation (for addition and subtraction).
     * @param ID     (int): The unique identifier for the remote variable.
     * @return The result of the operation received from the server.
     * @throws IOException if an I/O error occurs while sending or receiving data.
     */
    public static int getResult(int choice, int value, int ID) throws IOException {
        // create input and output streams for communication with the server
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
        String packetString;

        // form the string to sent to the server (ID,choice,value(if get: ID, choice))
        if (choice != 3) {
            packetString = ID + "," + choice + "," + value;
        } else {
            packetString = ID + "," + choice;
        }
        // send the request to the server
        out.println(packetString);
        out.flush();
        // receive and process the server's response
        String replyString = in.readLine();
        return Integer.parseInt(replyString);
    }

    /**
     * Displays the menu options for interacting with the remote variable server.
     *
     */
    public static void getMenu() {
        System.out.println();
        System.out.println("1. Add a value to your sum.");
        System.out.println("2. Subtract a value from your sum.");
        System.out.println("3. Get your sum.");
        System.out.println("4. Exit client.");
    }
}
