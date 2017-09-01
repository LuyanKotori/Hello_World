package com.li.entity;

public class EmpEntity {
	
	private String e_id;
	private String e_name;
	private int e_age;
	private int e_sex;
	private String e_time;
	private String d_id;
	private String url;
	private String uploadtime;
	
	private DeptEntity deptEntity;//在多的一方实体类中加入一的一方的实体类。
	
	
	
	
	public String getUploadtime() {
		return uploadtime;
	}
	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public DeptEntity getDeptEntity() {
		return deptEntity;
	}
	public void setDeptEntity(DeptEntity deptEntity) {
		this.deptEntity = deptEntity;
	}
	public String getE_id() {
		return e_id;
	}
	public void setE_id(String e_id) {
		this.e_id = e_id;
	}
	public String getE_name() {
		return e_name;
	}
	public void setE_name(String e_name) {
		this.e_name = e_name;
	}
	public int getE_age() {
		return e_age;
	}
	public void setE_age(int e_age) {
		this.e_age = e_age;
	}
	public int getE_sex() {
		return e_sex;
	}
	public void setE_sex(int e_sex) {
		this.e_sex = e_sex;
	}
	public String getE_time() {
		return e_time;
	}
	public void setE_time(String e_time) {
		this.e_time = e_time;
	}
	public String getD_id() {
		return d_id;
	}
	public void setD_id(String d_id) {
		this.d_id = d_id;
	}
	
	
	
	
	
	
	
}
