package ds.project1task3;
/*
 * @author Yi Guo
 * Andrew ID: yiguo
 *
 * This is a program designed to show baseball statistics collected from
 * web scraping and API. It has three functions. The first one is to get
 * the MLB schedule of tomorrow. The second one is to search for the player
 * name and get their bio and statistics. The third one is to select the
 * players in a specific team using the selection bar.
 *
 * The welcome-file in the deployment descriptor (web.xml) points
 * to this servlet.  So it is also the starting place for the web
 * application.
 *
 * The servlet is acting as the controller.
 * There are four views - index.jsp, PlayerList.jsp, PlayerStats.jsp and ShowSchedule.jsp.
 * index.jsp is the welcome page for displaying the button, text fields and selection bar.
 * PlayList is for displaying all the names of the players in a specific team selected by the user.
 * PlayerStats is used for displaying the player statistics and bio after searching.
 * ShowSchedule is used for displaying all the MLB play schedules of tomorrow.
 *
 * The model is provided by ByTeamStats,GetPlayerStatistics and GetSchedule.
 */

import java.io.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "baseballServlet", urlPatterns = {"/baseball_statistics"})
public class BaseballServlet extends HttpServlet {
    // define variables for the three models
    GetPlayerStatistics p;
    ByTeamStats c;
    GetSchedule s;
    // define variable for view change
    String nextView;

    // initiate models
    public void init() {
        p = new GetPlayerStatistics();
        c = new ByTeamStats();
        s = new GetSchedule();
    }

    // This servlet will reply to HTTP GET requests via this doGet method
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get the type parameter of the form submitted
        // so we can differentiate if submission is from button, text field, or selection bar
        String type = request.getParameter("type");

        // if type is null, direct to the welcome index page
        // show all the team names available in selection bar using model c.getTeams()
        if (type == null) {
            request.setAttribute("teams", c.getTeams());
            nextView = "index.jsp";
        } else if (type.equals("search")) { // if type is search
            // get the keyword entered in the text field
            String keyword = request.getParameter("search");
            // if it is empty, redirect to the welcome page
            if (keyword.isEmpty()) {
                request.setAttribute("teams", c.getTeams());
                nextView = "index.jsp";
            } else { // if we have search word, direct to the statistic display page
                nextView = "PlayerStats.jsp";
                // set the player attribute to the Player object returned by model p.getPlayerStats()
                // send it the the view
                request.setAttribute("player", p.getPlayerStats(keyword));
            }
        } else if (type.equals("select")) { // if type is select, direct to the player list page
            // get the team selected by user
            String team = request.getParameter("team");
            // set the playerList attribute to the Player ArrayList returned by model c.getPlayers()
            // send it the the view
            request.setAttribute("playerList", c.getPlayers(team));
            nextView = "PlayerList.jsp";
        } else if (type.equals("button")) { // if type is button, direct to the schedule page
            // set the schedule attribute to the Schedule ArrayList returned by model s.getAllSchedule()
            // send it the the view
            request.setAttribute("schedule", s.getAllSchedule());
            nextView = "ShowSchedule.jsp";
        }

        // Transfer control over the correct "view"
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        view.forward(request, response);
    }

    public void destroy() {
    }
}