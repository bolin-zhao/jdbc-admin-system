package cn.bolin.bank.dao;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Create By Bolin on 12.9
 * 一个通用的JDBC访问的工具类!
 * 封装了数据表的 增删改 方法
 */
public class BaseDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    static String drive;
    static String url;
    static String name;
    static String pwd;

    // 读取配置文件
    static{
        // 加载时只执行一次,读取配置文件jdbc.properties
        Properties pros = new Properties();
        try {
            // pros.load(new FileInputStream(new File("config/jdbc.properties")));
            pros.load(BaseDAO.class.getClassLoader().getResourceAsStream("jdbc.properties"));
            drive = (String) pros.get("driveClass");
            url = (String) pros.get("url");
            name = (String) pros.get("username");
            pwd = (String) pros.get("password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * 1. 打开数据库
    */
    public void openConnection(){
        try {
            Class.forName(drive);
           conn = DriverManager.getConnection(url, name, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    * 2. 关闭数据库    *
    */

    public void closeConnection(){
        // 如果不为null, 就关闭,如果为null,就跳过
        try{
            if (rs != null){
                rs.close();
            }
            if (ps != null){
                ps.close();
            }if (conn != null){
                conn.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    // 通用的增删改操作
    public int doexecuteUpdate(String sql, Object[] args) {
        this.openConnection(); // 打开数据库
        PreparedStatement ps = null;
        try {
            // 1. 预编译sql语句, 返回PreparedStatement实例
            ps = conn.prepareStatement(sql);
            // 2. 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]); // 小心声明参数错误!!
            }
            // 3. 执行增删改
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            this.closeConnection();// 关闭数据库
        }
        return 0;
    }

    /*
    * 4. 通用执行查询数据库的方法
    */
    public ResultSet doexecuteQuery(String sql, Object[] args){
        this.openConnection();// 打开数据库
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            // 查询的时候工具类不关闭,当ResultSet 使用后手动关闭!
        }
        return null;
    }
}
