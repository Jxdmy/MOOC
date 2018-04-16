package com.online.college.core.auth.domain;

import java.util.Set;

import com.online.college.common.web.auth.SessionUser;

public class AuthAdmin implements SessionUser{

	private Long id;
	/**
	*真实姓名
	**/
	private String realname;

	/**
	*登录用户名
	**/
	private String username;

	/**
	*密码
	**/
	private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Long getUserId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public Set<String> getPermissions() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
