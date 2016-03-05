package edu.xidian.windows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestService {

	
	public static String getServiceState(String service){
		excuteCommand("cmd /c net start >C:\\net.txt");

		String state = readFileByLines("C:\\net.txt",service);
		return state;
	}
	
	 /**
     * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
     */
    public static String readFileByLines(String fileName,String service) {
        File file = new File(fileName);
        BufferedReader reader = null;
        String returnStr = null;
        try {
            System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((tempString = reader.readLine()) != null) {
                // ��ʾ�к�
                System.out.println("line " + line + ": " + tempString);
                line++;
                if(tempString.trim().equals(service)){
                	System.out.println(tempString.trim());
                	return "0x0000800";
                }
            }
            reader.close();
            return "0x0000801";
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return "0x0000802";
    }
	
	/**
	 * ִ��windows����
	 * @param command
	 */
	public static boolean  excuteCommand(String command)
	{
	    Runtime r = Runtime.getRuntime();
	    Process p = null;
        try {
            p = r.exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inline;
            while ((inline = br.readLine()) != null) {
                System.out.println(inline);
            }
            br.close();
            int res = p.waitFor();
            if(res==0){
            	return true;
            }
            p.destroy();
            p=null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(TestService.getServiceState("Power"));
	}

}
