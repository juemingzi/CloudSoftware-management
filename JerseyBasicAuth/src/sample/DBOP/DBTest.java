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

public class DBTest {

private Connection con = null;
	
	public DBTest() {
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
	 * 向setup表格中插入数据
	 */
	public boolean InsertToSetup(String IP,String Software,String Version,String installPath) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "INSERT INTO setup (ip,software,version,installpath,status) VALUES ('"+IP+"','"+Software+"','"+Version+"','"+installPath+"','false')";
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
	 * 向config表格中插入数据
	 */
	public boolean InsertToConfig(String IP,String Software,String Path,String ParaName,String ParaValue) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "INSERT INTO setup (ip,software,path,paraname,paravalue,status) VALUES ('"+IP+"','"+Software+"','"+Path+"','"+ParaName+"','"+ParaValue+"','false')";
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
	 * 向script表格中插入数据
	 */
	public boolean InsertToScript(String IP,String UpLoadPath,String sName) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "INSERT INTO setup (ip,uploadpath,sname,status) VALUES ('"+IP+"','"+UpLoadPath+"','"+sName+"','false')";
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
	public boolean updateSetup(String IP,String Software) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();

		String sql = "UPDATE  setup  SET status ='true' WHERE ( ip='"+IP+"' and software='"+Software+"')";
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

		String sql = "UPDATE  setup  SET status ='true' WHERE ( ip='"+IP+"' and software='"+Software+"' and paraname='"+ParaName+"' and paravalue='"+ParaValue+"')";
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
	public boolean updateScript(String IP,String UpLoadPath,String sName) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();

		String sql = "UPDATE  setup  SET status ='true' WHERE ( ip='"+IP+"' and software='"+UpLoadPath+"' and sname="+sName+"')";
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
