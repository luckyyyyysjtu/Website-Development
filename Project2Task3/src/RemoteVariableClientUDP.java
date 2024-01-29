/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 6, 2023
 * This program implements a basic UDP (User Datagram Protocol) Remote Variable Client.
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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class RemoteVariableClientUDP {
    public static DatagramSocket aSocket = null;
    public static int serverPort;


    public static void main(String args[]) {
        // announce client is running
        System.out.println("The client is running.");

        // ask for the port number
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Please enter server port: ");
        serverPort = Integer.parseInt(keyboard.nextLine());

        int choice = 0, value = 0, ID = 0, result = 0;

        try {

            aSocket = new DatagramSocket();

            while (true) {
                // display the menu
                getMenu();
                // get the operation choice from user
                choice = keyboard.nextInt();

                // if choose 1, get the value to add
                if (choice == 1) {
                    System.out.println("Enter value to add:");
                    value = keyboard.nextInt();
                } else if (choice == 2) { // if choose 2, get the value to subtract
                    System.out.println("Enter value to subtract:");
                    value = keyboard.nextInt();
                } else if (choice == 4) { // if choose 4, client exit
                    System.out.println("Client side quitting. The remote variable server is still running.");
                    aSocket.close();
                    aSocket = null;
                    break;
                }

                // let user enter the ID
                System.out.println("Enter your ID:");
                ID = keyboard.nextInt();
                // get the result from server and display it
                result = getResult(choice, value, ID);
                System.out.println("The result is: " + result);
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
     * Sends a client request to the remote variable server and receives the server's response.
     *
     * @param choice (int): The selected operation (1: add, 2: subtract, 3: get).
     * @param value  (int): The value to use in the operation (for addition and subtraction).
     * @param ID     (int): The unique identifier for the remote variable.
     * @return The result of the operation received from the server.
     * @throws IOException if an I/O error occurs while sending or receiving data.
     */
    public static int getResult(int choice, int value, int ID) throws IOException {
        String packetString;

        // form the string to sent to the server ("ID,operation,value" ("ID,operation" if using get))
        if (choice != 3) {
            packetString = ID + "," + choice + "," + value;
        } else {
            packetString = ID + "," + choice;
        }

        // send the string in byte array to the server
        byte[] m = packetString.getBytes();
        InetAddress aHost = InetAddress.getByName("localhost");
        DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
        aSocket.send(request);

        // receive the reply from server
        byte[] buffer = new byte[1000];
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        aSocket.receive(reply);

        // transform the byte array reply into string of the result
        byte[] byteArray = new byte[reply.getLength()];
        System.arraycopy(reply.getData(), 0, byteArray, 0, reply.getLength());
        String replyString = new String(byteArray);

        // return the result
        return Integer.parseInt(replyString);
    }

    /**
     * Displays the menu options for users.
     *
     */
    public static void getMenu() {
        // print the contents of the menu
        System.out.println();
        System.out.println("1. Add a value to your sum.");
        System.out.println("2. Subtract a value from your sum.");
        System.out.println("3. Get your sum.");
        System.out.println("4. Exit client.");
    }
}
