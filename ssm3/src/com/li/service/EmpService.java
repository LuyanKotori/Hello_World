package com.li.service;

import java.util.List;
import java.util.Map;

import com.li.entity.EmpEntity;

public interface EmpService {
	//����dept ��emp
		public List getList();
		
		//ģ����ѯ
		public List getMList(EmpEntity empEntity);
		
		//ģ��+��ҳ
		public List getMFList(Map map);
		
		//��ѯ���ű�
		public List getDeptList();
		
		public void delEmp(String id);
		//mybatis����ɾ��
		public void pdel(String[] ids);
		
		//����
		public void add(EmpEntity empEntity);
		
		//��һ��ѯ
		public EmpEntity getOne(String id);
		
		//�޸�
		public void upd(EmpEntity empEntity);
		
		
}