/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 6, 2023
 *
 * The `SigningClientTCP` class represents a client application that communicates
 * securely with a remote server using the RSA digital signature.
 *
 * It allows the user to perform mathematical operations (addition, subtraction and get)
 * on a remote variable stored on the server, ensuring data integrity and security
 * through digital signatures.
 *
 * The client generates RSA key pairs, signs messages, and sends them to the server
 * for processing. The server validates the signatures and performs the requested
 * operations on the remote variable.
 *
 * This class provides methods for:
 * - Generating RSA key pairs for secure communication with the server.
 * - Creating a unique client ID based on the public key.
 * - Signing messages using the client's private key.
 * - Sending signed messages to the server for processing.
 * - Displaying a menu for user interaction.
 * - Displaying results.
 *
 * To use this client, first, provide the server's port number when prompted.
 * Then, select the desired operation (addition, subtraction, or retrieval)
 * and provide the required input. The client will display the result of each
 * operation.
 */

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;

public class SigningClientTCP {
    public static int serverPort;
    public static Socket clientSocket;
    public static BigInteger e;
    public static BigInteger n;
    public static String ID;
    private static BigInteger d;


    public static void main(String args[]) {
        // announce client is running
        System.out.println("The client is running.");

        // ask for port number
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Please enter server port: ");
        serverPort = Integer.parseInt(keyboard.nextLine());

        // variable to store choice,value and result
        int choice = 0, value = 0, result = 0;

        try {

            // establish a connection to the server
            clientSocket = new Socket("localhost", serverPort);
            // generate RSA key pairs for secure communication
            getKeys();
            // create a unique client ID based on the public key
            getID();
            System.out.println("ID: " + ID);


            while (true) {
                // display the menu for user interaction
                getMenu();
                // get the operation choice
                choice = keyboard.nextInt();

                // if 1, let user enter value to add
                if (choice == 1) {
                    System.out.println("Enter value to add:");
                    value = keyboard.nextInt();
                } else if (choice == 2) { // if 2, let user enter value to subtract
                    System.out.println("Enter value to subtract:");
                    value = keyboard.nextInt();
                } else if (choice == 4) { // if 4, client exit
                    System.out.println("Client side quitting. The remote variable server is still running.");
                    clientSocket.close();
                    clientSocket = null;
                    break;
                }

                // get result from server and display
                result = getResult(choice, value);
                System.out.println("The result is: " + result);
            }

        } catch (Exception e) { // handle exception
            System.out.println("Exception:" + e.getMessage());
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
     * Get the result of an operation from the server.
     *
     * @param choice The operation choice (1 for addition, 2 for subtraction, 3 for retrieval).
     * @param value  The value to be added or subtracted.
     * @return The result of the operation received from the server.
     * @throws Exception If an error occurs during communication.
     */
    public static int getResult(int choice, int value) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
        String message;
        String packetString;

        // format the message to send with ID, public key, choice (and value)
        if (choice != 3) {
            message = ID + "," + e + "," + n + "," + choice + "," + value;
        } else {
            message = ID + "," + e + "," + n + "," + choice;
        }

        // sign the message before sending it
        packetString = message + "," + sign(message);

        // send messages to server
        out.println(packetString);
        out.flush();
        // get reply from server
        String replyString = in.readLine();
        return Integer.parseInt(replyString);
    }

    /**
     * Display the menu of available operations.
     */
    public static void getMenu() {
        // print menu
        System.out.println();
        System.out.println("1. Add a value to your sum.");
        System.out.println("2. Subtract a value from your sum.");
        System.out.println("3. Get your sum.");
        System.out.println("4. Exit client.");
    }

    /**
     * Generate RSA key pairs for secure communication with the server.
     */
    // Cite: Code from RSAExample.java
    public static void getKeys() {
        Random rnd = new Random();
        // Step 1: Generate two large random primes.
        // We use 400 bits here.
        BigInteger p = new BigInteger(400, 100, rnd);
        BigInteger q = new BigInteger(400, 100, rnd);

        // Step 2: Compute n by the equation n = p * q.
        n = p.multiply(q);

        // Step 3: Compute phi(n) = (p-1) * (q-1)
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // Step 4: Select a small odd integer e that is relatively prime to phi(n).
        // By convention the prime 65537 is used as the public exponent.
        e = new BigInteger("65537");

        // Step 5: Compute d as the multiplicative inverse of e modulo phi(n).
        d = e.modInverse(phi);

        System.out.println("Public Key = " + "(" + e + "," + n + ")");  // Step 6: (e,n) is the RSA public key
        System.out.println("Private Key = " + "(" + d + "," + n + ")");  // Step 7: (d,n) is the RSA private key
    }

    /**
     * Convert an array of bytes to a hexadecimal string representation.
     *
     * @param bytes The array of bytes to convert.
     * @return The hexadecimal string representation.
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
     * Generate a unique ID based on the public key for the client.
     *
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     */
    public static void getID() throws NoSuchAlgorithmException {
        // form public key
        String publicKey = e.toString() + n.toString();
        // compute hash of the public key
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(publicKey.getBytes());
        String IDstring = bytesToHex(md.digest());
        // ID will be formed by taking the least significant 20 bytes of the hash of the client's public key
        ID = IDstring.substring(IDstring.length() - 20);
    }

    /**
     * Sign a message using the client's private key.
     *
     * @param message The message to be signed.
     * @return The signature of the message.
     * @throws Exception If an error occurs during the signing process.
     */
    // Cite: Code from ShortMessageSign.java
    public static String sign(String message) throws Exception {
        // compute the digest with SHA-256
        byte[] bytesOfMessage = message.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bigDigest = md.digest(bytesOfMessage);

        // copy these bytes into a byte array that is one byte longer than needed
        // the resulting byte array has its extra byte set to zero
        byte[] signed = new byte[bigDigest.length + 1];
        signed[0] = 0;
        System.arraycopy(bigDigest, 0, signed, 1, bigDigest.length);

        // From the digest, create a BigInteger
        BigInteger m = new BigInteger(signed);
        // encrypt the digest with the private key
        BigInteger c = m.modPow(d, n);

        // return this as a big integer string
        return c.toString();

    }


}
