package sample.DBOP;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mchange.v2.c3p0.ComboPooledDataSource;


/**
 * @brief 数据库连接类
 * @author huangpeng
 * @version 
 * @date 2015-3-30 下午6:06:15 
 *
 */
public class DBConnManager {
	private static ComboPooledDataSource cpds=new ComboPooledDataSource(true); 
	
	static{
		cpds.setDataSourceName("mydatasource");
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/cloudhost");
		try {
			cpds.setDriverClass("com.mysql.jdbc.Driver");
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		cpds.setUser("root");
		cpds.setPassword("123456");
		cpds.setMaxPoolSize(20);
		cpds.setMinPoolSize(3);
		cpds.setAcquireIncrement(1);
		cpds.setInitialPoolSize(3);
		cpds.setMaxIdleTime(25200);
		//定期使用连接池内的连接
//		cpds.setPreferredTestQuery("SELECT 1");
//		cpds.setIdleConnectionTestPeriod(2000);
//		cpds.setTestConnectionOnCheckin(true);
//		cpds.setTestConnectionOnCheckout(true);
		
	}
	
	private DBConnManager(){}
	
	public static Connection  getConnection(){
		try {
			return cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
