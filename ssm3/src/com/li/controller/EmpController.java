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
	
	//����
	@RequestMapping("getList")
	public ModelAndView getList(){
		ModelAndView mav=new ModelAndView();
		//����service�ӿ����鷽��
		List list=empService.getList();
		//��ֵ����
		mav.addObject("empList", list);
		//��תҳ��
		mav.setViewName("emp_list");
		return mav;
	}
	
	//ģ��+��ҳ��ѯ
	@RequestMapping("getMFList")
	public ModelAndView getMFList(@RequestParam("page") int pageNo,EmpEntity empEntity){
		ModelAndView mav=new ModelAndView();
		//1.���õ�ǰҳ��ʾ������
		int pageSize=2;
		//2.��ȡģ�������µ�������
		int sum=empService.getMList(empEntity).size();
		//3.��ȡβҳ
		int totalPage=(sum%pageSize==0)?sum/pageSize:sum/pageSize+1;
		//4.��ȡ��ǰҳ��ʾ��������Ϣ
		Map map=new HashMap();
		map.put("e_name", empEntity.getE_name());
		map.put("e_sex", empEntity.getE_sex());
		map.put("d_id", empEntity.getD_id());
		map.put("pageT",(pageNo-1)*pageSize);
		map.put("pageSize",pageSize);
		List empList=empService.getMFList(map);
		//5.��ȡ������Ϣ
		List deptList=empService.getDeptList();
		//6.��ֵ����
		mav.addObject("empList", empList);
		mav.addObject("deptList", deptList);
		mav.addObject("pageNo", pageNo);
		mav.addObject("totalPage", totalPage);
		mav.addObject("name", empEntity.getE_name());
		mav.addObject("sex", empEntity.getE_sex());
		mav.addObject("d_id", empEntity.getD_id());
		//7.��תҳ��
		mav.setViewName("emp_list");
		return mav;
	}
	
	//ajax���ɾ��---û�з���ֵ
	@ResponseBody//
	@RequestMapping("delEmp")
	public void delEmp(@RequestParam("eid") String id){
		empService.delEmp(id);
	}
	
	/*//ajax�������ɾ��---û�з���ֵ
		@ResponseBody
		@RequestMapping("pdelEmp")
		public void pdelEmp(@RequestParam("eids") String id){
			String [] ids=id.split(",");
			for(int i=0;i<ids.length;i++){
				empService.delEmp(ids[i]);
			}
			
		}*/
	//ajax���mybatis����ɾ��---û�з���ֵ
			@ResponseBody
			@RequestMapping("pdelEmp")
			public void pdelEmp(@RequestParam("eids") String id){
				String [] ids=id.split(",");//��ʱ��id��һ���ַ������ͣ�ͨ��split�������зָ��ַ������ָ��string�������͵�����
				empService.pdel(ids);//����service�ӿ�����ɾ������
			}

	//����֮ǰ--����emp_add.jspĿ����Ϊ�˻������Ϣ
	@RequestMapping("addEmpPage")
		public ModelAndView addEmpPage(){
			ModelAndView mav=new ModelAndView();
			//5.��ȡ������Ϣ
			List deptList=empService.getDeptList();
			mav.addObject("deptList", deptList);
			mav.setViewName("emp_add");
			return mav;
		}
	
	//�ϴ�����
	@ResponseBody
	@RequestMapping(value="upload",produces="application/json;charset=UTF-8")
	public String upload(@RequestParam("myfile") MultipartFile file,HttpServletRequest request) throws IllegalStateException, IOException{
		//1.��ȡ��ǰ�ϴ��ļ���·����ַ(��ʵ��·����ַ)
		String path=request.getSession().getServletContext().getRealPath("/upload");
		//2.��ȡͼƬ��ԭ����
		String trueName=file.getOriginalFilename();
		//3����·����ַ����һ���ļ���
		File targetFile=new File(path);// upload
		if(!targetFile.exists()){//�����ǰ�ļ��в�����
			targetFile.mkdirs();//������ǰ�ļ���mkdirs��������ļ��У�mkdir������һ�ļ���
		}
		//4.����һ���ļ���ͬʱ���ļ�Ҳһͬ�Ž�ȥ upload/aa.jpg
		File pathFile=new File(targetFile, trueName);
		//ִ���ϴ���ǰpathFile�ļ�
		file.transferTo(pathFile);
		//����һ��json����Ŀ���ǰѵ�ǰͼƬ��·����ַ�洢��ȥ��Ȼ�󴫵�ǰ̨չʾ
		JSONObject json=new JSONObject();
		json.put("url", "/upload/"+trueName);
		return json.toJSONString();
	}
	
	//����
	@RequestMapping("add")
	public ModelAndView add(EmpEntity empEntity){
		ModelAndView mav=new ModelAndView();
			//����id
		empEntity.setE_id(UUID.randomUUID().toString().replace("-", ""));
		//�����ϴ�ͼƬ��ʱ��
		Date date=new Date();//���ɹ���ʱ��
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		empEntity.setUploadtime(sdf.format(date));
		//����service�ӿ���������
		empService.add(empEntity);
		//��תҳ��
		mav.setViewName("redirect:/emp/getMFList?page=1");
		return mav;
	}
	//��һ��ѯ
	@RequestMapping("getOne")
	public ModelAndView getOne(@RequestParam("id") String eid){
		ModelAndView mav=new ModelAndView();
		//1.����service�ӿڵ�һ��ѯ����
		EmpEntity empEntity=empService.getOne(eid);
		//2.��ȡ�����б���Ϣ
		List deptList=empService.getDeptList();
		mav.addObject("deptList", deptList);
		mav.addObject("empEntity", empEntity);
		mav.setViewName("emp_upd");
		return mav;
	}
	
	//�޸�
	@RequestMapping("updEmp")
	public ModelAndView updEmp(EmpEntity empEntity){
		ModelAndView mav=new ModelAndView();
		Date date=new Date();//���ɹ���ʱ��
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		empEntity.setUploadtime(sdf.format(date));
		empService.upd(empEntity);
		//��תҳ��
		mav.setViewName("redirect:/emp/getMFList?page=1");
		return mav;
	}
	
	
	//ajax��----ģ��+��ҳ��ѯ
		@ResponseBody
		@RequestMapping(value="getAjaxMFList",produces="application/json;charset=UTF-8")
		public String getAjaxMFList(@RequestParam("page") int pageNo,EmpEntity empEntity){
			
			//1.���õ�ǰҳ��ʾ������
			int pageSize=2;
			//2.��ȡģ�������µ�������
			int sum=empService.getMList(empEntity).size();
			//3.��ȡβҳ
			int totalPage=(sum%pageSize==0)?sum/pageSize:sum/pageSize+1;
			//4.��ȡ��ǰҳ��ʾ��������Ϣ
			Map map=new HashMap();
			map.put("e_name", empEntity.getE_name());
			map.put("e_sex", empEntity.getE_sex());
			map.put("d_id", empEntity.getD_id());
			map.put("pageT",(pageNo-1)*pageSize);
			map.put("pageSize",pageSize);
			List empList=empService.getMFList(map);
			//5.��ȡ������Ϣ
			List deptList=empService.getDeptList();
			//6.��ֵ����
			map.put("empList", empList);
			map.put("deptList", deptList);
			map.put("pageNo", pageNo);
			map.put("totalPage", totalPage);
			String result=JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd");
			System.out.println(result);
			return result;
		}
		
		        //ajax���ҳ���----ģ��+��ҳ��ѯ
				@ResponseBody
				@RequestMapping(value="getAjaxMFListPageHelper",produces="application/json;charset=UTF-8")
				public String getAjaxMFListPageHelper(@RequestParam("page") int pageNo,EmpEntity empEntity){
					//1.���õ�ǰҳ��ʾ������
					int pageSize=2;
					//2.���÷�ҳ����ĵڼ�ҳ��ÿҳ��ʾ��������
					PageHelper.startPage(pageNo, pageSize);
					//3.��ȡģ��������ѯ��list�б�
					List<EmpEntity> list=empService.getMList(empEntity);
					//4.��ȡ��ҳ�����pageInfo����(������pageNum��ǰҳ,pages��ҳ��listģ���������б���Ϣ)
					PageInfo<EmpEntity> pageInfo=new PageInfo<EmpEntity>(list);
					//5.��ȡ�����б�
					List deptList=empService.getDeptList();
					//5.���д�ֵ����JSONObject����ֵ��ʽ������map
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("pageInfo", pageInfo);
					jsonObject.put("deptList", deptList);
					jsonObject.put("d_id", empEntity.getD_id());
					return jsonObject.toJSONString();
				}
		
		
		//ajax�İ�֮�������
		@RequestMapping("addPlus")
		public ModelAndView addPlus(EmpEntity empEntity){
			ModelAndView mav=new ModelAndView();
				//����id
			empEntity.setE_id(UUID.randomUUID().toString().replace("-", ""));
			//�����ϴ�ͼƬ��ʱ��
			Date date=new Date();//���ɹ���ʱ��
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			empEntity.setUploadtime(sdf.format(date));
			//����service�ӿ���������
			empService.add(empEntity);
			//��תҳ��
			mav.setViewName("emp_ajaxlist");
			return mav;
		}
		
		//����pageHelper��ҳ���--ģ��+��ҳ��ѯ
		@RequestMapping("getMFListPageHelper")
		public ModelAndView getMFListPageHelper(@RequestParam("page") int pageNo,EmpEntity empEntity){
			ModelAndView mav=new ModelAndView();
			//1.���õ�ǰҳ��ʾ������
			int pageSize=2;
			//2.���÷�ҳ����ĵڼ�ҳ��ÿҳ��ʾ��������
			PageHelper.startPage(pageNo, pageSize);
			//3.��ȡģ��������ѯ��list�б�
			List<EmpEntity> list=empService.getMList(empEntity);
			//4.��ȡ��ҳ�����pageInfo����(������pageNum��ǰҳ,pages��ҳ��listģ���������б���Ϣ)
			PageInfo<EmpEntity> pageInfo=new PageInfo<EmpEntity>(list);
			//5.��ȡ�����б�
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
