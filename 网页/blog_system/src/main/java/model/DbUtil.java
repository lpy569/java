package model;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 通过整个类，封装数据库建立和关闭连接的操作(使用单例模式)。
public class DbUtil {
    private volatile static DataSource dataSource = null;

    private static DataSource getDataSource() {
        if(dataSource == null) {
            synchronized (DbUtil.class) {
                if (dataSource == null) {
                    dataSource = new MysqlDataSource();
                    ((MysqlDataSource) dataSource).setUrl("jdbc:mysql://127.0.0.1:3306/blog_system?characterEncoding=utf8&&useSSL=false");
                    ((MysqlDataSource) dataSource).setUser("root");
                    ((MysqlDataSource) dataSource).setPassword("");
                }
            }
        }
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return (Connection) getDataSource().getConnection();
    }

    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
