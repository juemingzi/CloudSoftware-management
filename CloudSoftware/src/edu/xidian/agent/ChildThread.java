package edu.xidian.agent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.net.ftp.FTPClient;

public class ChildThread extends Thread {//���ڶ��߳�����
	
	
	public int id;
	private long startPosition;
	CountDownLatch latch;
		
	String remoteFileName;  //Ҫ���ص��ļ���������ĵ��ļ�
	String localTempFileName;   //���ڴ��ÿ���߳����ص���ʱ�ļ��ľ���·��  ��������ʱ�ļ������ֺͺ�׺��
	String path;//��ʱ�ļ��е�Ŀ¼
	
	FTPClient ftpClient = new FTPClient();
	
	public ChildThread(String OStype,String fileName, String storePath,
			String server, String userName, String password,long startPos,int id,CountDownLatch latch) {
				
		ftpClient.enterLocalPassiveMode(); // ��һ�仰һ��Ҫ�ǵü���
		remoteFileName = "/mnt/data/ftp/www/"+OStype.toLowerCase() +"/"+ fileName; // �������ϵ��ļ���ǰ�����ļ��е����֣���������ļ�������
		startPosition=startPos;
		this.latch=latch;  
		this.id=id;
		try {
			ftpClient.connect(server);
			ftpClient.login(userName, password);
			ftpClient.setBufferSize(1024);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);	// �����ļ����ͣ������ƣ�	
				
			if (storePath.endsWith("\\") || storePath.endsWith("/"))//������·�����½�һ����ʱ�ļ��У�����洢���Ǹ����߳����ص��ļ�
				{
					localTempFileName=storePath +fileName.substring(0, fileName.indexOf("."))+"Temp/" +id+"_"+fileName;//��֤��ʱ�ļ���Ψһ ҲӦ��֤��ʱ�ļ�������Ψһ
				} else{
					localTempFileName=storePath + "/" +fileName.substring(0, fileName.indexOf("."))+"Temp/" + id+"_"+fileName;
				}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP�ͻ��˳���", e);
		} finally {
		}
	}

	
	public void run() {
		
		FileOutputStream outputStream = null;
			try {
				File threadTempFile=new File(localTempFileName);
				outputStream = new FileOutputStream(localTempFileName,true);
				ftpClient.setRestartOffset(startPosition+threadTempFile.length());  //����ÿ���߳̿�ʼ������λ��  ���֮ǰthreadTempFile.length()������0������ϴ��Ǹ��ط���������  �ϵ�����
				
				InputStream in= ftpClient.retrieveFileStream(remoteFileName);
				int len = 0;
				byte[] b = new byte[1024];
				long count=threadTempFile.length();
				while ( (len = in.read(b)) != -1) { 
					count +=len;//��¼�ļ��еĳ��ȼ������׼��д�ĳ���
					if (count > FtpTransFile.threadBlock) { //�������һ�ζ������Ѿ��ȹ涨���߳̿����ֻȡǰ��һ���ּ���
						int lastLen= (int) (FtpTransFile.threadBlock-threadTempFile.length());
						outputStream.write(b, 0,lastLen);//����write(b, off, len)��b[off]��д��ĵ�һ���ֽں�b[off+len-1]��д��������������һ���ֽڡ�
						outputStream.flush();
						break;
					}
					outputStream.write(b, 0, len);
					outputStream.flush();
				}				
				in.close();//�ر���
				File file=new File(localTempFileName);
System.out.println("Thread file "+id+" "+file.length());
				outputStream.close();
				ftpClient.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		latch.countDown();//ÿ���߳̽�����ʱ�����ܵ��߳�����1		
	}
}
