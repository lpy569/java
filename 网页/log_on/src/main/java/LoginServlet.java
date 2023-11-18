import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 获取客户端传来的信息（账号，密码）
        req.setCharacterEncoding("utf8");
        String userName = req.getParameter("username");
        String passWord = req.getParameter("password");

        // 2. 验证是否正确
        if (!"llt".equals(userName) || !"2569".equals(passWord)) {
            // 登录失败
            resp.setContentType("txt/html; charset=utf8");
            resp.getWriter().write("账号或者密码错误");
            return;
        }

        // 3. 登录成功，创建会话(查找-不存在就创建)
        HttpSession  session = req.getSession(true);
        // 给会话自定义数据，通过 Attribute 来保存
        session.setAttribute("username", userName);
        session.setAttribute("loginTime", System.currentTimeMillis());

        // 4。 跳转页面
        resp.sendRedirect("index");

    }
}
