package model;


import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;

// 通过整个类 来完成对表的操作
public class UseDao {
    // 1. 根据 useId来查询用户信息
    public User getUseById(int useId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbUtil.getConnection();

            String sql = "select * from user where useId = ?";
            statement = (PreparedStatement) connection.prepareStatement(sql);
            statement.setInt(1, useId);

            resultSet = statement.executeQuery();
            if(resultSet.next()){
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setPassword(resultSet.getString("password"));
                user.setUsername(resultSet.getString("username"));
                return user;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DbUtil.close(connection, statement, resultSet);
        }

        return null;
    }

    // 2. 根据 username 来查询对应用户信息
    public User getUseByName(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbUtil.getConnection();
            String sql = "select * from user where username = ?";
            statement = (PreparedStatement) connection.prepareStatement(sql);
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DbUtil.close(connection, statement, resultSet);
        }
        return null;
    }

}
