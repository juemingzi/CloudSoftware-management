package edu.xidian.windows;
import java.io.File;


public class Utils {

	
	/**@author ZXQ
	 * ��ָ��Ŀ¼�²����Ƿ����key��ص��ļ��У����ڰ�װʱ��⣩
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
					//��������
					flag = isExists(file.getAbsolutePath(),key);
				}
			}
		}
		return flag;
		
	}
	/**@author ZXQ
	 * ���Ұ����ؼ��ֵ�Ŀ¼����ɾ����Ŀ¼
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
					//ɾ����Ŀ¼����Ŀ¼�µ������ļ�
					System.out.println("******�ҵ�*****"+file.getPath()+"*****");
					delFolder(file.getAbsolutePath());
					//break;
				}else {
					//��������
					findDirs(file.getAbsolutePath(),key);
				}
			}
		}
		
	}
	
	/**@author ZXQ
	 * ɾ��Ŀ¼
	 * @param folderPath
	 */
	public static void  delFolder(String folderPath) {
	     try {
	    	 System.out.println("***��ʼɾ��***");
	        delAllFile(folderPath); //ɾ����������������
	        String filePath = folderPath;
	        filePath = filePath.toString();
	        java.io.File myFilePath = new java.io.File(filePath);
	        myFilePath.delete(); //ɾ�����ļ���
	        System.out.println("***ɾ�����***");
	     } catch (Exception e) {
	       e.printStackTrace(); 
	     }
	}

	/**@author ZXQ
	 * ɾ��ָ��Ŀ¼�µ������ļ�
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
	             delAllFile(path + "/" + tempList[i]);//��ɾ���ļ���������ļ�
	             delFolder(path + "/" + tempList[i]);//��ɾ�����ļ���
	             flag = true;
	          }
	       }
	       return flag;
	     }
	
	/**@author ZXQ
	 * ��������̷��������йؼ��ֲ�����Ŀ¼ɾ��
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
