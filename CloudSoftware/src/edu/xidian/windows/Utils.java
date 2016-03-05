package edu.xidian.windows;
import java.io.File;


public class Utils {

	
	/**@author ZXQ
	 * 在指定目录下查找是否存在key相关的文件夹（用于安装时检测）
	 * @param dirPath
	 * @param key
	 * @return
	 */
	public static boolean isExists(String dirPath,String key){
		boolean flag = false;
		File f = new File(dirPath);
		File[] files = f.listFiles();
		if(files==null) {
			return false;
		}
		for(File file:files) {
			if(file.isDirectory()) {
				if(file.getName().toLowerCase().contains(key)) {
					flag = true;
					System.out.println(file.getAbsolutePath());
					break;
				}else {
					//继续查找
					flag = isExists(file.getAbsolutePath(),key);
				}
			}
		}
		return flag;
		
	}
	/**@author ZXQ
	 * 查找包含关键字的目录，并删除该目录
	 * @param dirPath
	 * @param key
	 */
	public static void findDirs(String dirPath,String key) {
		File f = new File(dirPath);
		File[] files = f.listFiles();
		if(files==null) {
			return;
		}
		for(File file:files) {
			if(file.isDirectory()) {
				if(file.getName().toLowerCase().contains(key.toLowerCase())) {
					//删除该目录及该目录下的所有文件
					System.out.println("******找到*****"+file.getPath()+"*****");
					delFolder(file.getAbsolutePath());
					//break;
				}else {
					//继续查找
					findDirs(file.getAbsolutePath(),key);
				}
			}
		}
		
	}
	
	/**@author ZXQ
	 * 删除目录
	 * @param folderPath
	 */
	public static void  delFolder(String folderPath) {
	     try {
	    	 System.out.println("***开始删除***");
	        delAllFile(folderPath); //删除完里面所有内容
	        String filePath = folderPath;
	        filePath = filePath.toString();
	        java.io.File myFilePath = new java.io.File(filePath);
	        myFilePath.delete(); //删除空文件夹
	        System.out.println("***删除完毕***");
	     } catch (Exception e) {
	       e.printStackTrace(); 
	     }
	}

	/**@author ZXQ
	 * 删除指定目录下的所有文件
	 * @param path
	 * @return
	 */
	public static  boolean delAllFile(String path) {
	       boolean flag = false;
	       File file = new File(path);
	       if (!file.exists()) {
	         return flag;
	       }
	       if (!file.isDirectory()) {
	         return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	          if (temp.isDirectory()) {
	             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
	             delFolder(path + "/" + tempList[i]);//再删除空文件夹
	             flag = true;
	          }
	       }
	       return flag;
	     }
	
	/**@author ZXQ
	 * 获得所有盘符，并进行关键字查找与目录删除
	 * @param key
	 * @return
	 */
	public static boolean deleteRoots(String key) {
		 File[] roots=File.listRoots();
		 if(roots==null) return true;
		 for(File file:roots) {
			System.out.println(file.getAbsolutePath());
			findDirs(file.getAbsolutePath(),key);
		 }
		 return true;
	}
	
	 
	public static void main(String[] args) {
		File[] fileList = new File("C:\\").listFiles();
		for(File f:fileList) {
			if(f.isDirectory()&&f.getName().contains("Apache1")) {
				System.out.println(f.getName());
			}
		}
		//Utils.deleteRoots("tomcat");
		//System.out.println(Utils.isExists("D:/", "tomcat"));
		//isExists("C:\\httpd\\","httpd");
	//	Utils.delFolder("C:\\software\\360Safe");
		System.out.println("end");

	}

}
