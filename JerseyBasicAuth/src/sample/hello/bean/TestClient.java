package sample.hello.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client
 * @author Icer
 *
 */
public class TestClient {
    
    private Socket s;
    private BufferedReader br;
    //private BufferedReader line;
    private PrintWriter pw;
    private String line = "";
    
    public TestClient(String path) throws Exception {
        try{
            s = new Socket("127.0.0.1",11900);          
            pw = new PrintWriter(s.getOutputStream(),true);            
            readFileByLines(path);//想要传送的文件名             
            pw.close();
        }catch(IOException ie){
            ie.printStackTrace();
        }
    }    
    
    public  void readFileByLines(String fileName) { //逐行读文件
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            pw.println(file.getName());//流中第一行存文件名字和后缀
            int i = 1;            
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
               // System.out.println(tempString);
                pw.println(tempString); //把文件内容写到socket流中，发送到服务器端
                i++;
            }
            reader.close();
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
    }
    
    public static void main(String[] args) throws Exception {
    	String filePath="C:\\Users\\repace\\Desktop\\test.bat";
        new TestClient(filePath);
    }
}