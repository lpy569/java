import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

//通过代码往数据库中插入一行记录
public class Demo1 {
    public static void main(String[] args) throws SQLException {


        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入姓名");
        String name = scanner.next();
        System.out.println("请输入学号");
        int id = scanner.nextInt();
        

        //1. 创建一个 DataSource 数据库源
        DataSource dataSource = new MysqlDataSource();
        ((MysqlDataSource) dataSource).setUrl("jdbc:mysql://127.0.0.1:3306/java109?characterEncoding=utf8&useSSL=false");
        ((MysqlDataSource) dataSource).setUser("root");
        ((MysqlDataSource) dataSource).setPassword("llt050201");

        //2. 建立和数据库之间的连接。连接好后才能进行后续的 请求-响应 交互
        Connection connection = dataSource.getConnection();

        //3. 构造sql(这里sql不用写;)
        String sql = "insert into student values(1, '张三')";
        //String sql = "insert into student values("+ id +", '"+ name +"')";//(不推荐)

        PreparedStatement statement = connection.prepareStatement(sql);

        //4. 把sql发到服务器
        int n = statement.executeUpdate();

        System.out.println("n = " + n);

        //5.释放资源，关闭连接(后取到的资源，先释放)
        statement.close();
        connection.close();

    }


}
