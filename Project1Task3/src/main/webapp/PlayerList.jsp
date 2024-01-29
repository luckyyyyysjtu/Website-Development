<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!-- This is the page to show all players from a specific team got from API-->

<html lang="en">
<head>
    <title>Player Name List</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

Data Got from:
<a href="https://sportsdata.io/developers/api-documentation/mlb"> MLB API </a>
<br><br>

<!-- Get the player names of a team in the ArrayList -->
<% ArrayList<String> p = (ArrayList<String>) request.getAttribute("playerList"); %>
<!-- Deal with empty list -->
<% if (p.isEmpty()) {
%> <p> Cannot find the result. </p> <%
} else { %>
<!-- Loop through the list and display the player names -->
<h> Players for team <%=request.getParameter("team")%> are listed below:</h>
<br><br>
<%
    for (String player : p) {
%>
<li><%= player %>
</li>
<%
    }
%>
<%} %>

</body>
</html>
