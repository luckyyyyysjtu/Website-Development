<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%-- This is the welcome page for hash value calculation that collects user inputs. --%>
<html>
<head>
    <title>Compute Hashes</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<h1><%= "Compute Hashes" %>
</h1>
<br/>
<%-- This is the form for uses to submit text inputs and radio button choice. --%>
<form action="compute-hashes" method="post">
    <%-- text input --%>
    <label for="hash_text">Enter the text you would like to hash:</label><br>
    <input type="text" id="hash_text" name="hash_text"><br>
    <br>
    <p>Please select the hash function you would like to use:</p>
    <%-- hash function choice --%>
    <input type="radio" id="md5" name="hash_function" value="MD5" checked>
    <label for="md5">MD5</label><br>
    <input type="radio" id="sha256" name="hash_function" value="SHA-256">
    <label for="sha256">SHA-256</label><br>
    <br>
    <input type="submit" value="Submit">
</form>

</body>
</html>