package edu.xidian.DBOperation;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.mysql.jdbc.Connection;

public class InsertOpstatus {

	private Connection con = null;
	
	DealException de = new DealException();
	
	public InsertOpstatus() {
		con = (Connection) DBConnManager.getConnection();
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
	public synchronized boolean insertOp(int opID,String status) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		// 插入新纪录到opinfo表
		// 计算时间
		java.util.Date date = new java.util.Date();
		Timestamp time = new Timestamp(date.getTime());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "INSERT INTO opinfo (opID,status,time) VALUES (" + opID+ ",'" + status + "','" + df.format(time) + "')";
		boolean flag = stmt.execute(sql);
		if(stmt!=null)
		{
			try{
				stmt.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	
	
	
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
