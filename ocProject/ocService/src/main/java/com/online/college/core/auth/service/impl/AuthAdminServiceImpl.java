package com.online.college.core.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.college.core.auth.dao.AuthAdminDao;
import com.online.college.core.auth.domain.AuthAdmin;
import com.online.college.core.auth.service.IAuthAdminService;
@Service
public class AuthAdminServiceImpl implements IAuthAdminService{

	@Autowired
	private AuthAdminDao authAdminDao;
	/**
	*根据username获取
	**/
	public AuthAdmin getByUsername(String username){
		return authAdminDao.getByUsername(username);
	}
	
	
	
	public AuthAdmin getById(Long id){
		return authAdminDao.getById(id);
	}
	
	/**
	*根据username和password获取
	**/
	public AuthAdmin getByUsernameAndPassword(AuthAdmin authAdmin){
		return authAdminDao.getByUsernameAndPassword(authAdmin);
	}
}
