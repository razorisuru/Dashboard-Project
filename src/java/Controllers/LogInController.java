/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.DatabaseConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author iduni
 */
@WebServlet(name = "LogInController", urlPatterns = {"/LogInController"})
public class LogInController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        String sessionToken = UUID.randomUUID().toString();

        String POSTemail = request.getParameter("email");
        String POSTpassword = request.getParameter("password");
        String remember = request.getParameter("remember");

        if (POSTemail.equals("") || POSTpassword.equals("")) {
            request.setAttribute("Login_msg", "Empty Fields.");
            request.setAttribute("Login_clz", "alert-danger");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            try (Connection conn = DatabaseConnection.getConnection()) {

                String checkSql = "SELECT * FROM users WHERE email = ? and password = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, POSTemail);
                checkStmt.setString(2, POSTpassword);

                ResultSet checkResult = checkStmt.executeQuery();

                if (checkResult.next()) {
                    String id = checkResult.getString("id");
                    String username = checkResult.getString("username");
                    String email = checkResult.getString("email");
                    String fname = checkResult.getString("fname");
                    String lname = checkResult.getString("lname");
                    String dp = checkResult.getString("dp");

                    HttpSession session = request.getSession();
                    session.setAttribute("ID", id);
                    session.setAttribute("UN", username);
                    session.setAttribute("DP_NAME", fname + " " + lname);
                    session.setAttribute("DP", dp);
                    session.setAttribute("sessionToken", sessionToken);
                    response.sendRedirect("index.jsp");
                    if (remember == null) {
                        session.setMaxInactiveInterval(30 * 60);
                    }

                } else {
                    request.setAttribute("Login_msg", "Incorrect Username and Password.");
                    request.setAttribute("Login_clz", "alert-danger");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } catch (SQLException ex) {
                request.setAttribute("Login_msg", ex);
                request.setAttribute("Login_clz", "alert-danger");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }

    }
}
