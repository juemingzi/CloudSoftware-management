package sample.hello.bean;

import java.sql.SQLException;

import sample.DBOP.DBOperation;

public class GetCodeBase {

	public String getCodeInfo(String codeID) {
		String ret = "";
		DBOperation dbop = new DBOperation();
		try {
			ret = dbop.getCode(codeID);
			dbop.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
