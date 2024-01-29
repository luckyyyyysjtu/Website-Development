/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 27th, 2023
 * EchoClientTCP Class Documentation
 * The `EchoClientTCP` class represents a TCP client application that interacts with a server to perform blockchain-related actions.
 * Client side enters inputs and get responses from the server about details of the blockchain. Then it displays the result
 * to the console.
 * Actions can be performed are:
 * 0. View basic blockchain status
 * 1. Add a transaction to the blockchain
 * 2. Verify the blockchain
 * 3. View the blockchain
 * 4. Corrupt the chain
 * 5. Hide the corruption by repairing the chain
 * 6. Exit
 */

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class EchoClientTCP {
    public static int serverPort;
    public static Socket clientSocket;
    static RequestMessage request;
    static ResponseMessage response;

    /**
     * The `main` method serves as the entry point for the EchoClientTCP application.
     * It establishes a connection to a blockchain server and allows the user to interact with the blockchain by sending requests and receiving responses.
     * - It initializes the client by creating a socket and connecting to the server running on the local host at a specified port (default: 6789).
     * - It presents a menu of options to the user, enabling various actions related to the blockchain.
     * - It sends requests to the server in the form of JSON-serialized messages and receives responses.
     * - Depending on the user's choice, it performs actions such as viewing the blockchain status, adding transactions, verifying the blockchain, and more.
     * - It displays the results of these actions, including blockchain statistics and execution times.
     * - The user can choose to exit the application, closing the client socket if necessary.
     * @param args Command-line arguments (not used).
     * @throws IOException if an I/O error occurs during socket initialization or communication with the server.
     */
    public static void main(String args[]) throws IOException {

        // Set the server port and create a socket to connect to the server.
        serverPort = 6789;
        clientSocket = new Socket("localhost", serverPort);
        // Create a scanner for user input.
        Scanner s = new Scanner(System.in);
        // Flag to control the application loop.
        boolean exit = false;

        while (!exit) {
            // Display the menu to the user and initialize request and response objects.
            getMenu();
            request = new RequestMessage();
            response = new ResponseMessage();
            // Read the user's choice.
            int choice = s.nextInt();
            request.choice = Integer.toString(choice);

            switch (choice) {
                case 0 -> {
                    // Request blockchain status and display relevant information.
                    getResult(request.toJSON());
                    System.out.println("Current Size of chain: " + response.size);
                    System.out.println("Difficulty of most recent block: " + response.recentDifficulty);
                    System.out.println("Total difficulty for all blocks: " + response.totalDifficulty);
                    System.out.println("Approximate hashes per second on this machine: " + response.hashPerSecond);
                    System.out.println("Expected total hashes required for the whole chain: " + response.totalExpectedHashes);
                    System.out.println("Nonce for the most recent block: " + response.nonce);
                    System.out.println("Chain hash: " + response.chainHash);
                }
                case 1 -> {
                    // Add a new transaction to the blockchain.
                    // ask for the difficulty from the user
                    System.out.println("Enter difficulty > 0");
                    int difficulty = s.nextInt();
                    request.difficulty = Integer.toString(difficulty);

                    // ask for the transaction data from the user
                    System.out.println("Enter transaction");
                    s.nextLine();
                    request.data = s.nextLine();

                    // Send the request,receive response and display the execution time.
                    getResult(request.toJSON());
                    System.out.println("Total execution time to add this block was " + response.time + " milliseconds");

                }
                case 2 -> {
                    // Verify the blockchain and display the verification result and execution time.
                    getResult(request.toJSON());
                    System.out.println("Chain verification: " + response.verification);
                    System.out.println("Total execution time to verify the chain was " + response.time + " milliseconds");
                }
                case 3 -> {
                    // View the blockchain and display its content.
                    getResult(request.toJSON());
                    System.out.println("View the blockchain\n" + response.chain);
                }
                case 4 -> {
                    // Corrupt the blockchain by modifying a specific block's data.
                    // Ask for the index of the block to corrupt
                    System.out.println("corrupt the Blockchain");
                    System.out.println("Enter block ID of block to corrupt");
                    int id = s.nextInt();
                    request.index = Integer.toString(id);
                    // Ask for the new data from user
                    System.out.println("Enter new data for block " + id);
                    s.nextLine();
                    request.data = s.nextLine();

                    // Send the request, receive the response, and inform the user about the updated block data.
                    getResult(request.toJSON());
                    System.out.println("Block " + id + " now holds " + request.data);

                }
                case 5 -> {
                    // Repair the blockchain and display the execution time.
                    getResult(request.toJSON());
                    System.out.println("Total execution time required to repair the chain was " + response.time + " milliseconds");
                }
                case 6 -> {
                    // Exit the application and close the client socket.
                    exit = true;
                    clientSocket.close();
                    clientSocket = null;
                }
                default -> System.out.println("Wrong choice.");
            }
        }

    }

    /**
     * Send a request packet to the server and receive the response.
     * Read the reply into the ResponseMessage object in the client.
     *
     * @param packetString The request packet in JSON format.
     * @throws IOException if an I/O error occurs during communication with the server.
     */
    public static void getResult(String packetString) throws IOException {
        // create input and output streams for communication with the server
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

        // send the request to the server
        out.println(packetString);
        out.flush();
        // receive and process the server's response
        String replyString = in.readLine();
        // read the reply into the ResponseMessage object in the client
        response.fromJSON(replyString);
    }

    /**
     * Display the menu of available actions.
     */
    public static void getMenu() {
        // print the menu options
        System.out.println("0. View basic blockchain status.");
        System.out.println("1. Add a transaction to the blockchain.");
        System.out.println("2. Verify the blockchain.");
        System.out.println("3. View the blockchain.");
        System.out.println("4. Corrupt the chain.");
        System.out.println("5. Hide the corruption by repairing the chain.");
        System.out.println("6. Exit");
    }

}
