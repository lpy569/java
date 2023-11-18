import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 获取会话对象 根据用户信息来构造页面
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.setContentType("text/html; charset=utf8");
            resp.getWriter().write("未登录");
            return;
        }

        // 2. 从会话中拿到存储的信息
        String username = (String) session.getAttribute("username");
        Long loginTime = (Long)session.getAttribute("loginTime");

        // 3. 生成页面，把上面数据返回到页面
        String body = "欢迎你 " + username + "!" + " 上次登录时间: " + loginTime;
        resp.setContentType("text/html; charset=utf8");
        resp.getWriter().write(body);
    }
}
