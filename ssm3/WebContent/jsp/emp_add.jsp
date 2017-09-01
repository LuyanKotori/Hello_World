<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增页面</title>
</head>
<style>
			table,tr,th,td{
					border:1px solid red;
					border-collapse:collapse;
			}
			table,h2,form{
				margin:0px auto;
				width:80%;
			}
			td,th{
				
				text-align:center;
			}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript">
					//上传功能
					function upload(){
						var formData=new FormData();//专门用来存储上传文件的数据
						formData.append("myfile",$('#file')[0].files[0]);//把上传的第一个文件封装到formData对象中
						$.ajax({
							type:'post',
							url:'${pageContext.request.contextPath }/emp/upload',
							data:formData,
							processData:false,
							contentType:false,
							success:function(msg){
								$("#img1").attr("src","${pageContext.request.contextPath }"+msg.url);
								$("input[type='hidden']").val(msg.url);
							},
							error:function(){
								alert("fsaga");
							}
						})
					}

</script>
<body>
<form action="${pageContext.request.contextPath }/emp/addPlus" method="post" >
		<table>
			<tr>
				<th>姓名</th>
				<td><input name="e_name"></td>
			</tr>
			<tr>
				<th>年龄</th>
				<td><input name="e_age"></td>
			</tr>
			<tr>
				<th>性别</th>
				<td>
					<input type="radio" name="e_sex" value="1">女
					<input type="radio" name="e_sex" value="2">男
				</td>
			</tr>
			<tr>
				<th>生日</th>
				<td><input name="e_time"></td>
			</tr>
			<tr>
				<th>部门</th>
				<td>
					<select name="d_id">
					<c:forEach items="${deptList }" var="dept">
						<option value="${dept.d_id }">${dept.d_name }</option>
					</c:forEach>
					</select>	
				</td>
			</tr>		
			<tr>
				<th>上传图片</th>
				<td>
					<img alt="图片预览" id="img1" src=""/>
					<input type="hidden" name="url" >
					<input type="file" id="file" onchange="upload()">
				</td>
			</tr>			
			<tr>
				
				<td colspan="2">
						<input type="submit" value="确定">
						<input type="button" value="返回" onclick="window.history.go(-1)">
				</td>
			</tr>				
		</table>
		
</form>
</body>
</html>