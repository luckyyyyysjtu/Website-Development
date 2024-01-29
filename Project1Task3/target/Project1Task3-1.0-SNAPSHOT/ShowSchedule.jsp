<%@ page import="java.util.ArrayList" %>
<%@ page import="ds.project1task3.Schedule" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- This is the page to show all MLB schedules got from web scraping-->

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MLB Schedule</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
        }

        header {
            background-color: #007BFF;
            color: white;
            padding: 20px;
            text-align: center;
        }

        section {
            max-width: 800px;
            margin: 20px auto;
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.2);
        }

        h1 {
            font-size: 24px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #007BFF;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        tr:hover {
            background-color: #ddd;
        }
    </style>
</head>
<body>
<!-- Get all Schedules in the ArrayList -->
<% ArrayList<Schedule> s = (ArrayList<Schedule>) request.getAttribute("schedule"); %>
<header>
    <h1>MLB Schedule</h1>
</header>

<section>
    Data Scrapped from:
    <a href="https://www.cbssports.com/mlb/schedule"> MLB Schedule </a>
    <h2>Schedule for tomorrow:</h2>
    <table>
        <thead>
        <tr>
            <!-- Table Header -->
            <th>Away</th>
            <th>Home</th>
            <th>Time</th>
            <th>Venue</th>
            <th>Home Starter</th>
            <th>Away Starter</th>
        </tr>
        </thead>
        <tbody>
        <!-- Use for loop to loop through the ArrayList and list contents in a table-->
        <%
            for (Schedule schedule : s) {
        %>
        <tr>
            <td><%= schedule.getAway()%>
            </td>
            <td><%= schedule.getHome()%>
            </td>
            <td><%= schedule.getTime()%>
            </td>
            <td><%= schedule.getVenue()%>
            </td>
            <td><%= schedule.getHomeStarter()%>
            </td>
            <td><%= schedule.getAwayStarter()%>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
</section>
</body>
</html>
