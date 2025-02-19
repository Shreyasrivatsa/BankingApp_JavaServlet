package com.bank;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/WithdrawServlet")
public class WithdrawServlet extends HttpServlet {
    String url = "jdbc:mysql://localhost:3306/BankingSystem";
    String un = "Mysql username";
    String pw = "Mysql password";
    Connection c = null;
    
    // Query to check account balance (if needed)
    String queryCheckBalance = "SELECT balance FROM Accounts WHERE id = ?";
    // Query to update the balance after withdrawal (ensuring sufficient funds)
    String queryWithdraw = "UPDATE Accounts SET balance = balance - ? WHERE id = ? AND balance >= ?";
    
    PreparedStatement pCheckBalance = null;
    PreparedStatement pWithdraw = null;
    
    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(url, un, pw);
            pCheckBalance = c.prepareStatement(queryCheckBalance);
            pWithdraw = c.prepareStatement(queryWithdraw);
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
        String withdrawalAmount = req.getParameter("amount");
        
        out.println("<html><head><title>Withdraw Result</title></head><body>");
        
        if (accountId == null || withdrawalAmount == null || accountId.isEmpty() || withdrawalAmount.isEmpty()) {
            out.println("<h3>Error: Missing account ID or withdrawal amount.</h3>");
        } else {
            try {
                int id = Integer.parseInt(accountId);
                double amount = Double.parseDouble(withdrawalAmount);
                
                // First, check if the account exists and retrieve current balance
                pCheckBalance.setInt(1, id);
                ResultSet rs = pCheckBalance.executeQuery();
                
                if (rs.next()) {
                    double currentBalance = rs.getDouble("balance");
                    
                    if (currentBalance >= amount) {
                        // Proceed with withdrawal
                        pWithdraw.setDouble(1, amount);
                        pWithdraw.setInt(2, id);
                        pWithdraw.setDouble(3, amount);
                        int rowsAffected = pWithdraw.executeUpdate();
                        
                        if (rowsAffected > 0) {
                            // Format current date and time
                            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            String formattedTime = new SimpleDateFormat("hh:mm:ss a").format(new Date());
                            
                            out.println("<h3>Withdrawal Successful</h3>");
                            out.println("<p>Account ID: " + accountId + "</p>");
                            out.println("<p>Amount Withdrawn: $" + withdrawalAmount + "</p>");
                            out.println("<p>Transaction Date: " + formattedDate + "</p>");
                            out.println("<p>Transaction Time: " + formattedTime + "</p>");
                        } else {
                            out.println("<h3>Error occurred during withdrawal.</h3>");
                        }
                    } else {
                        out.println("<h3>Insufficient funds in the account.</h3>");
                    }
                } else {
                    out.println("<h3>Account not found.</h3>");
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                out.println("<h3>Error occurred during withdrawal.</h3>");
            }
        }
        
        // Add Back to Home link
        out.println("<div style='margin-top:20px;'>");
        out.println("<a href='index.html' style='color:#4CAF50; font-weight:bold; text-decoration:none;'>Back to Home</a>");
        out.println("</div>");
        
        out.println("</body></html>");
    }
    
    @Override
    public void destroy() {
        try {
            if (pCheckBalance != null) pCheckBalance.close();
            if (pWithdraw != null) pWithdraw.close();
            if (c != null) c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
