/**
 * Last Modified: Sep 22, 2023
 * This program demonstrates a program that allows users to calculate the hash values
 * of the texts they entered. Users can self input the texts and make the choice between
 * SHA-256 and MD5 to get the hexadecimal  text and base 64 notation.
 * This is designed by using a jsp file that displays two radio buttons and a submit button.
 * When users make a choice, the answer is posted to the servlet. It will do the hash calculation
 * and returns the text input, hash function and two hash values.
 */

package ds.project1task1;
// import packages needed for servlet

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
// import packages needed for calculating hash values
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

/*
 * The following WebServlet receives POST requests from client.
 * It gets the text input and hash choice from user and write out the
 * HTML to display the hashed values.
 */

@WebServlet(name = "ComputeHashes", value = "/compute-hashes")
public class ComputeHashes extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // get text input from user and the hash function they choose
        String text = request.getParameter("hash_text");
        String hashName = request.getParameter("hash_function");

        try {
            // Access MessageDigest class for the hash method
            MessageDigest md = MessageDigest.getInstance(hashName);
            // Compute the digest for hex and base 64 annotation
            String hexString = DatatypeConverter.printHexBinary(md.digest(text.getBytes()));
            String base64String = DatatypeConverter.printBase64Binary(md.digest(text.getBytes()));

            // get a print writer from the response object
            PrintWriter out = response.getWriter();
            // send a html document to caller to display the final result
            out.println("<html><body>");
            out.println("<p>" + "Text you entered: " + text + "</p>");
            out.println("<p>" + "Hash Function: " + hashName + "</p>");
            out.println("<p>" + "Hash Value - Hex: " + hexString + "</p>");
            out.println("<p>" + "Hash Value - Base 64: " + base64String + "</p>");
            out.println("</body></html>");
        } catch (NoSuchAlgorithmException e) { // deal with exception
            System.out.println("No" + hashName + "available" + e);
        }


    }

    public void destroy() {
    }
}
