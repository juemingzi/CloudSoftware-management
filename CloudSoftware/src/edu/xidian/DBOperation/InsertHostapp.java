package edu.xidian.DBOperation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.mysql.jdbc.Connection;

public class InsertHostapp {

	private Connection con = null;
	
	DealException de = new DealException();
	
	public InsertHostapp() {
		con = (Connection) DBConnManager.getConnection();
		if (con == null) {
            System.out.println("Can't get connection");
            return;
        }
	}
	
	// Õ∑≈¡¨Ω”
	public void close() {
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public synchronized void insertHostappTable(String hostip,String software,String version,String installpath) throws SQLException{
		Statement stmt = null,st = null,sm = null;
		stmt = con.createStatement();
		st = con.createStatement();
		sm = con.createStatement();
		boolean flage = true;
		String sql = "UPDATE  hostapp  SET version = '"+version+"' , installpath = '"+installpath+"' WHERE hostip = '"+hostip+"' and software = '"+software+"'";
		String s = "SELECT * FROM hostapp WHERE hostip = '"+hostip+"'";
		ResultSet rs = stmt.executeQuery(s);
		while(rs.next()){
			if(rs.getString("software").toLowerCase().equals(software)){
				st.executeUpdate(sql);
				flage = false;
			}
		}
		if(flage){
			String sq = "INSERT INTO  hostapp(hostip,software,version,installpath) VALUES ('"+hostip+"','"+software+"','"+version+"','"+installpath+"')";
			sm.executeUpdate(sq);
			if(sm!=null)
			{
				try{
					sm.close();
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		if(st!=null)
		{
			try{
				st.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
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
	}
}