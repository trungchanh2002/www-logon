package chanh.example.week1.controls;

import chanh.example.week1.HelloServlet;
import chanh.example.week1.dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import chanh.example.week1.models.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/logon"})
public class LogonServlet extends HelloServlet {
    private UserDao dao;

    @Override
    public void init() {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            PrintWriter out = resp.getWriter();
            HttpSession session = req.getSession(true);
            dao = new UserDao(session);
            String us = req.getParameter("username");
            String psw = req.getParameter("mk");
            boolean kq = dao.logon(us, psw);
            if (!kq) {
                resp.getWriter().println("<h1>Invalid username or password");
            } else {
                Object obj = session.getAttribute("ds");
                if (obj == null) {
                    User user = (User) session.getAttribute("us");
                    out.println(user);
                } else {
                    List<User> lst = (ArrayList<User>) obj;
                    lst.forEach(System.out::println);
                }
            }
            ArrayList<User> lst = (ArrayList<User>) session.getAttribute("ds");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
