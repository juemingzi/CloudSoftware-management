<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    	
    <title>登录界面</title>
    
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
   <form action="Client"  method="POST">
	<%-- <%="Token： " + request.getParameter("authentication") %><br/><br/> --%>
	<%="Token： " + request.getSession().getAttribute("authentication") %><br/><br/>
  	 U R L  :  <textarea style="width: 900px" type = "text"  name = "url" ></textarea>
   <select name="patten" style="height: 30px" >
   				<option value="PUT"  selected>PUT</option>
   				<option value="GET" >GET</option>
   				<option value="POST" >POST</option>
   				<option value="DELETE" >DELETE</option>
   				</select><br/><br/><br/><br/><br/>
   	Token : <input type="text" name="token" value="Token"/><br/><br/><br/>
	
	<br />
  	 提   交：<input  type = "submit"  name="submit" value="提交"/>
   </form>
  </body>
   <!--  <script src="js/libs/jquery-2.0.2.min.js"></script>
  <script src="js/add/ajaxfileupload.js"></script>
<script>
			function ajaxfileupload(){
				//1.上传文件
				var filepath = $("#file").val();
				if($("#file").val()==""){
					//alert($("#filepath").val());
					//alert("上传文件不能为空!");
					$('#warnTag').html("上传文件不能为空!");
					return false;
				}
				var username=$(".username").val();
				var pwd = $(".pwd").val();
				var url = $(".url").val();
				var patten = $(".patten").val();
				$.ajax({
						type:"post",
						url:"Client",
						data:{username:username,pwd:pwd,url:url,patten:patten},
						success:function(data){
							/* alert(data);
							$.ajaxFileUpload({		
								url:"rest/vmScript/upload",
								secureuri:false,
								fileElementId:'file',	
								success: function (data) {
									alert(data);
								},
								error: function (data, status, e){
									alert("error:"+data);
								} */
							});	
					}
										
				});
			
							
			}
			
		</script> -->
</html>
