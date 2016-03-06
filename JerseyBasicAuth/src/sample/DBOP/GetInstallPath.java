package sample.DBOP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
public class GetInstallPath {

private Connection con = null;
	
	public GetInstallPath() {
		con = DBConnManager.getConnection();
		if (con == null) {
            System.out.println("Can't get connection");
            return;
        }
	}
	
	//释放连接
	public void close() {
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * 获取已安装软件的地址
	 */
	public String getpath(String IP,String Software) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "SELECT * FROM hostapp WHERE ( hostip='"+IP+"' and software='"+Software+"')";
		System.out.println(sql);
		ResultSet rs = stmt.executeQuery(sql);
		String path = null;
		while(rs.next()){
			path = rs.getString("installpath");
			System.out.println("~~~~~~~~~~~~sql  path is "+path);
		}
		if(stmt!=null)
		{
			try{
				stmt.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return path;
	}
	
	/*
	 * 获取已安装软件的版本号
	 */
	public String getVersion(String IP,String Software) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "SELECT * FROM hostapp WHERE ( hostip='"+IP+"' and software='"+Software+"')";
		System.out.println(sql);
		ResultSet rs = stmt.executeQuery(sql);
		String version = null;
		while(rs.next()){
			version = rs.getString("version");
		}
		if(stmt!=null)
		{
			try{
				stmt.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return version;
	}
	
	/*
	 * 为了在hostapp表中插入installPath，首先从setup表中查找获取
	 */
	public String getpathForHostapp(String IP,String Software) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "SELECT installpath FROM setup WHERE ( ip='"+IP+"' and software='"+Software.toLowerCase()+"' and status='false')";
		System.out.println(sql);
		ResultSet rs = stmt.executeQuery(sql);
		String path = null;
		while(rs.next()){
			path = rs.getString("installpath");
			System.out.println("111111111111111111111111111111111111111111111111111"+path);
		}
		if(stmt!=null)
		{
			try{
				stmt.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return path;
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
