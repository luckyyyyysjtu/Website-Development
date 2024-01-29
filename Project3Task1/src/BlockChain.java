/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 27th, 2023
 * BlockChain Class Documentation
 *
 * The `BlockChain` class represents a simple blockchain and provides methods for managing and interacting with the blockchain.
 *
 * Class Fields:
 * - `chain` (ArrayList<Block>): An ArrayList that holds blocks in the blockchain.
 * - `chainHash` (String): The SHA256 hash of the most recently added Block.
 * - `hashesPerSecond` (int): The approximate number of hashes per second on this computer.
 *
 * Constructors:
 * - `BlockChain()`: Initializes an empty blockchain with default values for fields.
 *
 * Public Methods:
 *
 * - `getChainHash()`: Get the hash of the most recently added Block.
 *
 * - `getTime()`: Get the current timestamp as a `java.sql.Timestamp` object.
 *
 * - `getLatestBlock()`: Get the most recent block in the blockchain.
 *
 * - `getChainSize()`: Get the current size of the blockchain (number of blocks).
 *
 * - `computeHashesPerSecond()`: Compute the number of hash calculations performed per second.
 *
 * - `getHashesPerSecond()`: Get the number of hash calculations per second.
 *
 * - `addBlock(Block newBlock)`: Add a new block to the blockchain.
 *
 * - `toString()`: Convert the blockchain's information into a JSON-formatted string.
 *
 * - `getBlock(int i)`: Get a specific block from the blockchain by its index.
 *
 * - `getTotalDifficulty()`: Calculate the total difficulty of all blocks in the blockchain.
 *
 * - `getTotalExpectedHashes()`: Calculate the total expected hashes required for the entire chain.
 *
 * - `isChainValid()`: Check if the blockchain is valid and uncorrupted.
 *
 * - `repairChain()`: Repair the blockchain in case of corruption.
 *
 * - `public static void main(String[] args) throws NoSuchAlgorithmException`: The main method to run the blockchain application.
 * - Allows for basic blockchain operations, including viewing, adding, verifying, and manipulating blocks.
 */

import com.google.gson.JsonObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;


public class BlockChain {
    // an ArrayList to hold Blocks
    private ArrayList<Block> chain;
    // a chain hash to hold a SHA256 hash of the most recently added Block
    private String chainHash;
    // an int to hold the approximate number of hashes per second on this computer
    private int hashesPerSecond;

    /**
     * Constructor to initialize an empty blockchain with default values.
     */
    public BlockChain() {
        // create a blockchain object
        chain = new ArrayList<Block>();
        // set chain hash to "" and hashPerSecond to 0
        chainHash = "";
        hashesPerSecond = 0;
    }

    /**
     * Get the hash of the most recently added Block.
     *
     * @return The hash of the most recently added Block.
     */
    public java.lang.String getChainHash() {
        return chainHash;
    }

    /**
     * Get the current timestamp as a `java.sql.Timestamp` object.
     *
     * @return A `java.sql.Timestamp` representing the current time.
     */
    public java.sql.Timestamp getTime() {
        return new java.sql.Timestamp(System.currentTimeMillis());
    }

    /**
     * Get the most recent block in the blockchain.
     *
     * @return The most recent `Block` in the blockchain.
     */
    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    /**
     * Get the current size of the blockchain (number of blocks).
     *
     * @return The current size of the blockchain.
     */
    public int getChainSize() {
        return chain.size();
    }

    /**
     * Compute the number of hash calculations performed per second.
     *
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available.
     */
    public void computeHashesPerSecond() throws NoSuchAlgorithmException {
        // record start time
        java.sql.Timestamp t = getTime();
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // hash "00000000" for 2000000 times
        for (int i = 0; i < 2000000; i++) {
            byte[] bytes = md.digest("00000000".getBytes());
        }

        // record end time
        java.sql.Timestamp t1 = getTime();
        // get time gap in between
        double time = t1.getTime() - t.getTime();

        // calculate the number of hash performed per second
        hashesPerSecond = (int) (2000000 / (time / 1000));
    }

    /**
     * Get the number of hash calculations performed per second.
     *
     * @return The number of hash calculations per second.
     */
    public int getHashesPerSecond() {
        return hashesPerSecond;
    }

