/**
 * Author: Yi Guo (Andrew ID:yiguo)
 * Last Modified: Oct 27th, 2023
 * RequestMessage Class Documentation
 * The `RequestMessage` class represents a message used for communication or data exchange.
 * It is used for communication between client and server. Client use it to form JSON string to send and
 * Server use it to decode the JSON string received.
 * It provides methods for serializing and deserializing the message in JSON format.
 */

import com.google.gson.Gson;

public class RequestMessage {

    String choice;
    String difficulty;
    String data;
    String index;

    /**
     * Default constructor to create an empty `RequestMessage`.
     */
    public RequestMessage() {
    }

    /**
     * Constructor to create a `RequestMessage` with specific values.
     *
     * @param choice     The operation chosen by the user.
     * @param difficulty The difficulty level of the hash calculation.
     * @param data       The transaction in the blockchain.
     * @param index      The index of the block to modify.
     */
    public RequestMessage(String choice, String difficulty, String data, String index) {
        this.choice = choice;
        this.difficulty = difficulty;
        this.data = data;
        this.index = index;
    }

    /**
     * Serialize the `RequestMessage` to JSON format.
     *
     * @return A JSON representation of the message.
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * Deserialize a JSON string into a `RequestMessage` object.
     *
     * @param JSON The JSON string to deserialize.
     */
    public void fromJSON(String JSON) {
        Gson gson = new Gson();
        // deserialize the JSON string
        RequestMessage m = gson.fromJson(JSON, RequestMessage.class);
        // assign the values
        this.choice = m.choice;
        this.difficulty = m.difficulty;
        this.data = m.data;
        this.index = m.index;
    }
}
