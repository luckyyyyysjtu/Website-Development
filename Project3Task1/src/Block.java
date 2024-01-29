/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 27th, 2023
 *
 * The `Block` class represents a block in a blockchain.
 * Each block contains information about transactions and is linked to the previous block in the chain.
 *
 * Class Fields:
 * - `index` (int): The index of the block within the blockchain.
 * - `timestamp` (java.sql.Timestamp): The timestamp indicating when the block was created.
 * - `data` (String): Transactions stored in the block.
 * - `previousHash` (String): The hash of the previous block in the blockchain.
 * - `nonce` (BigInteger): A BigInteger value determined by a proof of work routine.
 * - `difficulty` (int): The difficulty level of the proof-of-work algorithm.
 *
 * Constructors:
 * - `Block(int index, java.sql.Timestamp timestamp, String data, int difficulty)`: Creates a new block with the specified index, timestamp, data, and difficulty.
 *
 * Methods:
 * - `calculateHash()`: Calculate and return the hash of the concatenation of the index, timestamp, data, previousHash, nonce, and difficulty.
 * - `getNonce()`: Get the nonce value of the block. The nonce is a number that has been found to cause the hash of this block to have the correct number of leading hexadecimal zeroes.
 * - `proofOfWork() throws NoSuchAlgorithmException`: Perform a proof-of-work algorithm to find a valid hash for the block.
 * - `getDifficulty()`: Get the difficulty level of the proof-of-work algorithm.
 * - `setDifficulty(int difficulty)`: Set the difficulty level of the proof-of-work algorithm.
 * - `toString()`: Convert the block's information into a JSON-formatted string.
 * - `setPreviousHash(String previousHash)`: Set the previous block's hash.
 * - `getPreviousHash()`: Get the hash of the previous block.
 * - `getIndex()`: Get the index of the block.
 * - `setIndex(int index)`: Set the index of the block.
 * - `setTimestamp(java.sql.Timestamp timestamp)`: Set the timestamp of the block.
 * - `getTimestamp()`: Get the timestamp of the block.
 * - `getData()`: Get the data stored in the block.
 * - `setData(String data)`: Set the data stored in the block.
 *
 */

import com.google.gson.JsonObject;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {
    // The position of the block on the chain. The first block (the so called Genesis block) has an index of 0.
    private int index;
    // a Java Timestamp object, it holds the time of the block's creation.
    private java.sql.Timestamp timestamp;
    // a String holding the block's single transaction details.
    private java.lang.String data;
    // the SHA256 hash of a block's parent. This is also called a hash pointer.
    private String previousHash;
    //  a BigInteger value determined by a proof of work routine. This has to be found by the proof of work logic.
    //  It has to be found so that this block has a hash of the proper difficulty.
    private BigInteger nonce;
    // a small integer representing the minimum number of leading hex zeroes the hash must have.
    private int difficulty;

    /**
     * Constructor to create a new block with the specified properties.
     *
     * @param index The index of the block within the blockchain.
     * @param timestamp The timestamp indicating when the block was created.
     * @param data Transactions stored in the block.
     * @param difficulty The difficulty level of the proof-of-work algorithm.
     */
    public Block(int index,
                 java.sql.Timestamp timestamp,
                 java.lang.String data,
                 int difficulty) {

        // assign values to the instance variable
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.difficulty = difficulty;
    }

    /**
     * Calculate and return the hash of the block based on its properties.
     * This method computes a hash of the concatenation of the index, timestamp, data, previousHash, nonce, and difficulty.
     *
     * @return The SHA-256 hash of the block as a hexadecimal string.
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available.
     */
    public java.lang.String calculateHash() throws NoSuchAlgorithmException {
        // concatenation of the index, timestamp, data, previousHash, nonce, and difficulty
        String message = index + "," + timestamp.toString() + "," + data + "," + previousHash + "," + nonce + "," + difficulty;

        // calculate SHA-256 hash of the concatenation
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(message.getBytes());

        // Code from stack overflow
        // https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
        // Convert the bytes array to a hexadecimal string
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[hash.length * 2];
        for (int j = 0; j < hash.length; j++) {
            int v = hash[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Get the nonce value of the block.
     *
     * @return The nonce value.
     */
    public java.math.BigInteger getNonce() {
        return nonce;
    }

    /**
     * The proof of work methods finds a good hash. It increments the nonce until it produces a good hash.
     * This method calls calculateHash() to compute a hash of the concatenation of the index, timestamp, data, previousHash, nonce, and difficulty.
     * If the hash has the appropriate number of leading hex zeroes, it is done and returns that proper hash.
     * If the hash does not have the appropriate number of leading hex zeroes, it increments the nonce by 1 and tries again.
     *
     * @return a hash that has the appropriate number of leading hex zeroes.
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available.
     */
    public java.lang.String proofOfWork() throws NoSuchAlgorithmException {
        // set the nonce to zero
        nonce = BigInteger.valueOf(0);
        // build the string to check according to the difficulty
        String check = "0".repeat(difficulty);
        // calculate the initial hash for nonce 0
        String hash = calculateHash();

        // loop until the hash has the appropriate number of leading hex zeros
        while (!hash.substring(0, difficulty).equals(check)) {
            // if not, increase nonce by 1
            nonce = nonce.add(BigInteger.valueOf(1));
            hash = calculateHash();
        }
        // return the hash with the appropriate number of leading hex zeros
        return hash;

    }

    /**
     * Get the difficulty level of the proof-of-work algorithm.
     *
     * @return The current difficulty level.
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Set the difficulty level of the proof-of-work algorithm.
     *
     * @param difficulty The new difficulty level to set.
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }


    /**
     * Convert the block's information into a JSON-formatted string.
     *
     * @return A JSON-formatted string representing the block's data.
     */
    public java.lang.String toString() {
        // create a new JSON object
        // add new properties to it and pass the value
        JsonObject message = new JsonObject();
        message.addProperty("index", index);
        message.addProperty("time stamp", timestamp.toString());
        message.addProperty("Tx", data);
        message.addProperty("PrevHash", previousHash);
        message.addProperty("nonce", nonce);
        message.addProperty("difficulty", difficulty);

        return message.toString().replace("\\", "");
    }

    /**
     * Set the previous block's hash for linking.
     *
     * @param previousHash The hash of the previous block to set.
     */
    public void setPreviousHash(java.lang.String previousHash) {
        this.previousHash = previousHash;
    }

    /**
     * Get the hash of the previous block.
     *
     * @return The hash of the previous block in the blockchain.
     */
    public java.lang.String getPreviousHash() {
        return previousHash;
    }

    /**
     * Get the index of the block.
     *
     * @return The index of the block within the blockchain.
     */

    public int getIndex() {
        return index;
    }

    /**
     * Set the index of the block.
     *
     * @param index The new index to set for the block.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Set the timestamp of the block.
     *
     * @param timestamp The new timestamp to set for the block.
     */
    public void setTimestamp(java.sql.Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get the timestamp of the block.
     *
     * @return The timestamp indicating when the block was created.
     */
    public java.sql.Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Get the data stored in the block.
     *
     * @return The data or transactions stored in the block.
     */
    public java.lang.String getData() {
        return data;
    }

    /**
     * Set the data stored in the block.
     *
     * @param data The new data to set for the block.
     */
    public void setData(java.lang.String data) {
        this.data = data;
    }

    // Main method (not used in this class)
    public static void main(java.lang.String[] args) {
    }


}
