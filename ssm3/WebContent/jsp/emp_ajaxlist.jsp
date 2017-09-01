<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ajax页面</title>
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
				$(function(){
					init(1);//初始化方法
					//执行删除方法
					$(document).on("click",".del",function(){
						//注意：此时的a标签里的value是属于自定义属性，只能用attr("value")获取value值
						var id=$(this).attr("value");
						del(id);//执行删除方法
					})
				})
				
				var pageNo=null;//初始化当前页
				var totalPage=null;//初始化尾页
				//初始化信息
				function init(page){
					$("#pageNo").val(page);//先赋值
					var pageNo=$("#pageNo").val();//后获取
					var name=$("#name").val();//获取id为name的value值
					var sex=$("input[type='radio']:checked").val();//获取type=radio被选中的input标签的value值
					var did=$("option:selected").val();//获取被选中的option的value值一般为id
					$.ajax({
						type:'post',//提交方式分为get、post
						url:'${pageContext.request.contextPath }/emp/getAjaxMFListPageHelper',
						data:{"page":pageNo,"e_name":name,"e_sex":sex,"d_id":did},
						//表示成功的回调函数，后台代码没有任何问题，可以正常运行
						success:function(msg){//msg表示后台jsonObject--pageInfo,deptList
							//---要把部门信息遍历出来--begin--
							/* $("select option")表示获取当前select标签中所有的option
							 not("select option:first")除了select标签中第一个option*/
							//在select标签除了第一个option标签除外，其他的都删除
							$("select option").not("select option:first").remove();
						//获取jsonObject里deptList部门列表，然后进行遍历输出
							$(msg.deptList).each(function(index,items){
								//在select标签中添加option子元素
								$("select").append("<option value='"+items.d_id+"'>"+items.d_name+"</option>")
							});
						//	为当前select里option赋value值，赋的是id,目的是选择部门的时候回填部门
							$("select").val(msg.d_id);
						//----------end--------
							//把员工信息遍历出来-begin--
							var esex="";//初始化一个变量，目的是用判断性别男和女
						//在table表格的tr中除了第一行和最后一行，其他的都删除
							$("table tr").not("table tr:first").not("table tr:last").remove();
							//获取jsonObject里的pageInfo插件类里的list容器然后进行遍历，此时list里存储的员工emp列表
							$(msg.pageInfo.list).each(function(index,items){//items表示下标元素可以认为此时的items就表示当前list的别名
								var e_sex=items.e_sex;//获取e_sex
								if(e_sex==1){
									esex="女";
								}else if(e_sex==2){
									esex="男";
								}
								//拼接行
								var tr="<tr>"+
											"<td><input type='checkbox' name='check' value='"+items.e_id+"'></td>"+
											"<td>"+items.e_name+"</td>"+
											"<td>"+items.e_age+"</td>"+
											"<td>"+esex+"</td>"+
											"<td>"+items.e_time+"</td>"+
											"<td>"+items.deptEntity.d_name+"</td>"+
											"<td>"+
												"<a class='del' value='"+items.e_id+"' href='#'>删除</a>|"+
												"<a class='upd' value='"+items.e_id+"'>修改</a>"+
											"</td>"+
										"</tr>";
								$("table tr:last").before(tr);//把拼接好的行添加到table表格中最后一行之前	
							});
						//----------end----------------	
						//-------------分页条 begin----------------------
							pageNo=msg.pageInfo.pageNum;//此时从pageInfo插件类中获取的pageNum表示当前页
							var firstPage=pageNo-1;
							var nextPage=pageNo+1;
							totalPage=msg.pageInfo.pages;//此时的pages表示尾页
							//配置分页信息
							//表示table表格中最后一行里第i个a标签添加href属性
							$("table tr:last a:eq(0)").attr("href","javascript:to_page(1)");//首页
							$("table tr:last a:eq(1)").attr("href","javascript:to_page('"+firstPage+"')");//上一页
							$("table tr:last a:eq(2)").attr("href","javascript:to_page('"+nextPage+"')");//下一页
							$("table tr:last a:eq(3)").attr("href","javascript:to_page('"+totalPage+"')");//尾页
							//table表格中最后一行里的label标签添加文本信息
							$("table tr:last label").text(pageNo+"/"+totalPage+"页");
							$("#page").val(pageNo);//为id为page的元素里的value赋值，赋的是当前页码
						//-------------end---------------
						}
					});
				}
				//模糊查询
				function search(){
					//当触发搜索按钮的时候自动执行init方法
					init(1);
				}
				//分页
				function to_page(page){
					//判断上下页的界限
					if(page<=1){
						page=1;
					}
					//totalPage是作为全局变量存在，而且初始化=null,赋值是在init方法中完成的
					else if(page>=totalPage){
						page=totalPage;
					}
					init(page);
				}
				//Go方法
				function GO(){
					var page=$("#page").val();
					to_page(page);
				}
				
				//ajax版删除
				function del(id){
					$.ajax({
						type:'post',
						url:'${pageContext.request.contextPath }/emp/delEmp',
						data:{eid:id},
						success:function(msg){
							//回调函数成功之后重新加载init方法，目的是局部刷新
							init(1);
						},
						error:function(){
							alert("大爷你错了");
						}
					})
				}
				
				//批量删除
				function pdel(){
					//声明一个空的数组，目的是为了承载被选中行的id值
					var array=new Array();
					//目的是知道被选中几行数据，
					//$("input[name='check']:checked")被选中的name=check的input标签
					var sum=$("input[name='check']:checked").length;//被选中的name=check的input标签的个数
					if(sum<=0){//如果没有选中任何一行
						alert("至少要选择一项！");
					}else{//否则，选中行
						if(confirm("确定删除被选中的数据？")){//弹窗出提示信息，如果点击确定
							//被选中的name=check的input标签进行遍历
							$("input[name='check']:checked").each(function(){
								var id=$(this).val();//获取当前被选中的复选框的value值，此时value属性值是每行数据的id
							//push()表示添加数据，array.push(id)表示把遍历出来的id添加到array数组中
								array.push(id);//此时的array数组是一个装载着被选中行的id的容器
							})
							$.ajax({
							type:'post',
							url:'${pageContext.request.contextPath }/emp/pdelEmp',
							//data是ajax用来传输数据的，array.toString()把数组转化为字符串形式传过去
							//data传输数据的形式，采用的k-v键值对的形式，k和v之间用冒号间隔，eids是自定义的k值，array.toString()是v值
							data:{eids:array.toString()},
							success:function(msg){//回调函数成功之后重新加载init()方法，页码是第一页
								init(1);
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

	<!-- 模糊+分页-->
	<form id="form1" action="#" method="post">
			性别：<input type="radio" name="e_sex" value="0" checked>全部
				<input type="radio" name="e_sex" value="1">女
				<input type="radio" name="e_sex" value="2">男
			姓名:<input name="e_name" value="${name }" id="name">
			部门：<select name="d_id">
					<option value="">--全部--</option>
				</select>
			
			
			<input type="hidden" name="page" id="pageNo" value="">
			<input type="button" value="搜索" onclick="search()">
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
		
			<tr>
				<TD COLSPAN="7">
						<A href="#">首页</A>
						<A href="#">上一页</A>
						<label></label>
						<A href="#">下一页</A>
						<A href="#">尾页</A>
						<input id="page" style="width:50px" value="${pageNo }">
						<input type="button" id="GO" value="GO" onclick="GO()">
				</TD>
				
			</tr>
		</table>
</body>
</html>