package com.bank;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/CreateAccountServlet")
public class CreateAccountServlet extends HttpServlet {
    String url = "jdbc:mysql://localhost:3306/BankingSystem";
    String un = "Mysql UserName";
    String pw = "Mysql Password";
    Connection c = null;
    
    // SQL for inserting a new account; adjust as needed (e.g., check duplicate IDs)
    String queryInsert = "INSERT INTO Accounts (id, name, balance) VALUES (?, ?, 0.0)";
    PreparedStatement pInsert = null;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(url, un, pw);
            pInsert = c.prepareStatement(queryInsert);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        
        String accountId = req.getParameter("id");
        String accountName = req.getParameter("name");
        
        out.println("<html><head><title>Create Account Result</title></head><body>");
        
        if (accountId == null || accountName == null || accountId.isEmpty() || accountName.isEmpty()) {
            out.println("<h3>Error: Missing account information.</h3>");
        } else {
            try {
                // Set parameters and execute the insert query
                pInsert.setInt(1, Integer.parseInt(accountId));
                pInsert.setString(2, accountName);
                int rowsAffected = pInsert.executeUpdate();
                
                if (rowsAffected > 0) {
                    out.println("<h3>Account created successfully.</h3>");
                    out.println("<p>Account ID: " + accountId + "</p>");
                    out.println("<p>Account Name: " + accountName + "</p>");
                } else {
                    out.println("<h3>Account creation failed.</h3>");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                out.println("<h3>Error occurred while creating the account.</h3>");
            }
        }
        
        // Add a Back to Home link after the output
        out.println("<div style='margin-top:20px;'>");
        out.println("<a href='index.html' style='color:#4CAF50; font-weight:bold; text-decoration:none;'>Back to Home</a>");
        out.println("</div>");
        
        out.println("</body></html>");
    }
    
    @Override
    public void destroy() {
        try {
            if (pInsert != null) pInsert.close();
            if (c != null) c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
