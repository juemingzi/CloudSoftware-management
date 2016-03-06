package sample.hello.bean;

import ch.ethz.ssh2.Connection;  
import ch.ethz.ssh2.Session;  
import ch.ethz.ssh2.StreamGobbler;  
//import com.io.Debug;  
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStream;  


/**
 * @brief Oracle界面安装的一些特殊情况处理
 * @author wyq
 * @version 
 * @date 2015-3-30 下午6:09:10 
 *
 */
public class ChangeService {  
    private String username = "";  
    private String oldpassword = "";  
    private Connection conn = null;  
    private boolean hasError = false;  
    private String ErrorMessage = "";  
    private boolean isSuccessfully = false;  
    private String SystemMessage = "";  
     
    public static final String HOST = "119.90.140.253"; //server ip  
    public boolean isSuccessfully(){  
        return isSuccessfully;  
    }  
    public boolean isHasError(){  
        return hasError;  
    }  
    public String getErrorMessage(){  
        return ErrorMessage;  
    }  
    public void setErrorMessage(String msg){  
        hasError = true;  
        this.ErrorMessage =  msg;  
    }  
     
    public ChangeService(String username,String oldpassword) {  
        this.username = username;  
        this.oldpassword = oldpassword;  
         try{  
             conn = new Connection(HOST);  
             conn.connect();  
             
            boolean isAuthenticated = conn.authenticateWithPassword(username, oldpassword);  
            if (isAuthenticated == false) {  
                setErrorMessage("Authentication failed.");  
                conn=null;  
            }  
           
         }catch(Exception e){  
             conn.close();  
             conn = null;  
             System.out.println(e);  
         }  
    }  
    public void setservice() {  
        if(hasError){  
            return;  
        }  
        if (conn==null){  
            return;  
        }  
        try{  
            Session sess = conn.openSession();  
           // sess.execCommand("");
            sess.execCommand("/home/softsource/database/runInstaller -silent -responseFile /home/setupscripts/oracle/db_install.rsp");  
            InputStream so = sess.getStdout();  
            InputStream err = sess.getStderr();  
            OutputStream out = sess.getStdin(); 
            
             
            byte[] buffer = new byte[500];
            int length = 0;  
            length = so.read(buffer);  
            if (length > 0) {  
                System.out.println("#1:"+ new String(buffer, 0, length));  
                //(current) UNIX password:  
            }  
//            String coldpassword = oldpassword+"\n";  
//            out.write(coldpassword.getBytes());  
//            length = err.read(buffer);  
//            if (length > 0) {  
//                System.out.println("#2:"+ new String(buffer, 0, length));  
//                //New password:  
//            }  
//            String cnewpass = newpassword +"\n";  
//            out.write(cnewpass.getBytes());  
//            length = err.read(buffer);  
//            if (length > 0) {  
//                String rs = new String(buffer, 0, length);  
//                System.out.println("#3:"+rs); 
//                //Retype new password:
//                if(rs.indexOf("BAD")>-1){  
//                    sess.close();  
//                    conn.close();  
//                    setErrorMessage(rs);  
//                    return;  
//                }  
//            }  
//            out.write(cnewpass.getBytes());  
//            length = so.read(buffer);  
//            if (length > 0) {  
//                String rs = new String(buffer, 0, length); 
//                System.out.println(rs);
//                if(rs.indexOf("successfully")>-1){  
//                    this.isSuccessfully = true;  
//                    this.SystemMessage = rs;  
//                }  
//            }  
             
            sess.close();  
             
            conn.close();  
        } catch (IOException e) {  
            e.printStackTrace(System.err);  
        }  
    }  
    public String getSystemMessage() {  
        return SystemMessage;  
    }  
    
    
    public static void main(String[] args) {  
        //ChangeService cep = new ChangeService("root", "Dianzikeda2015#");  
    	ChangeService cep = new ChangeService("wenyanqi", "123456");  
        if(cep.isHasError()){  
            System.out.println(cep.getErrorMessage());  
            cep = null;  
            return;  
        }  
        cep.setservice();  
        if(cep.isHasError()){  
            System.out.println(cep.getErrorMessage());  
            cep = null;  
            return;  
        }  
        if(cep.isSuccessfully()){  
            System.out.println(cep.getSystemMessage());  
        }  
    }  
     
   
}  
