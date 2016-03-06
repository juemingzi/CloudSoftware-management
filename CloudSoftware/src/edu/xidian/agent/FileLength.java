package edu.xidian.agent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileLength {
	public static long getFileLength(String f) throws IOException {
		/**
		 * 获取文件大小
		 */
		File file = new File(f );
		if(!file.exists())
			return 0;
//		/**
//		 * length以KB为单位
//		 */
//		int length = (int) file.length() / 1024;
//		System.out.println(length);
		long size = 0;
        File flist[] = file.listFiles();
        for (int i = 0; i < flist.length; i++)
        {
            if (flist[i].isDirectory())
            {
                try {
					size = size + getFileSizes(flist[i]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else
            {
                size =  (size + flist[i].length());
            }
        }
        
       return size;
	}
	
	 public static long getFileSizes(File f) throws Exception{//取得文件大小
		 
	        long s=0;
	        if (f.exists()) {
	            FileInputStream fis = null;
	            fis = new FileInputStream(f);
	           s= fis.available();
	        } else {
	            f.createNewFile();
	            //System.out.println("文件不存在");
	        }
	        return s;
	    }
	 
	 public static long getFileSize(File f) throws Exception// 取得文件夹大小
		{
			long size = 0;
			if(!f.exists())
				return 0;
			File flist[] = f.listFiles();
			for (int i = 0; i < flist.length; i++)
			{
				if (flist[i].isDirectory())
				{
					size = size + getFileSize(flist[i]);
				} else
				{
					size = size + flist[i].length();
				}
			}
			return size;
		}
	public static void main(String[] args) throws IOException {
		long nun = new FileLength().getFileLength("E:\\a");
		System.out.println(nun);
	}
}
