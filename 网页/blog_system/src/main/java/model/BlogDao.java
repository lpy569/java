package model;


import com.mysql.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// 通过整个类 来完成对表的操作
public class BlogDao {
    // 1. 新增操作 (提交博客就会用到)
    public void insert(Blog blog) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // 1. 建立连接
            connection = DbUtil.getConnection();
            // 2. 构造 SQL
            String sql = "insert into blog values (null, ?, ?, now(), ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, blog.getTitle());
            statement.setString(2, blog.getContent());
            statement.setInt(3, blog.getUserId());
            // 3. 执行 SQL
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException();
        } finally {
            DbUtil.close(connection, statement, null);
        }
    }

    // 2. 查询博客列表 (博客列表页)
    //    把数据库里所有的博客都拿到.
    public List<Blog> getBlogs() {
        List<Blog> blogList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbUtil.getConnection();
            String sql = "select * from blog order by postTime desc";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Blog blog = new Blog();
                blog.setBlogId(resultSet.getInt("blogId"));
                blog.setTitle(resultSet.getString("title"));
                // 此处读到的正文是整个文章内容. 太多了. 博客列表页, 只希望显示一小部分. (摘要)
                // 此处需要对 content 做一个简单截断. 这个截断长度 100 这是拍脑门出来的. 具体截取多少个字好看, 大家都可以灵活调整.
                String content = resultSet.getString("content");
                if (content.length() > 100) {
                    content = content.substring(0, 100) + "...";
                }
                blog.setContent(content);
                blog.setPostTime(resultSet.getTimestamp("postTime"));
                blog.setUserId(resultSet.getInt("userId"));
                blogList.add(blog);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtil.close(connection, statement, resultSet);
        }
        return blogList;
    }

    // 3. 根据博客 id 查询指定的博客
    public Blog getBlog(int blogId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbUtil.getConnection();
            String sql = "select * from blog where blogId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, blogId);
            resultSet = statement.executeQuery();
            // 由于此处是拿着 blogId 进行查询. blogId 作为主键, 是唯一的.
            // 查询结果非 0 即 1 , 不需要使用 while 来进行遍历
            if (resultSet.next()) {
                Blog blog = new Blog();
                blog.setBlogId(resultSet.getInt("blogId"));
                blog.setTitle(resultSet.getString("title"));
                // 这个方法是期望在获取博客详情页的时候, 调用. 不需要进行截断, 应该要展示完整的数据内容
                blog.setContent(resultSet.getString("content"));
                blog.setPostTime(resultSet.getTimestamp("postTime"));
                blog.setUserId(resultSet.getInt("userId"));
                return blog;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DbUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 4. 根据博客 id, 删除博客
    public void delete(int blogId) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DbUtil.getConnection();
            String sql = "delete from blog where blogId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, blogId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DbUtil.close(connection, statement, null);
        }
    }

}
