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

public class DBForSCS {

private Connection con = null;
	
	public DBForSCS() {
		con = DBConnManager.getConnection();
		if (con == null) {
            System.out.println("Can't get connection");
            return;
        }
	}
	
	//�ͷ�����
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
	 * ��setup����в�������
	 */
	public boolean InsertToSetup(String IP,String Software,String Version,String installPath,int opID,String command) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		
		String sql = "INSERT INTO setup (ip,software,version,installpath,status,opid,command) VALUES ('"+IP+"','"+Software+"','"+Version+"','"+installPath+"','false','"+opID+"','"+command+"')";
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
	 * ��config����в�������
	 */
	public boolean InsertToConfig(String IP,String Software,String Path,String ParaName,String ParaValue) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "INSERT INTO config (ip,software,path,paraname,paravalue,status) VALUES ('"+IP+"','"+Software+"','"+Path+"','"+ParaName+"','"+ParaValue+"','false')";
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
	 * ��script����в�������
	 */
	public boolean InsertToScript(int opid,String IP,String UpLoadPath) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "INSERT INTO script (opid,ip,uploadpath,status) VALUES ('"+opid+"','"+IP+"','"+UpLoadPath+"','false')";
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
	 * ����setup��
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
	 * ����config��
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
	 * ����script��
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
	
	/*
	 * ��ȡ��װ����״̬
	 */
	public boolean getSetupStatus(String IP,String Software) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "SELECT * FROM setup WHERE ( ip='"+IP+"' and software='"+Software+"')";
		System.out.println(sql);
		ResultSet rs = stmt.executeQuery(sql);
		String status = null;
		while(rs.next()){
			status = rs.getString("status");
			if(status.equals("false")){
				return false;
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
		return true;
	}
	
	/*
	 * ��ȡ��������״̬
	 */
	public boolean getConfigStatus(String IP,String Software,String paraname) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "SELECT * FROM config WHERE ( ip='"+IP+"' and software='"+Software+"' and paraname='"+paraname+"')";
		System.out.println(sql);
		ResultSet rs = stmt.executeQuery(sql);
		String status = null;
		while(rs.next()){
			status = rs.getString("status");
			if(status.equals("false")){
				return false;
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
		return true;
	}
	
	/*
	 * ��ȡ�ű�ִ������״̬
	 */
	public boolean getScriptStatus(String IP,String uploadpath) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "SELECT * FROM script WHERE ( ip='"+IP+"' and uploadpath='"+uploadpath+"')";
		System.out.println(sql);
		ResultSet rs = stmt.executeQuery(sql);
		String status = null;
		while(rs.next()){
			status = rs.getString("status");
			if(status.equals("false")){
				return false;
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
		return true;
	}
	/*
	 * ɾ������statusΪtrue������
	 */
	public void Delete(String tablename) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "DELETE FROM "+tablename+" WHERE status = 'true'" ;
		stmt.executeUpdate(sql);
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
