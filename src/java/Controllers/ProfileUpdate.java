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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author iduni
 */
@WebServlet(name = "ProfileUpdate", urlPatterns = {"/ProfileUpdate"})
public class ProfileUpdate extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");

        String ID = request.getParameter("ID");
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String dp = request.getParameter("dp");

        if (dp == null || dp.trim().isEmpty()) {
            dp = request.getParameter("dp_txt");
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE users SET fname= ?, lname= ?, username= ?, email= ?, password= ?, dp= ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3, username);
            stmt.setString(4, email);
            stmt.setString(5, password);
//            stmt.setString(6, role);
            stmt.setString(6, dp);
            stmt.setString(7, ID);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
//                out.println("SUCCESS");
//                out.println("{ \"status\": \"ERROR\", \"message\": \"SIGNUP SUCCESSFULL. Now You Can login\" }");
                request.setAttribute("Reg_msg", "Update Successfully.");
                request.setAttribute("Reg_clz", "alert-success");
                request.getRequestDispatcher("ProfileUpdate.jsp").forward(request, response);

            } else {
                request.setAttribute("Reg_msg", "SQL ERROR");
                request.setAttribute("Reg_clz", "alert-danger");
                request.getRequestDispatcher("ProfileUpdate.jsp").forward(request, response);
            }
            DatabaseConnection.closeConnection(conn);

        } catch (SQLException e) {
            request.setAttribute("Reg_msg", "Please Wait! There is a error in MySQL Server");
            request.setAttribute("Reg_clz", "alert-danger");
            request.getRequestDispatcher("ProfileUpdate.jsp").forward(request, response);
        }
    }
}
