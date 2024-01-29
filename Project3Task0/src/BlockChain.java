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

    /**
     * The main method to run the blockchain application.
     *
     * If the user selects option 0, the program will display: The number of blocks on the chain. Difficulty of most recent block.
     * The total difficulty for all blocks Approximate hashes per second on this machine. Expected total hashes required for the whole chain.
     * The computed nonce for most recent block. The chain hash (hash of the most recent block).
     *
     * If the user selects option 1, the program will prompt for and then read the difficulty level for this block.
     * It will then prompt for and then read a line of data from the user (representing a transaction).
     * The program will then add a block containing that transaction to the block chain.
     * The program will display the time it took to add this block. Note: The first block added after Genesis has index 1.
     * The second has 2 and so on. The Genesis block is at position 0.
     *
     * If the user selects option 2, then call the isChainValid method and display the results.
     * It is important to note that this method will execute fast. Blockchains are easy to validate but time-consuming to
     *
     * If the user selects option 3, display the entire Blockchain contents as a correctly formed JSON document.
     *
     * If the user selects option 4, she wants to corrupt the chain. Ask her for the block index (0..size-1) and ask her for the new data that will be placed in the block.
     * Her new data will be placed in the block. At this point, option 2 (verify chain) should show false.
     * In other words, she will be making a data change to a particular block and the chain itself will become invalid.
     *
     * If the user selects 5, she wants to repair the chain. That is, she wants to recompute the proof of work for each node that has become invalid
     * - due perhaps, to an earlier selection of option 4. The program begins at the Genesis block and checks each block in turn.
     * If any block is found to be invalid, it executes repair logic.
     *
     * @param args Command-line arguments (not used).
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available.
     */

    /**
     * This part gives a comment for analyzing how this system behaves as the difficulty increases.
     * Experiment:
     * initial chain size: 1
     * Add a new block with difficulty level 2. Data: Yi Guo pays Marty 100 DSCoin.
     * time for addBlock() - 1 milliseconds
     * time for isChainValid() - 1 milliseconds
     * time for repairChain() - 2 milliseconds
     *
     * initial chain size: 2
     * Add a new block with difficulty level 3. Data: Yi Guo pays Marty 100 DSCoin.
     * time for addBlock() - 6 milliseconds
     * time for isChainValid() - 0 milliseconds
     * time for repairChain() - 5 milliseconds
     *
     * initial chain size: 3
     * Add a new block with difficulty level 4. Data: Yi Guo pays Marty 100 DSCoin.
     * time for addBlock() - 379 milliseconds
     * time for isChainValid() - 0 milliseconds
     * time for repairChain() - 382 milliseconds
     *
     * initial chain size: 4
     * Add a new block with difficulty level 5. Data: Yi Guo pays Marty 100 DSCoin.
     * time for addBlock() - 1412 milliseconds
     * time for isChainValid() - 0 milliseconds
     * time for repairChain() - 1795 milliseconds
     *
     * We can conclude that as the difficulty increases from 2-3-4-5
     * 1. The time required to execute addBlock() increases significantly (1 - 6 - 379 - 1412). This is because finding a valid hash with higher difficulty requires more computational effort.
     * 2. The isChainValid() method's execution time remains relatively constant (1 - 0 - 0 - 0) as it checks the integrity of the entire blockchain, which doesn't depend on individual block difficulties.
     * 3. The repairChain() method's execution time also increases significantly (2 - 5 - 382 - 1795). As the blockchain size grows, more blocks need to be repaired.
     * It's directly proportional to the number of blocks in the chain. When we do the repair, we still have to do proof of work to get the correct hash. It follows the same logic as addBlock, but loop through more blocks than it.
     * So generally running time of repairChain() increases very sharply as the difficulty increases.
     */
    public static void main(java.lang.String[] args) throws NoSuchAlgorithmException {
        // initialize a blockchain
        BlockChain bc = new BlockChain();

        // add the genesis block to it with difficulty 2
        Block genesis = new Block(0, bc.getTime(), "Genesis", 2);
        bc.addBlock(genesis);

        // compute HashesPerSecond
        bc.computeHashesPerSecond();
        int hashPerSecond = bc.getHashesPerSecond();

        Scanner s = new Scanner(System.in);
        boolean exit = false;

        // loop until client select exit
        while (!exit) {
            // display the menu
            System.out.println("0. View basic blockchain status.");
            System.out.println("1. Add a transaction to the blockchain.");
            System.out.println("2. Verify the blockchain.");
            System.out.println("3. View the blockchain.");
            System.out.println("4. Corrupt the chain.");
            System.out.println("5. Hide the corruption by repairing the chain.");
            System.out.println("6. Exit");

            switch (s.nextInt()) {
                case 0 -> {
                    // If the user selects option 0, display
                    // The number of blocks on the chain. Difficulty of most recent block.
                    // The total difficulty for all blocks Approximate hashes per second on this machine. Expected total hashes required for the whole chain.
                    // The computed nonce for most recent block. The chain hash (hash of the most recent block).
                    System.out.println("Current Size of chain: " + bc.getChainSize());
                    System.out.println("Difficulty of most recent block: " + bc.getLatestBlock().getDifficulty());
                    System.out.println("Total difficulty for all blocks: " + bc.getTotalDifficulty());
                    System.out.println("Approximate hashes per second on this machine: " + hashPerSecond);
                    System.out.println("Expected total hashes required for the whole chain: " + bc.getTotalExpectedHashes());
                    System.out.println("Nonce for the most recent block: " + bc.getLatestBlock().getNonce());
                    System.out.println("Chain hash: " + bc.getChainHash());
                }
                case 1 -> {
                    // If select option 1,
                    // ask for difficulty of the block to add
                    System.out.println("Enter difficulty > 0");
                    int difficulty = s.nextInt();

                    // ask for transaction data of the new block
                    System.out.println("Enter transaction");
                    s.nextLine();
                    String data = s.nextLine();

                    // add the block and calculate the time to add
                    java.sql.Timestamp t = bc.getTime();
                    Block newBlock = new Block(bc.getChainSize(), bc.getTime(), data, difficulty);
                    bc.addBlock(newBlock);
                    java.sql.Timestamp t1 = bc.getTime();
                    long time = t1.getTime() - t.getTime();
                    System.out.println("Total execution time to add this block was " + time + " milliseconds");

                }
                case 2 -> {
                    // if select option 2
                    java.sql.Timestamp t = bc.getTime();
                    // verify the chain
                    System.out.println("Chain verification: " + bc.isChainValid());
                    // calculate the time to verify
                    java.sql.Timestamp t1 = bc.getTime();
                    long time = t1.getTime() - t.getTime();
                    System.out.println("Total execution time to verify the chain was " + time + " milliseconds");
                }
                case 3 -> {
                    // if select option 3
                    // display the blockchain
                    System.out.println("View the blockchain\n" + bc);
                }
                case 4 -> {
                    // if select option 4
                    System.out.println("corrupt the Blockchain");
                    // ask for the block index to corrupt
                    System.out.println("Enter block ID of block to corrupt");
                    int id = s.nextInt();
                    // enter the new data for the corrupted block
                    System.out.println("Enter new data for block " + id);
                    s.nextLine();
                    String data = s.nextLine();
                    // set the data of that block to the new data
                    bc.getBlock(id).setData(data);
                    System.out.println("Block " + id + " now holds " + data);

                }
                case 5 -> {
                    // repair the chain and calculate the time it takes
                    java.sql.Timestamp t = bc.getTime();
                    bc.repairChain();
                    java.sql.Timestamp t1 = bc.getTime();
                    long time = t1.getTime() - t.getTime();
                    System.out.println("Total execution time required to repair the chain was " + time + " milliseconds");
                }
                case 6 -> {
                    // exit the client
                    exit = true;
                }
                default -> System.out.println("Wrong choice.");
            }

        }


    }


}
