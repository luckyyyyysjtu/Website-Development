<%@ page import="ds.project1task3.Player" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- This is the page to show the search result for player statistics got from web scraping-->
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Player Bio and Statistics</title>
    <link rel="stylesheet" href="styles.css"> <!-- Link to an external CSS file for styling -->
</head>
<body>
<header>
    <h1>Player Bio and Statistics</h1>
</header>

Data Scrapped from:
<a href="https://www.baseball-reference.com/players/"> Baseball Reference </a>

<!-- Get all Players in the ArrayList -->
<% Player p = (Player) request.getAttribute("player"); %>

<!-- If no results, display not found -->
<% if (p == null) {
%> <p> Cannot find the search result. </p> <%
} else { %>
<!-- display results if find a match -->
<!-- if data is missing will show null -->
<section class="player-bio">
    <h2>Player Bio</h2>
    <div class="bio-info">
        <img src="<%= p.getImage()%>" alt="<%= p.getImage()%>">
        <div class="details">
            <p><strong>Name:</strong> <%= p.getName()%>
            </p>
            <p><strong>Team:</strong> <%= p.getTeam()%>
            </p>
            <p><strong>Position:</strong> <%= p.getPosition()%>
            </p>
            <p><strong>Date of Birth:</strong> <%= p.getBirth()%>
            </p>
        </div>
    </div>
</section>

<section class="player-stats">
    <h2>Player Career Statistics</h2>
    <table>
        <thead>
        <tr>
            <th>WAR</th>
            <th>AB</th>
            <th>H</th>
            <th>HR</th>
            <th>BA</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><%= p.getWAR()%>
            </td>
            <td><%= p.getAB()%>
            </td>
            <td><%= p.getH()%>
            </td>
            <td><%= p.getHR()%>
            </td>
            <td><%= p.getBA()%>
            </td>
        </tr>
        </tbody>
    </table>
    <br><br>
    <table>
        <thead>
        <tr>
            <th>R</th>
            <th>RBI</th>
            <th>SB</th>
            <th>OBP</th>
            <th>SLG</th>
            <th>OPS</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><%= p.getR()%>
            </td>
            <td><%= p.getRBI()%>
            </td>
            <td><%= p.getSB()%>
            </td>
            <td><%= p.getOBP()%>
            </td>
            <td><%= p.getSLG()%>
            </td>
            <td><%= p.getOPS()%>
            </td>
        </tr>
        </tbody>
    </table>
</section>
<%} %>

</body>
</html>
