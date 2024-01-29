/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 27th, 2023
 * ResponseMessage Class Documentation
 * The ResponseMessage class represents a message used for communication or data exchange.
 * It is used for communication between client and server. Server uses it to form JSON string to respond and
 * Client uses it to decode the JSON string received into variables needed.
 * It provides methods for serializing and deserializing the message in JSON format.
 */

import com.google.gson.Gson;


public class ResponseMessage {


    // The operating time for different functions in the blockchain.
    String time;
    // The result to see if the chain is valid.
    String verification;
    // The string representation of the blockchain.
    String chain;
    // The size of the blockchain.
    String size;
    // The difficulty of the latest Block.
    String recentDifficulty;
    // The total difficulty of the blockchain.
    String totalDifficulty;
    // The hash calculations per second for the local computer.
    String hashPerSecond;
    // The total expected hashes required for the blockchain.
    String totalExpectedHashes;
    // The nonce value to get a valid hash.
    String nonce;
    // The hash of the latest Block.
    String chainHash;

    /**
     * Default constructor to create an empty `ResponseMessage`.
     */
    public ResponseMessage() {

    }

    /**
     * Constructor to create a `ResponseMessage` with specific values.
     *
     * @param time                The operating time for different functions in the blockchain.
     * @param verification        The result to see if the chain is valid.
     * @param chain               The string representation of the blockchain.
     * @param size                The size of the blockchain.
     * @param recentDifficulty    The difficulty of the latest Block.
     * @param totalDifficulty     The total difficulty of the blockchain.
     * @param hashPerSecond       The hash calculations per second for the local computer.
     * @param totalExpectedHashes The total expected hashes required for the blockchain.
     * @param nonce               The nonce value to get a valid hash.
     * @param chainHash           The hash of the latest Block.
     */
    public ResponseMessage(String time, String verification, String chain, String size, String recentDifficulty,
                           String totalDifficulty, String hashPerSecond, String totalExpectedHashes, String nonce, String chainHash) {

        this.time = time;
        this.verification = verification;
        this.chain = chain;
        this.size = size;
        this.recentDifficulty = recentDifficulty;
        this.totalDifficulty = totalDifficulty;
        this.hashPerSecond = hashPerSecond;
        this.totalExpectedHashes = totalExpectedHashes;
        this.nonce = nonce;
        this.chainHash = chainHash;
    }

    /**
     * Serialize the `ResponseMessage` to JSON format.
     *
     * @return A JSON representation of the message.
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * Deserialize a JSON string into a `ResponseMessage` object.
     *
     * @param JSON The JSON string to deserialize.
     */
    public void fromJSON(String JSON) {
        Gson gson = new Gson();
        // deserialize the JSON string
        ResponseMessage m = gson.fromJson(JSON, ResponseMessage.class);
        // assign the values
        this.time = m.time;
        this.verification = m.verification;
        this.chain = m.chain;
        this.size = m.size;
        this.recentDifficulty = m.recentDifficulty;
        this.totalDifficulty = m.totalDifficulty;
        this.hashPerSecond = m.hashPerSecond;
        this.totalExpectedHashes = m.totalExpectedHashes;
        this.nonce = m.nonce;
        this.chainHash = m.chainHash;
    }

}
