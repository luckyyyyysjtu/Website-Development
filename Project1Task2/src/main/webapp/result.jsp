<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%= request.getAttribute("doctype") %>
<!-- Feedback Page when a choice is made -->
<html>
<head>
    <title>Project1Task2</title>
</head>
<body>
<h1> Distributed Systems Class Clicker </h1>
<!-- Feedback for the choice made -->
<p>Your "<%= request.getParameter("choice")%>" has been registered</p>
<!-- Form for submitting the radio choices -->
<form action="submit" method="GET">
    <p>Submit your answer to the current question:</p>
    <input type="radio" id="A" name="choice" value="A">
    <label for="A">A</label><br>
    <input type="radio" id="B" name="choice" value="B">
    <label for="B">B</label><br>
    <input type="radio" id="C" name="choice" value="C">
    <label for="C">C</label><br>
    <input type="radio" id="D" name="choice" value="D">
    <label for="D">D</label><br>
    <br>
    <input type="submit" value="Submit">
</form>
</body>
</html>
