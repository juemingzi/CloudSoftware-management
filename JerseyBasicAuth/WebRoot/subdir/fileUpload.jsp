<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<title>Upload script File </title>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'fileUpload.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
 <body>
	 Upload script File <br/><br/>
	<form action="rest/vmScript/upload" method="post" enctype="multipart/form-data">
 		 File : <input type="file" name="upload" size="50" />
	  <br/>
 	   <input type="submit" value="Upload" />
	</form>
 </body>
</html>
