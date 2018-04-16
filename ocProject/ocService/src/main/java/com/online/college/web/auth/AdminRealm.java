package com.online.college.web.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.online.college.common.web.SessionContext;
import com.online.college.common.web.auth.SessionUser;
import com.online.college.core.auth.domain.AuthAdmin;
import com.online.college.core.auth.service.IAuthAdminService;

public class AdminRealm extends AuthorizingRealm{
	@Autowired
	private IAuthAdminService authAdminService;
	
	/**
	 * 实现用户登陆
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authToken;
		String username = token.getUsername();
		String password = String.valueOf(token.getPassword());
		AuthAdmin authAdmin = null;
		/**
		 * 业务代码-start
		 */
		try {
			AuthAdmin tmpauthAdmin = new AuthAdmin();
			tmpauthAdmin.setUsername(username);
			tmpauthAdmin.setPassword(password);
			
			tmpauthAdmin = authAdminService.getByUsernameAndPassword(tmpauthAdmin);
			if(null != tmpauthAdmin){
				authAdmin = new AuthAdmin();
				authAdmin.setId(tmpauthAdmin.getId());
				authAdmin.setRealname(tmpauthAdmin.getRealname());
				authAdmin.setUsername(tmpauthAdmin.getUsername());
			}else{
				throw new AuthenticationException("## user password is not correct! ");
			}
		} catch (Exception e) {
			throw new AuthenticationException("## user password is not correct! ");
		}
		//业务代码-end
		// 设置用户权限信息
		/*try {
			authUser.setPermissions();
		} catch (Exception e) {
			throw new AuthenticationException("## user permission setter exception! ");
		}*/
		// 创建授权用户
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(authAdmin, password, getName());
		return info;
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (principals == null)
			throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
		// 获取当前登录用户
		SessionUser user = SessionContext.getAuthUser();
		if (user == null) {
			return null;
		}
		// 设置权限
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// 获取用户权限并设置 以供shiro框架 
		info.setStringPermissions(user.getPermissions());
		return info;
	}

}