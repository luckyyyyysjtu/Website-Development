package ds.project1task3;

/*
 * @author Yi Guo
 * Andrew ID: yiguo
 *
 * This file is the Schedule class that is used to store all MLB competitions info.
 *
 */
public class Schedule {
    // MLB related information
    String away;
    String home;
    String time;
    String venue;
    String homeStarter;
    String awayStarter;

    /**
     * Constructor of the Schedule class.
     */
    public Schedule(String away, String home, String time, String venue, String homeStarter, String awayStarter) {
        this.away = away;
        this.home = home;
        this.time = time;
        this.venue = venue;
        this.homeStarter = homeStarter;
        this.awayStarter = awayStarter;

    }

    /**
     * Below are the getters of the variables.
     *
     * @return string of the variable values
     */
    public String getAway() {
        return away;
    }

    public String getHome() {
        return home;
    }

    public String getTime() {
        return time;
    }

    public String getVenue() {
        return venue;
    }

    public String getHomeStarter() {
        return homeStarter;
    }

    public String getAwayStarter() {
        return awayStarter;
    }


}
