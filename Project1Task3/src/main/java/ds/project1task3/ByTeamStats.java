package ds.project1task3;
/*
 * @author Yi Guo
 * Andrew ID: yiguo
 *
 * This file is the Model component of the MVC. It models the logic of getting player names of a specific team
 * selected by user. It also searches for all the team abbreviations to be displayed in the selection bar.
 * The model relies on API and uses gson to parse Json file.
 *
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class ByTeamStats {
    /**
     * The method is to connect to the api and get the responses in json
     *
     * @param apiUrl The url end point of the api.
     * @return the responses string from the api
     */
    public String getAPI(String apiUrl) {
        String apiResponse = null;
        try {
            URL url = new URL(apiUrl);

            // Open a connection to the API
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "f5f272463e9a48e6ac5b5063e1b927d5");


            // Read the response from the API
            int responseCode = connection.getResponseCode();

            // read responses line by line
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // The 'response' variable now contains the API response as a String
                apiResponse = response.toString();

            } else {
                System.out.println("Error: " + responseCode);
            }

            // close connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiResponse;
    }

    /**
     * The method is to get all team abbreviations used for displaying in the selection bar
     *
     * @return the set of teams abbreviations
     */
    public HashSet<String> getTeams() {
        // for storing team abbreviations
        HashSet<String> t = new HashSet<String>();

        // get responses from api
        String apiResponse = getAPI("https://api.sportsdata.io/v3/mlb/scores/json/teams?key=f5f272463e9a48e6ac5b5063e1b927d5");
        // use gson to parse json
        Gson gson = new Gson();
        JsonArray jsonTeams = gson.fromJson(apiResponse, JsonArray.class);
        // loop through the json elements and add the team name to the hashset
        // duplicates will be removed
        for (JsonElement team : jsonTeams) {
            JsonObject teamObject = team.getAsJsonObject();
            String name = teamObject.get("Key").getAsString();
            t.add(name);
        }
        return t;
    }

    /**
     * The method is to the names of all players in a team.
     *
     * @param team the team to get all players from
     * @return an ArrayList of all player names in the team
     */
    public ArrayList<String> getPlayers(String team) {
        // connect to search player by team API
        String apiUrl = "https://api.sportsdata.io/v3/mlb/scores/json/PlayersBasic/" + team + "?key=f5f272463e9a48e6ac5b5063e1b927d5";
        String apiResponse = getAPI(apiUrl);
        // used for storing results
        ArrayList<String> p = new ArrayList<String>();

        Gson gson = new Gson();
        // loop through json elements and get the first name and last name for each player in the team
        // add the name to the arraylist
        JsonArray jsonPlayers = gson.fromJson(apiResponse, JsonArray.class);
        for (JsonElement player : jsonPlayers) {
            JsonObject playerObject = player.getAsJsonObject();
            String name = playerObject.get("FirstName").getAsString() + " " + playerObject.get("LastName").getAsString();

            p.add(name);
        }
        return p;
    }

    public static void main(String[] args) {
    }

}
