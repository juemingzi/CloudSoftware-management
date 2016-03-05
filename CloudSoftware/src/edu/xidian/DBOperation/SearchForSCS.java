package edu.xidian.DBOperation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

import com.mysql.jdbc.Connection;

import edu.xidian.DBOperation.*;

public class SearchForSCS {

	private Connection con = null;

	DealException de = new DealException();
	

	public SearchForSCS() {
		con = (Connection) DBConnManager.getConnection();
		if (con == null) {
			System.out.println("Can't get connection");
			return;
		}
	}

	// 释放连接
	public void close() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void searchSetup(int os) throws Exception {
		String[] str = new String[4];
		Statement stmt = null;
		int opid = 0;
		updateSCS scs = new updateSCS();
		InsertOpstatus insertop = new InsertOpstatus();
		InsertHostapp inserthost = new InsertHostapp();

		stmt = con.createStatement();
		String sql = "SELECT * FROM setup WHERE  status='false'";
		System.out.println(sql);
		ResultSet rs = stmt.executeQuery(sql);
		String result = null;
		String installPath = null;
		int flag = -1;
		while (rs.next()) {
			str[0] = rs.getString("ip");
			str[1] = rs.getString("software");
			str[2] = rs.getString("command");
			str[3] = rs.getString("version");
			opid = rs.getInt("opid");
			installPath = rs.getString("installpath");
			flag = getOs(str[0]);
			if (os == 0&&flag ==0) {
				result = de.Setup(str);
				// 此处添加在opinfo中插入成功编码。
				insertop.insertOp(opid, result);
				String regEx1 = "0x0100.00";
				String regEx3 = "0x0400.00";
				if (Pattern.matches(regEx1, result) || Pattern.matches(regEx3, result)) 
					inserthost.insertHostappTable(str[0], str[1], str[3], installPath);
				scs.updateSetup(str[0], str[1]);
			} else if (os == 1&&flag == 1) {
				result = de.Setup_Linux(str);
				// 此处添加在opinfo中插入成功编码。
				insertop.insertOp(opid, result);
				String regEx1 = "0x0100.00";
				String regEx3 = "0x0400.00";
				if (Pattern.matches(regEx1, result) || Pattern.matches(regEx3, result)) 
					inserthost.insertHostappTable(str[0], str[1], str[3], installPath);
				scs.updateSetup(str[0], str[1]);
			}
			
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		scs.close();
		insertop.close();
		inserthost.close();
	}

	public void searchConfig(int os) throws Exception {
		String[] str = new String[5];
		Statement stmt = null;
		updateSCS scs = new updateSCS();

		stmt = con.createStatement();
		String sql = "SELECT * FROM config WHERE  status='false'";
		System.out.println(sql);
		ResultSet rs = stmt.executeQuery(sql);
		int flag = -1;
		while (rs.next()) {
			str[0] = rs.getString("ip");
			str[1] = rs.getString("software");
			str[2] = rs.getString("path");
			str[3] = rs.getString("paraname");
			str[4] = rs.getString("paravalue");
			flag = getOs(str[0]);
			if (os == 0&&flag ==0) {
				de.config(str);
				scs.updateConfig(str[0], str[1], str[3], str[4]);
			} else if (os == 1&&flag == 1) {
				de.config_Linux(str);
				scs.updateConfig(str[0], str[1], str[3], str[4]);
			}
			
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		scs.close();
	}

	public void searchScript(int os) throws Exception {
		String[] str = new String[2];
		updateSCS scs = new updateSCS();
		int opid = 0;
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "SELECT * FROM script WHERE  status='false'";
		System.out.println(sql);
		ResultSet rs = stmt.executeQuery(sql);
		int flag = -1;
		
		while (rs.next()) {
			opid = rs.getInt("opid");
			str[0] = rs.getString("ip");
			str[1] = rs.getString("uploadpath");
			flag = getOs(str[0]);
			if (os == 0&&flag ==0) {
				de.script(str);
				scs.updateScript(opid);
			} else if (os == 1&&flag == 1) {
				de.script_Linux(str);
				scs.updateScript(opid);
			}
			
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		scs.close();
	}
	private int getOs(String ip) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "SELECT * FROM hostinfo WHERE  hostIP = '"+ip+"'";
		System.out.println(sql);
		ResultSet r = stmt.executeQuery(sql);
		String os = null;
		while (r.next()) {
			os = r.getString("OS");
			if(os.toLowerCase().equals("w")){
//				if (con != null) {
//					try {
//						c.close();
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				}
					
				return 0;
			}else{
//				if (con != null) {
//					try {
//						c.close();
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				}
				return 1;
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		if (con != null) {
//			try {
//				con.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
		return 2;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
