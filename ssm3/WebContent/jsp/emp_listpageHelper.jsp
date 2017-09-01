<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>1111</title>
</head>
<style>
			table,tr,th,td{
					border:1px solid red;
					border-collapse:collapse;
			}
			table,h2,form,div{
				margin:0px auto;
				width:50%;
			}
			td,th{
				text-align:center;
			}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-3.1.1.min.js"></script>
<script>
				//分页功能
				/* function to_page(page1){
					var page=parseInt(page1);
					var total="${totalPage}";
					if(page<=1){
						page=1;
					}else if(page>=total){
						page=total;
					}
					window.location.href="${pageContext.request.contextPath }/emp/getFenList?page="+page;
				}*/
				
				//go方法
				$(function(){
					$("#GO").click(function(){
						var page=$("#page").val();
						to_page(page);
					})
				}); 
				
				function to_page(page1){
					var page=parseInt(page1);
					var total="${pageInfo.pages}";
					if(page<=1){
						page=1;
					}else if(page>=total){
						page=total;
					}
					$("#pageNo").val(page);//为当前form 表单中pageNo赋值处理
					$("#form1").submit();//通过form表单的action进行跳转
				}
				
				
				//ajax版删除
				function del(id){
					$.ajax({
						type:'post',
						url:'${pageContext.request.contextPath }/emp/delEmp',
						data:{eid:id},
						success:function(msg){
							location.reload();
						},
						error:function(){
							alert("大爷你错了");
						}
					})
				}
				
				
				//批量删除
				function pdel(){
					var array=new Array();
					var sum=$("input[name='check']:checked").length;
					if(sum<=0){
						alert("至少要选择一项！");
					}else{
						if(confirm("确定删除被选中的数据？")){
							$("input[name='check']:checked").each(function(){
								var id=$(this).val();
								array.push(id);
							})
							
							$.ajax({
							type:'post',
							url:'${pageContext.request.contextPath }/emp/pdelEmp',
							data:{eids:array.toString()},
							success:function(msg){
								location.reload();
							},
							error:function(){
								alert("大爷你错了");
							}
						})
						}
					}
				}
</script>
<body>
	<div>
		<a href="${pageContext.request.contextPath }/emp/addEmpPage">新增</a>
	</div>
	
<%-- 	单纯的模糊查询
<form action="${pageContext.request.contextPath }/emp/getMList" method="post">
			性别：<input type="radio" name="e_sex" value="0" checked>全部
				<input type="radio" name="e_sex" value="1"
				<c:if test="${sex==1 }">
					checked="checked"
				</c:if>
				>女
				<input type="radio" name="e_sex" value="2"
				<c:if test="${sex==2 }">
					checked="checked"
				</c:if>
				>男
			姓名:<input name="e_name" value="${name }">
			<input type="submit" value="搜索">
	</form> --%>
	<!-- 模糊+分页-->
	<form id="form1" action="${pageContext.request.contextPath }/emp/getMFList" method="post">
			性别：<input type="radio" name="e_sex" value="0" checked>全部
				<input type="radio" name="e_sex" value="1"
				<c:if test="${sex==1 }">
					checked="checked"
				</c:if>
				>女
				<input type="radio" name="e_sex" value="2"
				<c:if test="${sex==2 }">
					checked="checked"
				</c:if>
				>男
			姓名:<input name="e_name" value="${name }">
			部门：<select name="d_id">
					<option value="">--全部--</option>
				<c:forEach items="${deptList }" var="dept">
					<option value="${dept.d_id }" 
						<c:if test="${dept.d_id==d_id }">
							selected="selected"
						</c:if>
					>${dept.d_name }</option>
				</c:forEach>
				</select>
			
			
			<input type="hidden" name="page" id="pageNo" value="1">
			<input type="submit" value="搜索">
	</form>
		<table>
			<tr>
				<th><a href="javascript:pdel()">批量删除</a></th>
				<th>姓名</th>
				<th>年龄</th>
				<th>性别</th>
				<th>生日</th>
				<th>部门</th>
				<th>操作</th>
			</tr>
		<c:forEach items="${pageInfo.list }" var="emp">
			<tr>
				<td><input type="checkbox" name="check" value="${emp.e_id }"></td>
				<td>${emp.e_name }</td>
				<td>${emp.e_age }</td>
			<c:if test="${emp.e_sex==1 }">
				<td>女</td>
			</c:if>
			<c:if test="${emp.e_sex==2 }">
				<td>男</td>
			</c:if>
				<td>${emp.e_time }</td>
				<td>${emp.deptEntity.d_name }</td>
				<td>
					<a href="javascript:void(0)" onclick="del('${emp.e_id}')">删除</a>
					<a href="${pageContext.request.contextPath }/emp/getOne?id=${emp.e_id}">修改</a>
					<a href="${pageContext.request.contextPath }/">详情</a>
				</td>
			</tr>
		</c:forEach>	
			<tr>
				<TD COLSPAN="7">
						<A href="javascript:to_page(1)">首页</A>
						<A href="javascript:to_page(${pageInfo.pageNum-1 })">上一页</A>
						${pageInfo.pageNum }/${pageInfo.pages }页
						<A href="javascript:to_page(${pageInfo.pageNum+1 })">下一页</A>
						<A href="javascript:to_page(${pageInfo.pages })">尾页</A>
						<input id="page" style="width:50px" value="${pageInfo.pageNum }">
						<input type="button" id="GO" value="GO">
				</TD>
				
			</tr>
		</table>
</body>
</html>