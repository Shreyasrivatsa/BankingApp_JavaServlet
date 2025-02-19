import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/CheckBalanceServlet")
public class CheckBalanceServlet extends HttpServlet {
    String url = "jdbc:mysql://localhost:3306/BankingSystem";
    String un = "Mysql username";
    String pw = "Mysql password";
    Connection c = null;
    
    // Query to retrieve the balance from the Accounts table
    String queryCheckBalance = "SELECT balance FROM Accounts WHERE id = ?";
    
    PreparedStatement pCheckBalance = null;
    
    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(url, un, pw);
            pCheckBalance = c.prepareStatement(queryCheckBalance);
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
        
        out.println("<html><head><title>Check Balance Result</title></head><body>");
        
        if (accountId == null || accountId.isEmpty()) {
            out.println("<h3>Error: Missing account ID.</h3>");
        } else {
            try {
                int id = Integer.parseInt(accountId);
                
                pCheckBalance.setInt(1, id);
                ResultSet rs = pCheckBalance.executeQuery();
                
                if (rs.next()) {
                    double currentBalance = rs.getDouble("balance");
                    
                    // Format current date and time
                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    String formattedTime = new SimpleDateFormat("hh:mm:ss a").format(new Date());
                    
                    out.println("<h3>Account Balance</h3>");
                    out.println("<p>Account ID: " + accountId + "</p>");
                    out.println("<p>Current Balance: $" + currentBalance + "</p>");
                    out.println("<p>Transaction Date: " + formattedDate + "</p>");
                    out.println("<p>Transaction Time: " + formattedTime + "</p>");
                } else {
                    out.println("<h3>Account not found.</h3>");
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                out.println("<h3>Error occurred while retrieving the balance.</h3>");
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
            if (c != null) c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
