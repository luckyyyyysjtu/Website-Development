<%@ page import="java.util.HashSet" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- This is the welcome page -->

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Find Baseball Statistics</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
        }

        h1 {
            font-size: 36px;
            color: #333;
            margin-top: 20px;
        }

        button {
            background-color: #007bff;
            color: #fff;
            padding: 12px 24px;
            border: none;
            cursor: pointer;
            font-size: 16px;
            margin-top: 20px;
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #0056b3;
        }

        form {
            margin-top: 20px;
        }

        label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
            font-size: 18px;
        }

        input[type="text"],
        input[type="submit"],
        select {
            padding: 12px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
            width: 100%;
        }

        select {
            width: 100%;
        }

        input[type="submit"] {
            background-color: #007bff;
            color: #fff;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        input[type="submit"]:hover {
            background-color: #0056b3;
        }

        p {
            font-size: 18px;
            color: #333;
        }

        /* Style for the search form */
        .search-form {
            display: flex;
            flex-direction: column;
            align-items: center;
            width: 100%;
            max-width: 400px;
        }
    </style>
</head>
<body>
<h1>Welcome to Find Baseball Statistics</h1>
<!-- This is the form that uses button to get all MLB schedules -->
<form action="baseball_statistics" method="GET" name="button">
    <button>Show all MLB schedule for tomorrow</button>
    <input type="hidden" name="type" value="button">
</form>

<!-- This is the form that uses search to get the statistics of a player -->
<form class="search-form" action="baseball_statistics" method="GET" name="searchForm">
    <label for="search">Search baseball players by name:</label>
    <input type="text" id="search" name="search">
    <input type="submit" value="Search">
    <input type="hidden" name="type" value="search">
</form>

<!-- This is the selection bar that allows users to select a team and get all players from the team -->
<% HashSet<String> t = (HashSet<String>) request.getAttribute("teams"); %>
<form action="baseball_statistics" method="GET" name="selectForm">
    <input type="hidden" name="type" value="select">
    <label for="team">Select players by team:</label>
    <select name="team" id="team">
        <%
            for (String team : t) {
        %>
        <option value="<%= team %>"><%= team %>
        </option>
        <%
            }
        %>
    </select>
    <input type="submit" value="Submit">
</form>
</body>
</html>



