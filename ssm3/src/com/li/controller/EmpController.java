package com.li.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.li.entity.EmpEntity;
import com.li.service.EmpService;



@Controller
@RequestMapping("emp")
public class EmpController {
	@Autowired
	private EmpService empService;
	
	//联查
	@RequestMapping("getList")
	public ModelAndView getList(){
		ModelAndView mav=new ModelAndView();
		//调用service接口联查方法
		List list=empService.getList();
		//传值处理
		mav.addObject("empList", list);
		//跳转页面
		mav.setViewName("emp_list");
		return mav;
	}
	
	//模糊+分页查询
	@RequestMapping("getMFList")
	public ModelAndView getMFList(@RequestParam("page") int pageNo,EmpEntity empEntity){
		ModelAndView mav=new ModelAndView();
		//1.设置当前页显示的条数
		int pageSize=2;
		//2.获取模糊条件下的总条数
		int sum=empService.getMList(empEntity).size();
		//3.获取尾页
		int totalPage=(sum%pageSize==0)?sum/pageSize:sum/pageSize+1;
		//4.获取当前页显示的数据信息
		Map map=new HashMap();
		map.put("e_name", empEntity.getE_name());
		map.put("e_sex", empEntity.getE_sex());
		map.put("d_id", empEntity.getD_id());
		map.put("pageT",(pageNo-1)*pageSize);
		map.put("pageSize",pageSize);
		List empList=empService.getMFList(map);
		//5.获取部门信息
		List deptList=empService.getDeptList();
		//6.传值处理
		mav.addObject("empList", empList);
		mav.addObject("deptList", deptList);
		mav.addObject("pageNo", pageNo);
		mav.addObject("totalPage", totalPage);
		mav.addObject("name", empEntity.getE_name());
		mav.addObject("sex", empEntity.getE_sex());
		mav.addObject("d_id", empEntity.getD_id());
		//7.跳转页面
		mav.setViewName("emp_list");
		return mav;
	}
	
	//ajax版的删除---没有返回值
	@ResponseBody//
	@RequestMapping("delEmp")
	public void delEmp(@RequestParam("eid") String id){
		empService.delEmp(id);
	}
	
	/*//ajax版的批量删除---没有返回值
		@ResponseBody
		@RequestMapping("pdelEmp")
		public void pdelEmp(@RequestParam("eids") String id){
			String [] ids=id.split(",");
			for(int i=0;i<ids.length;i++){
				empService.delEmp(ids[i]);
			}
			
		}*/
	//ajax版的mybatis批量删除---没有返回值
			@ResponseBody
			@RequestMapping("pdelEmp")
			public void pdelEmp(@RequestParam("eids") String id){
				String [] ids=id.split(",");//此时的id是一个字符串类型，通过split方法进行分割字符串，分割成string数组类型的数据
				empService.pdel(ids);//调用service接口批量删除方法
			}

	//新增之前--进入emp_add.jsp目的是为了回填部门信息
	@RequestMapping("addEmpPage")
		public ModelAndView addEmpPage(){
			ModelAndView mav=new ModelAndView();
			//5.获取部门信息
			List deptList=empService.getDeptList();
			mav.addObject("deptList", deptList);
			mav.setViewName("emp_add");
			return mav;
		}
	
	//上传功能
	@ResponseBody
	@RequestMapping(value="upload",produces="application/json;charset=UTF-8")
	public String upload(@RequestParam("myfile") MultipartFile file,HttpServletRequest request) throws IllegalStateException, IOException{
		//1.获取当前上传文件的路径地址(真实的路径地址)
		String path=request.getSession().getServletContext().getRealPath("/upload");
		//2.获取图片的原名称
		String trueName=file.getOriginalFilename();
		//3根据路径地址创建一个文件夹
		File targetFile=new File(path);// upload
		if(!targetFile.exists()){//如果当前文件夹不存在
			targetFile.mkdirs();//创建当前文件夹mkdirs创建多层文件夹，mkdir创建单一文件夹
		}
		//4.创建一个文件夹同时把文件也一同放进去 upload/aa.jpg
		File pathFile=new File(targetFile, trueName);
		//执行上传当前pathFile文件
		file.transferTo(pathFile);
		//创建一个json对象，目的是把当前图片的路径地址存储进去，然后传到前台展示
		JSONObject json=new JSONObject();
		json.put("url", "/upload/"+trueName);
		return json.toJSONString();
	}
	
	//新增
	@RequestMapping("add")
	public ModelAndView add(EmpEntity empEntity){
		ModelAndView mav=new ModelAndView();
			//生成id
		empEntity.setE_id(UUID.randomUUID().toString().replace("-", ""));
		//生成上传图片的时间
		Date date=new Date();//生成国际时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		empEntity.setUploadtime(sdf.format(date));
		//调用service接口新增方法
		empService.add(empEntity);
		//跳转页面
		mav.setViewName("redirect:/emp/getMFList?page=1");
		return mav;
	}
	//单一查询
	@RequestMapping("getOne")
	public ModelAndView getOne(@RequestParam("id") String eid){
		ModelAndView mav=new ModelAndView();
		//1.调用service接口单一查询方法
		EmpEntity empEntity=empService.getOne(eid);
		//2.获取部门列表信息
		List deptList=empService.getDeptList();
		mav.addObject("deptList", deptList);
		mav.addObject("empEntity", empEntity);
		mav.setViewName("emp_upd");
		return mav;
	}
	
