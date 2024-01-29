package ds.project1task2;
/*
 * @author Yi Guo
 * Andrew ID: yiguo
 *
 * This program uses MVC to build a clicker for class.
 * It allows users to submit answers to questions and provide a separate URL
 * endpoint for the results of the response.
 *
 * The welcome-file in the deployment descriptor (web.xml) points
 * to this servlet.  So it is the starting place for the web
 * application. We will first enter prompt.jsp to let users make the choice.
 *
 * The servlet is acting as the controller.
 * There are three views - prompt.jsp, result.jsp and allResults.jsp.
 *
 * We decide between prompt.jsp and result.jsp by determining if there is a choice made or not.
 * If there is no parameter, then it uses the prompt.jsp view, as a
 * starting place. If there is a search parameter, then it gets the submitted choice and provides feedback
 * to the user regarding the choice that they made.
 * allResults.jsp view is for displaying the results when users hit the url /getResults. It will also clear
 * the data stored previously in the model.
 *
 * The model is provided by ClickerModel.
 */

import java.io.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

/*
 * The following WebServlet annotation gives instructions to the web container.
 * It states that when the user browses to the URL path /submit or /getResults
 * then the servlet with the name clickerServlet should be used.
 */
@WebServlet(name = "clickerServlet", urlPatterns = {"/submit", "/getResults"})
public class ClickerServlet extends HttpServlet {

    // create an object of the ClickerModel
    ClickerModel model = null;

    public void init() {
        model = new ClickerModel();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get the user choice
        String choice = request.getParameter("choice");

        // determine what type of device our user is
        String ua = request.getHeader("User-Agent");

        // get the ServletPath for determining if we are using desktop or mobile
        String path = request.getServletPath();

        // prepare the appropriate DOCTYPE for the view pages
        if (ua != null && ((ua.indexOf("Android") != -1) || (ua.indexOf("iPhone") != -1))) {
            // set different document types accordingly
            request.setAttribute("doctype", "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.2//EN\" \"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile12.dtd\">");
        } else {
            request.setAttribute("doctype", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        }
        // variable for storing nextView
        String nextView;

        // if path is /submit
        if (path.equals("/submit")) {

            /*
             * Check if the choice parameter is present.
             * If not, then give the user choices of A,B,C,D to choose from.
             * If there is a choice parameter, then show the choice registered and give the user choices of A,B,C,D to choose from.
             */
            if (choice != null) {
                // with choice, provide feedback and choose again
                nextView = "result.jsp";
                model.addAnswer(choice);
            } else {
                // no choice parameter so choose the prompt view
                nextView = "prompt.jsp";
            }
            // Transfer control over the correct "view"
            RequestDispatcher view = request.getRequestDispatcher(nextView);
            view.forward(request, response);

        } else if (path.equals("/getResults")) { // if path is /getResults
            // model.getMap() returns a treemap that records the submission times of each choice made
            // it is sorted by alpha order
            // pass this treemap as attribute results to the allResults.jsp view
            request.setAttribute("results", model.getMap());
            nextView = "allResult.jsp";
            // Transfer control over the correct "view"
            RequestDispatcher view = request.getRequestDispatcher(nextView);
            view.forward(request, response);
            // clear the contents in the treemap after calling getResults
            model.clearAnswer();
        }

    }

}