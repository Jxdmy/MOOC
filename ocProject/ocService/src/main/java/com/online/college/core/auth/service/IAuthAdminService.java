package com.online.college.core.auth.service;

import com.online.college.core.auth.domain.AuthAdmin;

public interface IAuthAdminService {
	/**
	 * 根据username 和 password获取
	 * @param authAdmin
	 * @return
	 */
	public AuthAdmin getByUsernameAndPassword(AuthAdmin authAdmin);
	
	public AuthAdmin getById(Long id);
	
	public AuthAdmin getByUsername(String username);
}
