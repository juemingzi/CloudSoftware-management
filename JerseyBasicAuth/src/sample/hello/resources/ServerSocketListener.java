package sample.hello.resources;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import sample.hello.bean.TCPServer;

/**
 * 设置Tomcat启动时便运行工程
 * @author huangpeng
 *
 */
public class ServerSocketListener implements ServletContextListener {
	
	TCPServer ts;
	
	//tomcat关闭时，关闭线程，释放端口
	@SuppressWarnings("deprecation")
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		ts.stop();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		ts = new TCPServer();
		ts.start();		
	}

}
