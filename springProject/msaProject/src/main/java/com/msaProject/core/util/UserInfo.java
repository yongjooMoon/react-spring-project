package com.msaProject.core.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.msaProject.board.model.User;


public class UserInfo implements Serializable {

	private static final long serialVersionUID = -222002029472067607L;
	
	private String id;
	private String name;
	private String birthDate;
	private Integer age;
	private String sex;
	private String phoneNumber;
	private String address;
	private String createTime;
	private String updateTime;
	
	public UserInfo() {
		super();
	}
	
	public UserInfo(String id  , String name, String birthDate, Integer age
				  , String sex, String phoneNumber, String address   , String createTime
				  , String updateTime 
			      ) {
		super();
		this.id       		= id;
		this.name     		= name;
		this.birthDate   	= birthDate;
		this.age   			= age;
		this.sex     		= sex;
		this.phoneNumber    = phoneNumber;
		this.address 	    = address;
		this.createTime     = createTime;
		this.updateTime 	= updateTime;
	}
	
	public Map<String, Object> getMaps()  {
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("id"  			, this.id);
		ret.put("name"			, this.name);
		ret.put("birthDate"  	, this.birthDate);
		ret.put("age"  			, this.age);
		ret.put("updateTime"  	, this.updateTime);
		ret.put("sex"  			, this.sex);
		ret.put("phoneNumber"  	, this.phoneNumber);
		ret.put("address"  		, this.address);
		ret.put("createTime" 	, this.createTime);
		return ret;
	}
	
	public void parseMap(Optional<User> info)  {
		if ( info != null )  {
			this.id   		= info.get().getId();
			this.name 		= info.get().getName();
			this.birthDate 	= info.get().getBirthDate();
			this.age 		= info.get().getAge();
			this.updateTime = info.get().getUpdateTime();
			this.sex   		= info.get().getSex();
			this.phoneNumber	= info.get().getPhoneNumber();
			this.address		= info.get().getAddress();
			this.createTime		= info.get().getCreateTime();
		}
	}
}