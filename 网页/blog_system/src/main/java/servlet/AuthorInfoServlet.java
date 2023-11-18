package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Blog;
import model.BlogDao;
import model.UseDao;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/getAuthorInfo")
public class AuthorInfoServlet extends HttpServlet {
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 先拿到请求中的 blogId
        String blogId = req.getParameter("blogId");
        if (blogId == null) {
            resp.setContentType("text/html; charset=utf8");
            resp.getWriter().write("请求中缺少 blogId!");
            return;
        }
        // 2. 在 blog 表中查询到对应的 Blog 对象
        BlogDao blogDao = new BlogDao();
        Blog blog = null;
        blog = blogDao.getBlog(Integer.parseInt(blogId));
        if (blog == null) {
            resp.setContentType("text/html; charset=utf8");
            resp.getWriter().write("blogId 没有找到!");
            return;
        }
        // 3. 根据 blog 对象中的 userId, 从 user 表中查到对应的作者.
        UseDao userDao = new UseDao();
        User user = null;
        try {
            user = userDao.getUseById(blog.getUserId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user == null) {
            resp.setContentType("text/html; charset=utf8");
            resp.getWriter().write("userId 没有找到!");
            return;
        }
        // 4. 把这个 user 对象, 返回到浏览器这边.
        user.setPassword("");
        String respJson = objectMapper.writeValueAsString(user);
        resp.setContentType("application/json; charset=utf8");
        resp.getWriter().write(respJson);

    }
}
