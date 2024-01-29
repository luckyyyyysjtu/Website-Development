package ds.project1task3;
/*
 * @author Yi Guo
 * Andrew ID: yiguo
 *
 * This file is the Model component of the MVC. It models the logic of getting player statistics when user search the
 * player by full name. The user input format is like "Firstname Lastname" and the model provides ways to first search
 * the portfolio page of the player. Then enter the portfolio page to get the statistics.
 *
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GetPlayerStatistics {

    /**
     * The method is to find the url of the player searched by user.
     *
     * @param name The search string.
     * @return the portfolio URL of the player found
     */
    public String getPlayerUrl(String name) {
        // split the search string using white space
        String[] nameArray = name.split("\\s+");
        // if only one word, not full name, return null
        if (nameArray.length < 2) {
            return null;
        }

        // the player list url to search from using the first character of the last name
        String searchURL = "https://www.baseball-reference.com/players/" + nameArray[1].toLowerCase().charAt(0);
        String playerURL = null;

        try {
            Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.0.0 Safari/537.36").validateTLSCertificates(false).get();
            Element link = doc.select("#div_players_").first();
            if (link != null) {
                Elements players = link.select("a");
                // loop through all players in the page with the same first letter of the last name
                // if find an exact match to the search string, return the player url
                for (Element player : players) {
                    if (player.text().equalsIgnoreCase(nameArray[0] + " " + nameArray[1])) {
                        playerURL = "https://www.baseball-reference.com/" + player.attr("href");
                    }
                }
            } else { // else return null
                System.out.println("player url not found");
            }
        } catch (IOException e) {
            System.out.println("Connection Failed.");
        }
        return playerURL;
    }

    /**
     * The method is to get specific statistics we want from a player's portfolio page.
     *
     * @param var The name of the statistic we want.
     * @param doc The jsoup doc connected to the player webpage.
     * @return the string of statistics we want
     */
    public String getStat(String var, Document doc) {
        String result = null;
        Element E = null;

        // search the stats table that contain the variable name we want
        Elements e = doc.select(".stats_pullout").select(":containsOwn(" + var + ")");
        // loop through the elements to get the field with exact match
        for (Element element : e) {
            if (element.text().equals(var)) {
                E = element;
                break;
            }
        }

        // if found one match, get to the p element that contains the result
        if (E != null) {
            Elements p = E.parent().parent().children().select("p");
            if (!p.isEmpty()) {
                // if only one p field, it means the player is retired and we only have career data
                // if two fields, we get the second field showing career data instead of 2023 data
                result = p.get(p.size() - 1).text();
            }
        }
        // return the string of the statistic
        return result;

    }

    /**
     * The method is to get the statistics of the player found and return a Player object
     * containing these statistics.
     *
     * @param name The search string.
     * @return a Player object with the statistics and bios as variables
     */
    public Player getPlayerStats(String name) {
        // get the player URL of the searched name
        String playerURL = getPlayerUrl(name);
        Player p = new Player();

        // if not found, return null
        if (playerURL == null) {
            return null;
        }

        try {
            // use jsoup to link to the player URL
            Document doc = Jsoup.connect(playerURL).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.0.0 Safari/537.36").validateTLSCertificates(false).get();

            // get the position of the player and assigns it to the object
            Element position = doc.select("p").select(":contains(Positions:)").first();
            if (position != null) {
                p.position = position.text().split(":")[1].strip();
            } else {
                position = doc.select("p").select(":contains(Position:)").first();
                if (position != null) {
                    p.position = position.text().split(":")[1].strip();
                }
            }

            // get the team of the player and assigns it to the object
            Element team = doc.select("p").select(":contains(Team:)").first();
            if (team != null) {
                p.team = team.text().split(":")[1].strip();
            }

            // get the birth date of the player and assigns it to the object
            Element birth = doc.select("#necro-birth").first();
            if (birth != null) {
                p.birth = birth.attr("data-birth");
            }

            // get various statistics of the player and assigns them to the object
            p.WAR = getStat("WAR", doc);
            p.AB = getStat("AB", doc);
            p.H = getStat("H", doc);
            p.HR = getStat("HR", doc);
            p.BA = getStat("BA", doc);
            p.R = getStat("R", doc);
            p.RBI = getStat("RBI", doc);
            p.SB = getStat("SB", doc);
            p.OBP = getStat("OBP", doc);
            p.SLG = getStat("SLG", doc);
            p.OPS = getStat("OPS", doc);

            // get image URL of the player and assigns it to the object
            p.image = doc.select(".media-item.multiple").select("img").attr("src");

            // assigns name to the player object
            Element pName = doc.select("#meta").select("h1").first();
            if (pName != null) {
                p.name = pName.text();
            }

        } catch (IOException e) {
            System.out.println("Connection Failed.");
        }

        return p;
    }

    public static void main(String[] args) {
    }
}
