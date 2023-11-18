import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/message")
public class MessageServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();

    // 1. 创建数据源
    private DataSource dataSource = new MysqlDataSource();
    @Override
    public void init() throws ServletException {
        ((MysqlDataSource) dataSource).setUrl("jdbc:mysql://127.0.0.1:3306/message_wall?characterEncoding=utf8&&useSSL=false");
        ((MysqlDataSource) dataSource).setUser("root");
        ((MysqlDataSource) dataSource).setPassword("llt050201");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 读取前端发来的数据，并且保存到服务器

        // 1. 读
        Message message = objectMapper.readValue(req.getInputStream(), Message.class);
        System.out.println("请求中收到的 message: " + message);

        // 2. 存入数据库
        try {
            save(message);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // 3. 返回响应
        resp.setStatus(200);
        resp.getWriter().write("OK!");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 将已经保存的数据发给浏览器
        resp.setStatus(200);
        resp.setContentType("application/json; charset=utf8");

        // 从数据库查询
        List<Message> list = null;
        try {
            list = load();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String respJson = objectMapper.writeValueAsString(list);
        resp.getWriter().write(respJson);
    }

    private void save(Message message) throws SQLException {

        // 2. 建立连接
        Connection connection = (Connection) dataSource.getConnection();

        // 3. 构造 sql
        String sql = "insert into message values(?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, message.from);
        statement.setString(2, message.to);
        statement.setString(3, message.message);

        // 4. 执行 sql
        statement.executeUpdate();

        // 5. 回收资源
        statement.close();
        connection.close();

    }

    private List<Message> load() throws SQLException {

        // 2. 建立连接
        Connection connection = (Connection) dataSource.getConnection();

        // 3. 构造 sql
        String sql = "select * from message";
        PreparedStatement statement = connection.prepareStatement(sql);

        // 4. 执行 sql
        ResultSet resultSet = statement.executeQuery();

        // 5. 遍历
        List<Message> list = new ArrayList<>();
        while (resultSet.next()) {
            Message message = new Message();
            message.from = resultSet.getString("from");
            message.to = resultSet.getString("to");
            message.message = resultSet.getString("message");
            list.add(message);
        }

        // 5. 回收资源
        resultSet.close();
        statement.close();
        connection.close();

        // 6. 返回 List
        return list;
    }
}
