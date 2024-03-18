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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author iduni
 */
@WebServlet(name = "SignUpController", urlPatterns = {"/SignUpController"})
public class SignUpController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");

        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO users(fname,lname,username,email,password) VALUES (?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3, username);
            stmt.setString(4, email);
            stmt.setString(5, password);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
//                out.println("SUCCESS");
//                out.println("{ \"status\": \"ERROR\", \"message\": \"SIGNUP SUCCESSFULL. Now You Can login\" }");
                request.setAttribute("Login_msg", "Hello " + fname + " ! your registration is success.");
                request.setAttribute("Login_clz", "alert-success");
                request.getRequestDispatcher("signin.jsp").forward(request, response);

            } else {
                request.setAttribute("Login_msg", "SQL ERROR");
                request.setAttribute("Login_clz", "alert-danger");
                request.getRequestDispatcher("signin.jsp").forward(request, response);
            }
            DatabaseConnection.closeConnection(conn);

        } catch (SQLException e) {
            request.setAttribute("Login_msg", "SQL ERROR");
            request.setAttribute("Login_clz", "alert-danger");
            request.getRequestDispatcher("signin.jsp").forward(request, response);
        }
    }

}
