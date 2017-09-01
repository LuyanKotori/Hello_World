package com.li.service;

import java.util.List;
import java.util.Map;

import com.li.entity.EmpEntity;

public interface EmpService {
	//联查dept 和emp
		public List getList();
		
		//模糊查询
		public List getMList(EmpEntity empEntity);
		
		//模糊+分页
		public List getMFList(Map map);
		
		//查询部门表
		public List getDeptList();
		
		public void delEmp(String id);
		//mybatis批量删除
		public void pdel(String[] ids);
		
		//新增
		public void add(EmpEntity empEntity);
		
		//单一查询
		public EmpEntity getOne(String id);
		
		//修改
		public void upd(EmpEntity empEntity);
		
		
}
