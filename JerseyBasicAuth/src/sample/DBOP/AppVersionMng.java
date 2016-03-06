package sample.DBOP;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @brief 
 * @author huangpeng
 * @version 
 * @date 2015-3-17 上午9:38:54 
 * 
 */
public class AppVersionMng {
	/**
	 * 
	 * @author huangpeng
	 * @date 2015-3-17
	 * @brief 从一个版本号列表(长度至少为2)里面选择一个最新的版本号
	 */
	public String pickNewestVerionNumber(ArrayList<String> versionList){
		String newestVersion = "";
		String firstVersion = versionList.get(0);
		String regEx1 = "[0-9]+\\.[0-9]+\\.[0-9]+";
		String regEx2 = "[0-9]+\\.[0-9]+";
		if(Pattern.matches(regEx1, firstVersion)){
			newestVersion=versionList.get(0);
			for(int i=1;i<versionList.size();i++){
				newestVersion=whichIsNew(newestVersion,versionList.get(i));
			}
		}else if(Pattern.matches(regEx2, firstVersion)){
			newestVersion=versionList.get(0);
			for(int i=1;i<versionList.size();i++){
				newestVersion=whichIsNew(newestVersion,versionList.get(i));
			}
		}else{
			System.out.println("ERROR : Version format error");//版本号格式错误，无法处理。
		}
		return newestVersion;
	}
	
	
	
	public String whichIsNew(String version1,String version2){
		if(version1.compareTo(version2)>0){
			return version1;
		}else if(version1.compareTo(version2)<0){
			return version2;
		}
		return null;
	}
	
	
	
	public static void main(String[] args){
		AppVersionMng avm = new AppVersionMng();
//		avm.StringFilter("2008.0.2");
		ArrayList<String> versionList = new ArrayList<String>();
		versionList.add("2.4.2");
		versionList.add("2.4.15");
		String newest = avm.pickNewestVerionNumber(versionList);
		System.out.println(newest);
	}
}
