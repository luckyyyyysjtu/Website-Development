package ds.project1task3;
/*
 * @author Yi Guo
 * Andrew ID: yiguo
 *
 * This file is the Model component of the MVC. It models the logic of getting the MLB competition schedule for tomorrow.
 * With dates changing, the result is dynamic.
 * It has methods to get an arraylist of Schedule objects that has attributes of away, home, time, venue, home starter and away starter.
 *
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Period;
import java.util.ArrayList;
import java.time.LocalDate;


public class GetSchedule {
    /**
     * The method is to get the schedules for the upcoming MLB competitions.
     *
     * @return an arraylist of schedule objects
     */
    public ArrayList<Schedule> getAllSchedule() {
        // get the date of tomorrow = current date + 1
        LocalDate date = LocalDate.now().plus(Period.ofDays(1));
        // the url to fetch the schedule from
        String url = "https://www.cbssports.com/mlb/schedule/" + date.toString().replace("-", "");
        ArrayList<Schedule> s = new ArrayList<Schedule>();
        String awayText = null, homeText = null, timeText = null, venueText = null, homeStarterText = null, awayStarterText = null;

        try {
            // connect to the url
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.0.0 Safari/537.36").validateTLSCertificates(false).get();

            // get to the table that contains the schedule
            Elements table = doc.select(".TableBase-bodyTr");
            // loop through each competition
            for (Element e : table) {
                // assigns away variable
                Element away = e.select(".TeamName").first();
                if (away != null) {
                    awayText = away.text();
                }
                // assigns home variable
                Element home = e.select(".TeamName").get(1);
                if (home != null) {
                    homeText = home.text();
                }
                // assigns time variable
                Element time = e.select(".CellGame").select("a").first();
                if (time != null) {
                    timeText = time.text();
                }
                // assigns venue variable
                Element venue = e.select(".TableBase-bodyTd").get(3);
                if (venue != null) {
                    venueText = venue.text();
                }
                // assigns home starter variable
                Element homeStarter = e.select(".CellPlayerName--short").get(0).select("a").first();
                if (homeStarter != null) {
                    homeStarterText = homeStarter.text();
                }
                // assigns away starter variable
                Element awayStarter = e.select(".CellPlayerName--short").get(1).select("a").first();
                if (awayStarter != null) {
                    awayStarterText = awayStarter.text();
                }
                // build the Schedule object and add it to the arraylist
                s.add(new Schedule(awayText, homeText, timeText, venueText, homeStarterText, awayStarterText));


            }

        } catch (IOException e) {
            System.out.println("Connection Failed.");
        }
        return s;


    }

    public static void main(String[] args) {
    }
}
