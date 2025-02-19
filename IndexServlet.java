package com.bank;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/IndexServlet")
public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/html");

        // Prepare the writer
        PrintWriter out = response.getWriter();

        // Display HTML content for the homepage
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Banking System</title>");
        out.println("<style>");
        out.println("body {font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; height: 100vh;}");
        out.println(".container {background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); width: 100%; max-width: 500px; text-align: center;}");
        out.println("h1 {color: #333; margin-bottom: 30px;}");
        out.println(".btn {display: block; width: 100%; padding: 15px; background-color: #4CAF50; color: white; font-size: 18px; border: none; border-radius: 4px; margin-bottom: 15px; cursor: pointer; text-align: center; text-decoration: none;}");
        out.println(".btn:hover {background-color: #45a049;}");
        out.println(".btn:active {background-color: #4CAF50;}");
        out.println(".back-link {margin-top: 20px;}");
        out.println(".back-link a {color: #4CAF50; font-weight: bold; text-decoration: none;}");
        out.println(".back-link a:hover {text-decoration: underline;}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<div class='container'>");
        out.println("<h1>Welcome to the Banking System</h1>");
        
        // Add buttons for various banking functionalities
        out.println("<a href='CreateAccount.html' class='btn'>Create Account</a>");
        out.println("<a href='CheckBalance.html' class='btn'>Check Balance</a>");
        out.println("<a href='Deposit.html' class='btn'>Deposit Money</a>");
        out.println("<a href='Withdraw.html' class='btn'>Withdraw Money</a>");

        out.println("<div class='back-link'>");
        out.println("<a href='#'>Back to Home</a>");
        out.println("</div>");

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
