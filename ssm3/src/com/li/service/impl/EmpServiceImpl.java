package com.li.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.li.dao.EmpDao;
import com.li.entity.EmpEntity;
import com.li.service.EmpService;

@Service
public class EmpServiceImpl implements EmpService {
	@Autowired
	private EmpDao dao;
	//����dept ��emp
	@Override
	public List getList() {
		// TODO Auto-generated method stub
		return dao.getList();
	}
	//ģ����ѯ
	@Override
	public List getMList(EmpEntity empEntity) {
		// TODO Auto-generated method stub
		return dao.getMList(empEntity);
	}
	
	//ģ��+��ҳ
	@Override
	public List getMFList(Map map) {
		// TODO Auto-generated method stub
		return dao.getMFList(map);
	}
	
	//��ѯ���ű�
	@Override
	public List getDeptList() {
		// TODO Auto-generated method stub
		return dao.getDeptList();
	}
	
	@Override
	public void delEmp(String id) {
		// TODO Auto-generated method stub
		dao.delEmp(id);
	}
	//mybatis����ɾ��
	@Override
	public void pdel(String[] ids) {
		dao.pdel(ids);
	}
	
	
	@Override
	public void add(EmpEntity empEntity) {
		// TODO Auto-generated method stub
		dao.add(empEntity);
	}
	
	@Override
	public EmpEntity getOne(String id) {
		// TODO Auto-generated method stub
		return dao.getOne(id);
	}
	
	@Override
	public void upd(EmpEntity empEntity) {
		// TODO Auto-generated method stub
		dao.upd(empEntity);
	}
	

}
