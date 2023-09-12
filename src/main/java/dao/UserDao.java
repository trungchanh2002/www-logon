package dao;


import jakarta.servlet.http.HttpSession;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private Connection connection;
    private HttpSession session;


    public UserDao(HttpSession session) throws Exception {
        Class.forName("org.mariadb.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/logondb","root", "sapassword");
        this.session = session;
    }

    public boolean logon(String us, String psw) throws Exception {
        String sql = "SELECT * FROM dbuser WHERE user_name = ? AND password = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, us);
        ps.setString(2, psw);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String role = rs.getString("role");
            if (role.equalsIgnoreCase("admin1")) {
                Statement stmt = connection.createStatement();
                ResultSet rsU = stmt.executeQuery("SELECT * FROM dbuser");
                List<User> lst = new ArrayList<>();
                while (rsU.next()) {
                    User u = new User(
                            rsU.getString(1),
                            rsU.getString(2),
                            rsU.getString(3),
                            rsU.getString(4),
                            rsU.getString(5)
                    );
                    lst.add(u);
                }
                session.setAttribute("ds", lst);
            } else {
                User u = new User(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
                );
                session.setAttribute("us", u);
            }
            return true;
        }
        return false;
    }

    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
