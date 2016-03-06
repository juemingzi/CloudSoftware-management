package edu.xidian.DBOperation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

public class GetTotallSpace {

	private Connection con = null;

	DealException de = new DealException();

	public GetTotallSpace() {
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

	public int searchSetupForSpace(String path,String name) throws Exception {// path下面包含:
		System.out.println("path  is  "+path);
		String[] str = new String[3];
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "SELECT * FROM setup WHERE  status='false'";
		System.out.println(sql);
		ResultSet rs = stmt.executeQuery(sql);
		String result = null;
		String version = null;
		String installPath = null;
		int total = 0;
		while (rs.next()) {
			str[0] = rs.getString("ip");
			str[1] = rs.getString("software");
			version = rs.getString("version");
			installPath = rs.getString("installpath");
			if (!installPath.startsWith("/")) {
				if (installPath.toLowerCase().substring(0, 2)
						.contains(path.toLowerCase())&&!str[1].equals(str[1])) {
						total += getspace(str[1], version, "W");
				}

			}else{
				if(!str[1].equals(str[1]))
					total += getspace(str[1], version, "L");
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return total;
	}

	public int searchSetupForZip(String name) throws Exception {
		String[] str = new String[3];
		Statement stmt = null;
		stmt = con.createStatement();
		String sql = "SELECT * FROM setup WHERE  status='false'";
		System.out.println(sql);
		ResultSet rs = stmt.executeQuery(sql);
		String version = null;
		String installPath = null;
		int total = 0;
		while (rs.next()) {
			str[0] = rs.getString("ip");
			str[1] = rs.getString("software");
			version = rs.getString("version");
			installPath = rs.getString("installpath");
			if(installPath.startsWith("/")){
				if(!str[1].equals(name))
					total += getzipspace(str[1],version,"L");
			}else{
				if(!str[1].equals(name))
					total += getzipspace(str[1],version,"W");
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return total;
	}

	public int getspace(String softName, String version, String os)
			throws SQLException {
		int spaceRequirement = 0;
		Statement stmt = null;
		stmt = con.createStatement();
		ResultSet rs = null;
		String sql = "SELECT * FROM rcinfo WHERE softName = '" + softName
				+ "' and softVersion = '" + version + "'" + " and OS = '" + os
				+ "'";
		System.out.println(sql);
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			spaceRequirement = rs.getInt("spaceRequirement");
		//	zipspace = rs.getInt("softSize");

		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return spaceRequirement;
	}

	public int getzipspace(String softName, String version, String os)
			throws SQLException {

		int zipspace = 0;
		Statement stmt = null;
		stmt = con.createStatement();
		ResultSet rs = null;
		String sql = "SELECT * FROM rcinfo WHERE softName = '" + softName
				+ "' and softVersion = '" + version + "'" + " and OS = '" + os
				+ "'";
		System.out.println(sql);
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			zipspace = rs.getInt("softSize");

		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return zipspace;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
