package edu.xidian.DBOperation;

import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

public class updateSCS {

	private Connection con = null;
	
	DealException de = new DealException();
	
	public updateSCS() {
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

	/*
	 * 更新setup表
	 */
	public boolean updateSetup(String IP,String Software) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();

		String sql = "UPDATE  setup  SET status ='true' WHERE ( ip='"+IP+"' and software='"+Software+"')";
		System.out.println(sql);
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
	
	/*
	 * 更新setup表
	 */
	public boolean updateConfig(String IP,String Software,String ParaName,String ParaValue) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();

		String sql = "UPDATE  config  SET status ='true' WHERE ( ip='"+IP+"' and software='"+Software+"' and paraname='"+ParaName+"' and paravalue='"+ParaValue+"')";
		System.out.println(sql);
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
	
	/*
	 * 更新script表
	 */
	public boolean updateScript(int opid) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "UPDATE  script  SET status ='true' WHERE ( opid='"+opid+"')";
		System.out.println(sql);
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
