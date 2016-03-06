package sample.hello.resources;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import sample.DBOP.DBOperation;

import com.sun.jersey.api.uri.UriBuilderImpl;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class AuthFilter implements ContainerRequestFilter {
	/**
	 * Apply the filter : check input request, validate or not with user auth
	 * @param containerRequest The request from Tomcat server
	 * @return 
	 */
	private static boolean flag = false;
	@SuppressWarnings("null")
	public ContainerRequest filter(ContainerRequest containerRequest) {
		//GET, POST, PUT, DELETE, ...
//		System.out.println("==================");
		String method = containerRequest.getMethod();
		// myresource/get/56bCA for example
		String path = containerRequest.getPath(true);

		//Get the authentification passed in HTTP headers parameters
		String password = containerRequest.getHeaderValue("password");
		String d0 = containerRequest.getHeaderValue("time");
		//If the user does not have the right (does not provide any HTTP Basic Auth)
		if(password == null){
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String d1= df.format(now);
		Date d2 = null;
		Date d3 = null; 
		try {
			d2 = df.parse(d0);
			d3 = df.parse(d1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int minutes = (int)(d2.getTime()-d3.getTime())/1000/60;
		//判断客户端发送时间与服务端接收时间间隔
		if(minutes>60){
			System.out.println("##### request time out ! #####");//时间超时
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}else{
			DBOperation opt = new DBOperation();
			String pwd = null;
			String userId = null;
			try {
				String[] values = opt.judge();
				pwd = values[1];
				userId = values[0];
				opt.close();
//				System.out.println(pwd+"----");
//				System.out.println(password+"----");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//判断秘钥是否匹配
			
			if(!pwd.equals(password)){
				System.out.println("##### password error ! #####");
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}else{
//				System.out.println("original containerRequest:"+containerRequest.getRequestUri());
//				containerRequest.setUris(containerRequest.getBaseUri(), containerRequest.getRequestUriBuilder().queryParam("uid", userId).build());
//				System.out.println("containerRequest append uid:"+containerRequest.getRequestUri());
				return containerRequest;
			}
		}
}

		//TODO : HERE YOU SHOULD ADD PARAMETER TO REQUEST, TO REMEMBER USER ON YOUR REST SERVICE...
	 public void init(FilterConfig config) throws ServletException { 
	        //读取错误信息提示页面路径  
	       boolean flag = false;
	    } 
	 
	
}