    /**
     * Add a new block to the blockchain.
     *
     * @param newBlock The `Block` to add to the blockchain.
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available.
     */
    public void addBlock(Block newBlock) throws NoSuchAlgorithmException {
        // set PreviousHash to the hash value of the previous block in the chain
        newBlock.setPreviousHash(chainHash);
        // do proof of work for the new block
        chainHash = newBlock.proofOfWork();
        // add the block to the ArrayList
        chain.add(newBlock);
    }

    /**
     * Convert the blockchain's information into a JSON-formatted string.
     *
     * @return A JSON-formatted string representing the blockchain's data.
     */
    public java.lang.String toString() {
        JsonObject message = new JsonObject();

        // add property to the JSON showing the whole chain and the hash of the latest block in chain
        message.addProperty("ds_chain", chain.toString());
        message.addProperty("chainHash", chainHash);

        return message.toString().replace("\\", "");
    }

    /**
     * Get a specific block from the blockchain by its index.
     *
     * @param i The index of the block to retrieve.
     * @return The `Block` at the specified index.
     */
    public Block getBlock(int i) {
        // get the i th block from the ArrayList
        return chain.get(i);
    }

    /**
     * Calculate the total difficulty of all blocks in the blockchain.
     *
     * @return The total difficulty of all blocks in the blockchain.
     */
    public int getTotalDifficulty() {
        int total = 0;
        // loop through blocks and sum up their difficulty
        for (Block b : chain) {
            total += b.getDifficulty();
        }
        return total;
    }

    /**
     * Calculate the total expected hashes required for the entire chain.
     *
     * @return The total expected hashes required for the entire chain.
     */
    public double getTotalExpectedHashes() {
        double total = 0;
        // loop through blocks and sum up their expected hashes calculated from difficulty
        for (Block b : chain) {
            total += Math.pow(16, b.getDifficulty());
        }
        return total;
    }

    /**
     * Check if the blockchain is valid and uncorrupted.
     *
     * @return "TRUE" if the chain is valid, or an error message if it's corrupted.
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available.
     */
    public java.lang.String isChainValid() throws NoSuchAlgorithmException {
        // If the chain only contains one block
        if (getChainSize() == 1) {
            // computes the hash of the block
            Block g = getBlock(0);
            String hash = g.calculateHash();
            String check = "0".repeat(g.getDifficulty());

            // checks that the hash has the requisite number of leftmost 0's (proof of work)
            if (!hash.substring(0, g.getDifficulty()).equals(check)) {
                // if not, show the error
                return "FALSE\nImproper hash on node 0 does not begin with " + check;
            } else if (!chainHash.equals(hash)) { // else check that the chain hash is equal to this computed hash
                // if not, shpw the error
                return "FALSE\nChain hash error.";
            } else {
                // else return "TRUE"
                return "TRUE";
            }
        } else { // If the chain has more blocks than one
            Block g0 = getBlock(0);
            String check0 = "0".repeat(g0.getDifficulty());
            Block g1;
            String check1;
            String hash1 = "";

            // first check the proof of work of block 0
            if (!g0.calculateHash().substring(0, g0.getDifficulty()).equals(check0)) {
                return "FALSE\nImproper hash on node 0 does not begin with " + check0;
            }

            // then checking from block one, until we have validated the entire chain
            for (int i = 1; i < chain.size(); i++) {
                // compute a hash in Block i
                g1 = getBlock(i);
                check1 = "0".repeat(g1.getDifficulty());
                hash1 = g1.calculateHash();

                // compare the previous hash with the hash of the previous block
                if (!getBlock(i - 1).calculateHash().equals(g1.getPreviousHash())) {
                    return "FALSE\nBlock " + i + " previous hash error.";
                } else if (!hash1.substring(0, g1.getDifficulty()).equals(check1)) { // check proof of work if block i
                    return "FALSE\nImproper hash on node " + i + " does not begin with " + check1;
                }
            }

            // check chainHash
            if (!chainHash.equals(hash1)) {
                return "FALSE\nChain hash error.";
            } else {
                // else return "TRUE"
                return "TRUE";
            }
        }
    }

    /**
     * Repair the blockchain in case of corruption.
     *
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available.
     */
    public void repairChain() throws NoSuchAlgorithmException {
        String hash = "";

        // loop through the chain and fix the proof of work as well as the previous hash
        for (int i = 0; i < getChainSize(); i++) {
            chain.get(i).setPreviousHash(hash);
            hash = chain.get(i).proofOfWork();
        }

        // fix the chainHash
        chainHash = hash;
    }


}
