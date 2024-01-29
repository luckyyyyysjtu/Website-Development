/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 27th, 2023
 * The `EchoServerTCP` class represents a blockchain server that listens for incoming client connections
 * and handles blockchain-related requests. It serves as the core component of the blockchain network,
 * allowing clients to interact with the blockchain by sending and receiving messages.
 *
 * This class initializes a server socket, accepts client connections, and communicates with clients using
 * the TCP (Transmission Control Protocol) network protocol. It handles various client requests for viewing,
 * adding, updating, and verifying the blockchain, and continuously accepts new client connections to maintain server
 * operation.
 *
 * The server runs indefinitely, waiting for client connections and processing requests as they arrive. It also
 * manages socket resources and cleans up when the server exits.
 *
 **/

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class EchoServerTCP {
    public static int serverPort;
    public static Socket clientSocket;

    static RequestMessage request;
    static ResponseMessage response;
    static Scanner in;
    static PrintWriter out;

    /**
     * Main method for the Blockchain server. This method initializes the server, sets up a connection with a client,
     * and handles client requests for viewing, adding, updating, verifying, repairing the blockchain.
     * It also maintains the server operation by continuously accepting new client connections.
     *
     * @param args Command-line arguments (not used).
     * @throws NoSuchAlgorithmException if there's an issue with cryptographic algorithms.
     */
    public static void main(String args[]) throws NoSuchAlgorithmException {
        System.out.println("Blockchain server running");
        clientSocket = null;
        serverPort = 6789;

        // Create a new blockchain and generate the genesis block.
        BlockChain bc = new BlockChain();
        Block genesis = new Block(0, bc.getTime(), "Genesis", 2);
        bc.addBlock(genesis);

        // Calculate the hash speed for the local computer.
        bc.computeHashesPerSecond();
        int hashPerSecond = bc.getHashesPerSecond();

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

                // Initialize request and response objects.
                request = new RequestMessage();
                response = new ResponseMessage();

                // Receive the client's request and parse it.
                receive();

                // Process the client's request based on their choice.
                switch (Integer.parseInt(request.choice)) {
                    case 0 -> {
                        // Handle request to view blockchain status.
                        // Populate the response with blockchain information.
                        response.size = Integer.toString(bc.getChainSize());
                        response.recentDifficulty = Integer.toString(bc.getLatestBlock().getDifficulty());
                        response.totalDifficulty = Integer.toString(bc.getTotalDifficulty());
                        response.hashPerSecond = Integer.toString(hashPerSecond);
                        response.totalExpectedHashes = Double.toString(bc.getTotalExpectedHashes());
                        response.nonce = bc.getLatestBlock().getNonce().toString();
                        response.chainHash = bc.getChainHash();
                    }
                    case 1 -> {
                        // Handle request to add a transaction to the blockchain.
                        // Measure the execution time and populate the response.
                        java.sql.Timestamp t = bc.getTime();
                        Block newBlock = new Block(bc.getChainSize(), bc.getTime(), request.data, Integer.parseInt(request.difficulty));
                        bc.addBlock(newBlock);
                        java.sql.Timestamp t1 = bc.getTime();
                        long time = t1.getTime() - t.getTime();
                        response.time = Long.toString(time);
                    }
                    case 2 -> {
                        // Handle request to verify the blockchain.
                        // Measure the execution time and populate the response with verification result.
                        java.sql.Timestamp t = bc.getTime();
                        response.verification = bc.isChainValid();
                        java.sql.Timestamp t1 = bc.getTime();
                        long time = t1.getTime() - t.getTime();
                        response.time = Long.toString(time);
                    }
                    case 3 -> {
                        // Handle request to view the blockchain.
                        // Populate the response with the serialized blockchain.
                        response.chain = bc.toString();
                    }
                    case 4 -> {
                        // Handle request to corrupt the blockchain by modifying a block's data.
                        // get the index and new data from client request and modify the data in blockchain.
                        bc.getBlock(Integer.parseInt(request.index)).setData(request.data);
                    }
                    case 5 -> {
                        // Handle request to repair the blockchain.
                        // Measure the execution time and populate the response.
                        java.sql.Timestamp t = bc.getTime();
                        bc.repairChain();
                        java.sql.Timestamp t1 = bc.getTime();
                        long time = t1.getTime() - t.getTime();
                        response.time = Long.toString(time);
                    }
                    case 6 -> {
                        // Handle the exit request.
                    }
                    default -> System.out.println("Wrong choice.");
                }

                // Send the response to the client.
                send(response.toJSON());

            }
            // Handle exceptions
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
     * The `receive` method is responsible for receiving a JSON request message from the client.
     * It reads the message from the input stream and deserializes it into a RequestMessage object.
     */
    public static void receive() {
        System.out.println("We have a visitor");
        // receive request message from client
        String requestMessage = in.nextLine();
        System.out.println("JSON REQUEST MESSAGE:" + requestMessage);
        // deserializes it into a RequestMessage object
        request.fromJSON(requestMessage);
    }

    /**
     * The `send` method is responsible for sending a JSON response message to the client.
     * It takes a message as input, serializes it into a JSON string, and sends it through the output stream to the client.
     * @param message The message to be sent as a JSON response.
     */
    public static void send(String message) {
        // send response message to the client
        System.out.println("JSON RESPONSE MESSAGE:" + message.replace("\\", ""));
        out.println(message);
        out.flush();
    }
}
