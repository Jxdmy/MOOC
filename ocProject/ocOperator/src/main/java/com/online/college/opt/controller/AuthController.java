package com.online.college.opt.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.online.college.common.util.EncryptUtil;
import com.online.college.common.web.SessionContext;
import com.online.college.core.auth.domain.AuthAdmin;
import com.online.college.core.auth.service.IAuthAdminService;


/**
 * 用户登录 & 注册
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private IAuthAdminService iAuthAdminService;
	
	/**
	 * 登录页面
	 */
	@RequestMapping(value = "/login")
	public  ModelAndView login(){
		if(SessionContext.isLogin()){
			return new ModelAndView("redirect:/index.html");
		}
		return new ModelAndView("auth/login");
	}
	
	@RequestMapping(value = "/doLogin")
	public ModelAndView doLogin(AuthAdmin admin, String identiryCode, HttpServletRequest request){
		
		//如果已经登录过
		if(SessionContext.getAuthUser() != null){
			return new ModelAndView("redirect:/index.html");
		}
		
		//验证码判断
		if(identiryCode!=null && !identiryCode.equalsIgnoreCase(SessionContext.getIdentifyCode(request))){
			ModelAndView mv = new ModelAndView("auth/login");
			mv.addObject("errcode", 1);
			return mv;
		}
		UsernamePasswordToken token = new UsernamePasswordToken(admin.getUsername(),EncryptUtil.encodedByMD5(admin.getPassword()));
		try {
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.login(token);//shiro实现登录
			return new ModelAndView("redirect:/index.html");
		}catch(AuthenticationException e){ //登录失败
			ModelAndView mv = new ModelAndView("auth/login");
			mv.addObject("errcode", 2);
			return mv;
		}
	}
	
	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpServletRequest request) {
		SessionContext.shiroLogout();
		return new ModelAndView("redirect:/index.html");
	}
	
}
