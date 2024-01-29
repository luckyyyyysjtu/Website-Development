/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 6, 2023
 *
 * The `VerifyingServerTCP` class implements a server that receives and verifies
 * client requests with digital signatures before processing them.
 *
 * This server listens on a specified port for incoming client requests,
 * verifies the digital signatures on these requests, and processes them if
 * they are valid. It maintains a data structure to store client IDs and sums
 * to perform addition, subtraction and get operations based on client requests.
 */

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;

public class VerifyingServerTCP {
    // data structure to store client IDs and sums
    static TreeMap<String, Integer> sums = new TreeMap<String, Integer>();
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

                // receive client request
                String requestString = receive();

                // verify signature and ID of the client
                // if true, perform the operation and send back the result
                if (verify(requestString)) {

                    // retrieve ID, choice and value from the client request
                    String[] packetString = requestString.split(",");
                    String ID = packetString[0];
                    int choice = Integer.parseInt(packetString[3]);
                    int value;

                    // if get, then set value to 0 (unused)
                    if (packetString.length == 6) {
                        value = Integer.parseInt(packetString[4]);
                    } else {
                        value = 0;
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
                        System.out.println("Operation: " + original + " " + operation[choice - 1] + " " + value + ", Result: " + result);
                    } else {
                        System.out.println("Operation: " + operation[choice - 1] + ", Result: " + result);
                    }

                    // send the result to the client
                    send(String.valueOf(result));

                } else {
                    // if fail to verify, send Error in request
                    send("Error in request");
                }


            }
            // Handle exceptions
        } catch (Exception e) {
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
     * Calculate the result of an operation based on client choice.
     *
     * @param choice The client's choice of operation (1 for add, 2 for subtract, 3 for get).
     * @param value  The value to be added or subtracted.
     * @param ID     The client's ID.
     * @return The result of the operation.
     */
    public static int getResult(int choice, int value, String ID) {
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
     * Convert a byte array to a hexadecimal string.
     *
     * @param bytes The byte array to be converted.
     * @return The hexadecimal string representation of the byte array.
     */
    // Cite: Code from https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
    public static String bytesToHex(byte[] bytes) {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Verify the client's request using digital signatures.
     * The server will make two checks before servicing any client request.
     * First, does the public key (included with each request) hash to the ID (also provided with each request)?
     * Second, is the request properly signed?
     *
     * @param data The client's request data as a string.
     * @return true if the request is verified successfully; otherwise, false.
     * @throws Exception Thrown if there is an error in the verification process.
     */
    // Cite: Code from ShortMessageVerify.java
    public static boolean verify(String data) throws Exception {
        // extract ID, e, n from client messages
        String[] packetString = data.split(",");
        String ID = packetString[0];
        String e = packetString[1];
        String n = packetString[2];
        String encryptedHashStr = packetString[packetString.length - 1];
        String messageToCheck;

        // format public key
        System.out.println("Public Key = " + "(" + e + "," + n + ")");

        // if add or subtract, obtain the first 5 elements as the token used to sign
        if (packetString.length == 6) {
            messageToCheck = String.join(",", Arrays.copyOfRange(packetString, 0, 5));

        } else { // if get, obtain the first 4 elements as the token used to sign
            messageToCheck = String.join(",", Arrays.copyOfRange(packetString, 0, 4));
        }

        // verify the hash of public key to ID
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String IDhased = bytesToHex(md.digest((e + n).getBytes()));
        IDhased = IDhased.substring(IDhased.length() - 20);
        // if not match, print Error in request, return false
        if (!IDhased.equals(ID)) {
            System.out.println("Error in request");
            return false;
        }

        // verify if the request is properly signed
        // get the BigInteger from the encryptedHashStr
        BigInteger encryptedHash = new BigInteger(encryptedHashStr);
        // decrypt the encryptedHash
        BigInteger decryptedHash = encryptedHash.modPow(new BigInteger(e), new BigInteger(n));
        // Get the bytes from messageToCheck
        byte[] bytesOfMessageToCheck = messageToCheck.getBytes("UTF-8");

        // compute the digest of the message with SHA-256
        byte[] messageToCheckDigest = md.digest(bytesOfMessageToCheck);
        // add a zero byte the bytes array
        byte[] extraByte = new byte[messageToCheckDigest.length + 1];
        extraByte[0] = 0;
        System.arraycopy(messageToCheckDigest, 0, extraByte, 1, messageToCheckDigest.length);

        // Make it a big int
        BigInteger bigIntegerToCheck = new BigInteger(extraByte);

        // compare the two
        if (bigIntegerToCheck.compareTo(decryptedHash) == 0) {
            // if true, print verified
            System.out.println("Signature verified.");
            return true;
        } else {
            // if false, print error in request
            System.out.println("Error in request");
            return false;
        }

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