	//修改
	@RequestMapping("updEmp")
	public ModelAndView updEmp(EmpEntity empEntity){
		ModelAndView mav=new ModelAndView();
		Date date=new Date();//生成国际时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		empEntity.setUploadtime(sdf.format(date));
		empService.upd(empEntity);
		//跳转页面
		mav.setViewName("redirect:/emp/getMFList?page=1");
		return mav;
	}
	
	
	//ajax版----模糊+分页查询
		@ResponseBody
		@RequestMapping(value="getAjaxMFList",produces="application/json;charset=UTF-8")
		public String getAjaxMFList(@RequestParam("page") int pageNo,EmpEntity empEntity){
			
			//1.设置当前页显示的条数
			int pageSize=2;
			//2.获取模糊条件下的总条数
			int sum=empService.getMList(empEntity).size();
			//3.获取尾页
			int totalPage=(sum%pageSize==0)?sum/pageSize:sum/pageSize+1;
			//4.获取当前页显示的数据信息
			Map map=new HashMap();
			map.put("e_name", empEntity.getE_name());
			map.put("e_sex", empEntity.getE_sex());
			map.put("d_id", empEntity.getD_id());
			map.put("pageT",(pageNo-1)*pageSize);
			map.put("pageSize",pageSize);
			List empList=empService.getMFList(map);
			//5.获取部门信息
			List deptList=empService.getDeptList();
			//6.传值处理
			map.put("empList", empList);
			map.put("deptList", deptList);
			map.put("pageNo", pageNo);
			map.put("totalPage", totalPage);
			String result=JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd");
			System.out.println(result);
			return result;
		}
		
		        //ajax版分页插件----模糊+分页查询
				@ResponseBody
				@RequestMapping(value="getAjaxMFListPageHelper",produces="application/json;charset=UTF-8")
				public String getAjaxMFListPageHelper(@RequestParam("page") int pageNo,EmpEntity empEntity){
					//1.设置当前页显示的条数
					int pageSize=2;
					//2.设置分页插件的第几页和每页显示几条数据
					PageHelper.startPage(pageNo, pageSize);
					//3.获取模糊条件查询的list列表
					List<EmpEntity> list=empService.getMList(empEntity);
					//4.调取分页插件的pageInfo的类(包含了pageNum当前页,pages总页，list模糊条件的列表信息)
					PageInfo<EmpEntity> pageInfo=new PageInfo<EmpEntity>(list);
					//5.获取部门列表
					List deptList=empService.getDeptList();
					//5.进行传值利用JSONObject，传值方式类似于map
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("pageInfo", pageInfo);
					jsonObject.put("deptList", deptList);
					jsonObject.put("d_id", empEntity.getD_id());
					return jsonObject.toJSONString();
				}
		
		
		//ajax改版之后的新增
		@RequestMapping("addPlus")
		public ModelAndView addPlus(EmpEntity empEntity){
			ModelAndView mav=new ModelAndView();
				//生成id
			empEntity.setE_id(UUID.randomUUID().toString().replace("-", ""));
			//生成上传图片的时间
			Date date=new Date();//生成国际时间
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			empEntity.setUploadtime(sdf.format(date));
			//调用service接口新增方法
			empService.add(empEntity);
			//跳转页面
			mav.setViewName("emp_ajaxlist");
			return mav;
		}
		
		//基于pageHelper分页插件--模糊+分页查询
		@RequestMapping("getMFListPageHelper")
		public ModelAndView getMFListPageHelper(@RequestParam("page") int pageNo,EmpEntity empEntity){
			ModelAndView mav=new ModelAndView();
			//1.设置当前页显示的条数
			int pageSize=2;
			//2.设置分页插件的第几页和每页显示几条数据
			PageHelper.startPage(pageNo, pageSize);
			//3.获取模糊条件查询的list列表
			List<EmpEntity> list=empService.getMList(empEntity);
			//4.调取分页插件的pageInfo的类(包含了pageNum当前页,pages总页，list模糊条件的列表信息)
			PageInfo<EmpEntity> pageInfo=new PageInfo<EmpEntity>(list);
			//5.获取部门列表
			List deptList=empService.getDeptList();
			mav.addObject("pageInfo", pageInfo);
			mav.addObject("deptList", deptList);
			mav.addObject("name", empEntity.getE_name());
			mav.addObject("sex", empEntity.getE_sex());
			mav.addObject("d_id", empEntity.getD_id());
			mav.setViewName("emp_listpageHelper");
			return mav;
		}
		
}
