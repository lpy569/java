import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//jdbc查询
public class Demo2 {
    public static void main(String[] args) throws SQLException {
        //1. 创建DataSource
        DataSource dataSource = new MysqlDataSource();
        ((MysqlDataSource)dataSource).setUrl("jdbc:mysql://127.0.0.1:3306/java109?characterEncoding=utf8&useSSL=false");
        ((MysqlDataSource)dataSource).setUser("root");
        ((MysqlDataSource)dataSource).setPassword("llt050201");

        //2. 建立连接
        Connection connection =  dataSource.getConnection();

        //3. 构造 sql
        String sql = "select * from student";
        PreparedStatement statement = connection.prepareStatement(sql);

        //4. 执行 sql
        //    ResultSet 表示查询结果集合 （临时表）,此处就要遍历
        ResultSet resultSet =statement.executeQuery();

        //5. 遍历结果集合
        //    通过 next 方法就可以获取到临时表每一行数据  最后一行之后 再执行next 返回为false，循环结束
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            System.out.println("id = " + id + "  name = " + name);
        }

        //6. 释放资源
        resultSet.close();
        statement.close();
        connection.close();


    }
}
