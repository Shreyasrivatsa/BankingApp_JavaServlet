package com.bank;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/DepositServlet")
public class DepositServlet extends HttpServlet {
    String url = "jdbc:mysql://localhost:3306/BankingSystem";
    String un = "MySql username";
    String pw = "Mysql password";
    Connection c = null;
    
    // SQL query to update the balance in the Accounts table
    String queryDeposit = "UPDATE Accounts SET balance = balance + ? WHERE id = ?";
    // SQL query to log the transaction (if needed)
    // String queryTransaction = "INSERT INTO Transactions (account_id, transaction_type, amount) VALUES (?, 'Deposit', ?)";
    
    PreparedStatement pDeposit = null;
    // PreparedStatement pTransaction = null;

    @Override
    public void init() throws ServletException {
        try {
            // Load the JDBC driver and establish connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(url, un, pw);
            pDeposit = c.prepareStatement(queryDeposit);
            // pTransaction = c.prepareStatement(queryTransaction);
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
        String depositAmount = req.getParameter("amount");
        
        out.println("<html><head><title>Deposit Result</title></head><body>");
        
        if (accountId == null || depositAmount == null || accountId.isEmpty() || depositAmount.isEmpty()) {
            out.println("<h3>Error: Missing account information or deposit amount.</h3>");
        } else {
            try {
                int id = Integer.parseInt(accountId);
                double amount = Double.parseDouble(depositAmount);
                
                // Set parameters and execute the deposit update
                pDeposit.setDouble(1, amount);
                pDeposit.setInt(2, id);
                int rowsAffected = pDeposit.executeUpdate();
                
                if (rowsAffected > 0) {
                    // Optionally, log the transaction here if needed
                    // pTransaction.setInt(1, id);
                    // pTransaction.setDouble(2, amount);
                    // pTransaction.executeUpdate();
                    
                    // Format current date and time to 12-hour format with AM/PM
                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    String formattedTime = new SimpleDateFormat("hh:mm:ss a").format(new Date());
                    
                    // Display success message with deposit details
                    out.println("<h3>Deposit Successful</h3>");
                    out.println("<p>Account ID: " + accountId + "</p>");
                    out.println("<p>Amount Deposited: $" + depositAmount + "</p>");
                    out.println("<p>Transaction Date: " + formattedDate + "</p>");
                    out.println("<p>Transaction Time: " + formattedTime + "</p>");
                } else {
                    out.println("<h3>Deposit failed. Account not found.</h3>");
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                out.println("<h3>Error occurred during deposit.</h3>");
            }
        }
        
        // Add a "Back to Home" link after the output
        out.println("<div style='margin-top:20px;'>");
        out.println("<a href='index.html' style='color:#4CAF50; font-weight:bold; text-decoration:none;'>Back to Home</a>");
        out.println("</div>");
        
        out.println("</body></html>");
    }
    
    @Override
    public void destroy() {
        try {
            if (pDeposit != null) pDeposit.close();
            // if (pTransaction != null) pTransaction.close();
            if (c != null) c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
