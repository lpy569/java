package servlet;

import model.UseDao;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. 读取参数中的账号与密码
        req.setCharacterEncoding("utf8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // 2. 观察是否合理
        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            resp.setContentType("text/html; charset=utf8");
            resp.getWriter().write("您输入的用户名或者密码为空");
            return;
        }

        // 3. 账号或者密码是否正确
        UseDao useDao = new UseDao();

        User user = null;
        try {
            user = useDao.getUseByName(username);
            if (user == null) {
                resp.setContentType("text/html; charset=utf8");
                resp.getWriter().write("您输入的用户名或者密码不正确");
                return;
            }

            if (!password.equals(user.getPassword())) {
                resp.setContentType("text/html; charset=utf8");
                resp.getWriter().write("您输入的用户名或者密码不正确");
                return;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // 4. 创建会话
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);

        // 5. 跳转
        resp.sendRedirect("blog_list.html");

    }

    // 通过这个来判断是否已经登录
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session == null) {
            resp.setStatus(403);
            return;
        }
        User user = (User) session.getAttribute("user");
        if(user == null) {
            resp.setStatus(403);
            return;
        }
        resp.setStatus(200);
    }
}
