package edu.xidian.agent;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * 在这个版本中希望做的是软件的多线程下载
 * 现在也做了断点下载 ，是否断点下载的依据为目标文件是否已经存在 ，不管是否从断点出继续下载，分的线程下载的文件都是从指定的位置开始下载的
 * 
 */
public class FtpTransFile {

	private static String fileName; // 要上传或下载的文件的名字
	private static String path;// 临时文件夹的目录,用于存放多个线程下载的文件
	static long threadBlock = 100 * 1024 * 1024L; //表示线程块大小
	//static long threadBlock = 5100 * 1024 * 1024L;
	/**
	 * @author zhangke
	 * 
	 * @param path
	 *            要上传的本地文件路径 如"C:/Users/repace/Desktop/zhangke1.txt";
	 * @param server
	 *            ftp服务器ip地址 192.168.242.133
	 * @param userName
	 *            登录ftp的用户名 test
	 * @param password
	 *            登录ftp用户名对应的密码 123456
	 */
	public static void fileUpload(String OStype, String path, String server,
			String userName, String password) { // 要上传的文件的本地路径路径
		// 目前可完成单个文件的上传

		if (!(OStype.equalsIgnoreCase("windows") || OStype
				.equalsIgnoreCase("linux"))) {
			System.out.println("操作系统类型输入错误，应为windows或linux");
			return;
		}

		FTPClient ftpClient = new FTPClient();
		ftpClient.enterLocalPassiveMode(); // 这一句话一定要记得加上
		FileInputStream fis = null;
		try {
			ftpClient.connect(server);
			ftpClient.login(userName, password);

			File srcFile = new File(path);// 要上传的本地文件路径
			fis = new FileInputStream(srcFile);
			String storeName = srcFile.getName();// 要存储的文件的名字
			String remoteFilename = "/mnt/data/ftp/www/" + OStype.toLowerCase() + "/"
					+ storeName;
			ftpClient.changeWorkingDirectory("/mnt/data/ftp/www/" + OStype.toLowerCase()
					+ "/"); // 设置上传的文件在centos上的目录，文件上传不成功是要查看指定目录的权限
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);// 设置文件类型（二进制）
			FTPFile[] files = ftpClient.listFiles(remoteFilename);// 判断软件中心是否包含这个文件
			if (files.length == 1) {// 软件中心包含该文件
				long remoteSize = files[0].getSize();// 软件中心的文件大小
				long localSize = srcFile.length();// 打算要上传的文件大小
				if (remoteSize == localSize) { // 软件中心有这个文件，并且和打算要上传的文件大小一样，则说要上传的文件已存在
					System.out.println("要上传的文件已存在");
					ftpClient.disconnect();
					return;
				} else if (remoteSize > localSize) {// 软件中心的文件比要上传的大，可能新上传的文件被修改了，然后再次上传的
					System.out.println("软件中心的软件比即将上传的要大，无须上传或重新命名要上传的文件名");
					ftpClient.disconnect();
					return;
				}
				// 软件中心的存的文件比要上传的文件小，则尝试移动文件内读取指针,实现断点续传 **************
				if (fis.skip(remoteSize) == remoteSize) {
					ftpClient.setRestartOffset(remoteSize);
					boolean i = ftpClient.storeFile(
							new String(storeName.getBytes("UTF-8"),
									"iso-8859-1"), fis);
					if (i) {
						System.out.println("文件断点续传成功");
						ftpClient.disconnect();
						return;
					}
				}
			} else { // 软件中心不包含要上传的文件，或者续传不成功，则上传全新的文件即可
				boolean i = ftpClient.storeFile(
						new String(storeName.getBytes("UTF-8"), "iso-8859-1"),
						fis);
				System.out.println("文件上传" + i);
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP客户端出错！", e);
		} finally {
			try {
				fis.close();
				ftpClient.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}

	/**
	 * *
	 * 
	 * @param OStype
	 *            操作系统的类型 windows或者是linux
	 * @param fileName
	 *            指出要下载的文件名字 加后缀的
	 * @param storePath
	 *            下载之后想要在本地的存储路径，在window系统中支持两种文件路径\\ 或者/
	 * @param server
	 *            ftp服务器IP地址
	 * @param userName
	 *            ftp分配的登录名 test
	 * @param password
	 *            与登录名对应的登录密码 123456
	 * @throws FileNotFoundException
	 * @throws InterruptedException
	 */
	public static void fileDownload(String OStype, String fileNames,
			String storePath, String server, String userName, String password)
			throws FileNotFoundException, InterruptedException { // 参数是带后缀的文件名字和下载之后要存储的本地路径
		// 可完成单个文件的下载 ,

		if (!(OStype.equalsIgnoreCase("windows") || OStype
				.equalsIgnoreCase("linux"))) {
			System.out.println("操作系统类型输入错误，应为windows或linux");
			return;
		}
		fileName = fileNames;

		File file = new File(storePath);
		if (!file.exists()) {// 判断文件夹是否存在,如果不存在则创建文件夹
			file.mkdir();
		}

		FTPClient ftpClient = new FTPClient();
		ftpClient.enterLocalPassiveMode(); // 这一句话一定要记得加上
		String remoteFileName = "/mnt/data/ftp/www/" + OStype.toLowerCase() + "/"
				+ fileName; // 服务器上的文件，前面是文件夹的名字，后面的是文件的名字
		String localFileName = "";// 本地要存储的文件绝对路径 文件夹加上文件名
		
		try {
			ftpClient.connect(server);
			ftpClient.login(userName, password);
			ftpClient.setBufferSize(1024);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); // 设置文件类型（二进制）
			FTPFile[] files = ftpClient.listFiles(remoteFileName);
			if (files.length == 0) { // 判断软件中心是否有要下载的软件
				System.out.println("软件中心没有找到要下载的软件");
				ftpClient.disconnect();
				return;
			} else { // 软件中心包含请求下载的文件

				long localSize = 0L; // 记录本地文件的大小
				if (storePath.endsWith("\\") || storePath.endsWith("/"))// 存储路径直接是某个盘下的根目录或者用户加上了最后的斜线
				{
					localFileName = storePath + fileName;
					path = storePath
							+ fileName.substring(0, fileName.indexOf("."))
							+ "Temp/";
				} else {
					localFileName = storePath + "/" + fileName;
					path = storePath + "/"
							+ fileName.substring(0, fileName.indexOf("."))
							+ "Temp/";
				}
				
				File localFile = new File(localFileName);
				long remoteSize = files[0].getSize();// 软件中心的文件大小
				if (localFile.exists()) {// 指定下载的文件在本地文件夹内已经存在
					localSize = localFile.length();// 已存在的文件大小
					if (remoteSize == localSize) {
						System.out.println("文件已下载过，无需再下载");
						return;
					} else if (remoteSize > localSize) { // 之前下载未完成，实现断点下载
						System.out.println("断点下载。。。");
					}
					if (remoteSize < localSize) {// 如果本地的文件比软件中心的文件大，则说明本地的文件可能有错，删除，然后从头开始下载
						localFile.delete();
						System.out.println("软件从新开始下载");
						localSize = 0L;
					}
				} else {// 指定下载的文件在本地文件夹内不存在,从头下载文件
					localSize = 0L;
					System.out.println("软件从头下载");
				}	
				
				File tempfile = new File(path);
				if (tempfile.exists()) {// 判断文件夹是否存在,如果已经存在，则删除该文件夹及其所有的子文件，以免其包含的线程影响后面的下载过程
					System.out.println("delete 之前的临时文件夹");
					deleteTempFile(path);
				}
				tempfile.mkdir();// 新建存放临时文件夹的目录
				
				ExecutorService exec = Executors.newCachedThreadPool(); // 开始启动多线程下载文件
				int threadNum = (int) ((remoteSize - localSize) / threadBlock + 1);// 每50M分一个线程下载
																					// 计算线程总数
				System.out.println("分成的线程个数" + threadNum);
				CountDownLatch latch = new CountDownLatch(threadNum);
				System.out.println(fileNames + "请求还需下载的文件大小"
						+ (remoteSize - localSize));
				long[] startPos = new long[threadNum];
				ChildThread[] childThreads = new ChildThread[threadNum];// ChildThread
																			// 变成ChildThread1共有4处修改
				for (int i = 0; i < threadNum; i++) {
					startPos[i] = localSize + i * threadBlock; // 设置每个线程开始下载文件的起始位置
					
					childThreads[i] = new ChildThread(OStype, fileName,
							storePath, server, userName, password, startPos[i],
							i, latch); // 创建线程 线程编号从0开始
					exec.execute(childThreads[i]);// 开始执行线程
				}

				latch.await(); // 等待所有的线程都运行结束
				exec.shutdown();
				tempFileToTargetFile(localFileName, childThreads, threadNum);// 把临时得到的文件夹内的文件合并到目标文件

			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP客户端出错！", e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}

	/**
	 * @author repace 把临时文件夹内的文件都写入目标文件内 即将各个线程所下的文件进行合并
	 * @param target
	 *            目标文件 是之前用户发送的请求要把请求下载的文件存放的绝对路径的目录
	 *            比如要下载的是test.txt文件，想存在c:\\123\\文件夹内 则目标文件target
	 *            指的的就是c:\\123\\test.txt
	 * @param tempFile
	 *            临时文件夹的目录则是 c:\\123\\testTemp\\
	 * @param threadNum
	 * @return
	 * @throws IOException
	 */

	public static boolean tempFileToTargetFile(String target,
			ChildThread[] childThreads, int threadNum) throws IOException { // 完成把临时文件夹内的日志都写到目标文件中
		
		System.out.println("KAISHI HEBING");
		boolean result = true;

		FileInputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(target, true); // 追加内容
			for (int i = 0; i < threadNum; i++) { // 遍历所有子线程创建的临时文件，按顺序把下载内容写入目标文件中
				inputStream = new FileInputStream(
						childThreads[i].localTempFileName);
				int len = 0;
				byte[] b = new byte[1024];
				int count = 0;
				while ((len = inputStream.read(b)) != -1) {
					outputStream.write(b, 0, len);
					outputStream.flush();
					count += len;
				}
				inputStream.close();
				
			}
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (outputStream != null) {
			outputStream.close();
		}
		File file = new File(target);
		System.out.print(target + "下载得到的文件大小是 " + file.length());
		deleteTempFile(path);// 删除临时文件夹
		return result;
	}

	public static void deleteTempFile(String Path) {//删除临时文件夹

		File file = new File(Path);
		if (file.isFile()) {// 表示该文件不是文件夹
			file.delete();
		} else {
			// 首先得到当前的路径
			String[] childFilePaths = file.list();
			for (String childFilePath : childFilePaths) {
				File childFile = new File(file.getAbsolutePath() + "/"
						+ childFilePath);
				String s = childFile.getAbsolutePath();
				deleteTempFile(s);
			}
			file.delete();
		}
	}

}