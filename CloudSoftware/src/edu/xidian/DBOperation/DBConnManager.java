//package edu.xidian.DBOperation;
//
//import java.sql.*;
//
//public class DBConnManager {
//
//	private String dbDriver = "com.mysql.jdbc.Driver"; // 数据库的驱动
//
//	private String url = "jdbc:mysql://119.90.140.60:3306/cloudhost?user=root&password=123456&characterEncoding=GBK"; // URL地址
//
//	public Connection connection = null;
//
//	public DBConnManager() {
//		try {
//			Class.forName(dbDriver).newInstance(); // 加载数据库驱动
//			connection = DriverManager.getConnection(url); // 加载数据库
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			System.out.println("数据库加载失败");
//		}
//	}
//	
//	public static void main(String[] args){
//		new DBConnManager();
//	}
//}
package edu.xidian.DBOperation;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.*;
//import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @brief 数据库连接类
 * @author huangpeng
 * @version
 * @date 2015-3-30 下午6:06:15
 * 
 */
public class DBConnManager {
	// private static ComboPooledDataSource cpds=new
	// ComboPooledDataSource(true);
	//
	// static{
	// cpds.setDataSourceName("mydatasource");
	// cpds.setJdbcUrl("jdbc:mysql://119.90.140.60:3306/cloudhost");
	// try {
	// cpds.setDriverClass("com.mysql.jdbc.Driver");
	// } catch (PropertyVetoException e) {
	// e.printStackTrace();
	// }
	// cpds.setUser("root");
	// cpds.setPassword("123456");
	// cpds.setMaxPoolSize(20);
	// cpds.setMinPoolSize(3);
	// cpds.setAcquireIncrement(1);
	// cpds.setInitialPoolSize(3);
	// cpds.setMaxIdleTime(25200);
	//
	// }
	//

	//
	// public static Connection getConnection(){
	// try {
	// return cpds.getConnection();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
	//

	private static String url = "jdbc:mysql://119.90.140.60:3306/cloudhost";
	private static String username = "root";
	private static String password = "123456";

	public static Connection getConnection() {
		try {
			// 加载MySql的驱动类
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, username,
					password);
//			String u ="jdbc:mysql://119.90.140.60:3306/cloudhost?user=root&password=123456";
//			Connection con = DriverManager.getConnection(u);
			return con;
		} catch (ClassNotFoundException e) {
			System.out.println("找不到驱动程序类 ，加载驱动失败！");
			e.printStackTrace();
		}catch (SQLException se) {
			System.out.println("数据库连接失败！");
			se.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws SQLException {
		DBConnManager dbc = new DBConnManager();
		Connection con = dbc.getConnection();
		if (con == null) {
			System.out.println("Can't get connection");
			return;
		}
		//System.out.println(con);
		
		//ocean 2015-8/8
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "SELECT installpath FROM setup WHERE ( ip='"+"119.90.140.59"+"' and software='"+"oracle11g"+"' and status='true')";
		
		ResultSet rs = stmt.executeQuery(sql);
		String path = null;
		while(rs.next()){
			path = rs.getString("installpath");
			System.out.println("输出一次路径");
			System.out.println(path);
		}	
//		stmt = con.createStatement();
//		String sql = "INSERT INTO  hostapp(hostip,software,version,installpath) VALUES ('"+"119.90.140.221"+"','"+"Oracle11g"+"','"+"11g"+"','"+"C:/a/a/a"+"')";
//		stmt.executeUpdate(sql);
//		if(stmt!=null)
//		{
//			try{
//				stmt.close();
//			}catch(Exception e)
//			{
//				e.printStackTrace();
//			}
//		}
	}
}