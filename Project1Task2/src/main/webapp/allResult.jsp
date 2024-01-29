<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%= request.getAttribute("doctype") %>
<!-- When user chooses to get all results -->

<html>
<head>
    <title>Project1Task2</title>
</head>
<body>
<h1> Distributed Systems Class Clicker </h1>

<!-- Get the treemap attribute sent by the servlet -->
<% Object o = request.getAttribute("results"); %>
<% TreeMap<String, Integer> m = (TreeMap<String, Integer>) o; %>

<!-- If no results, display there are bo results -->
<!-- Else loop through the treemap to display the choice and its number -->
<% if (m.isEmpty()) {
%> <p> There are currently no results </p> <%
} else {
%>
<p>The results from the survey are as follows</p>
<% for (Map.Entry<String, Integer> entry : m.entrySet()) { %> <p><%=entry.getKey()%>: <%=entry.getValue()%>
</p> <%} %>
<%
    } %>

</body>
</html>
