package com.online.college.portal.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.online.college.common.storage.QiniuStorage;
import com.online.college.common.util.EncryptUtil;
import com.online.college.common.web.JsonView;
import com.online.college.common.web.SessionContext;
import com.online.college.core.auth.domain.AuthUser;
import com.online.college.core.auth.service.IAuthUserService;
import com.online.college.core.consts.domain.ConstsCollege;
import com.online.college.core.consts.service.IConstsCollegeService;


/**
 * 用户登录 & 注册
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private IAuthUserService authUserService;
	
	@Autowired
	private IConstsCollegeService collegeService;
	/**
	 * 注册页面
	 */
	@RequestMapping(value = "/register")
	public  ModelAndView register(ModelAndView mv,ConstsCollege collegeEntity){
		List<ConstsCollege> college = collegeService.queryAll(collegeEntity);
		mv.addObject("college",college);
		mv.setViewName("auth/register");
		if(SessionContext.isLogin()){
			mv.setViewName("redirect:/index.html");
			return mv;
		}
		return mv;
	}
	
	/**
	 * 实现注册
	 */
	@RequestMapping(value = "/doRegister")
	@ResponseBody
	public String doRegister(AuthUser authUser, String identiryCode, HttpServletRequest request,@RequestParam MultipartFile pictureImg) {
		//验证码判断
		if(identiryCode!=null && !identiryCode.equalsIgnoreCase(SessionContext.getIdentifyCode(request))){
			return JsonView.render(2);
		}
		try {
			if (null != pictureImg && pictureImg.getBytes().length > 0) {
				String key = QiniuStorage.uploadImage(pictureImg.getBytes());
				authUser.setHeader(key);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		authUser.setCreateTime(new Date());
		AuthUser tmpUser = authUserService.getByUsername(authUser.getUsername());
		if(tmpUser != null){
			return JsonView.render(1);
		}else{
			authUser.setPassword(EncryptUtil.encodedByMD5(authUser.getPassword()));
			authUserService.createSelectivity(authUser);
			return JsonView.render(0);
		}
	}
	
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
	
	/**
	 * ajax登录
	 */
	@RequestMapping(value = "/ajaxlogin")
	@ResponseBody
	public String ajaxLogin(AuthUser user, String identiryCode, Integer rememberMe, HttpServletRequest request){
		//验证码判断
		if(identiryCode!=null && !identiryCode.equalsIgnoreCase(SessionContext.getIdentifyCode(request))){
			return JsonView.render(2, "验证码不正确！");
		}
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),EncryptUtil.encodedByMD5(user.getPassword()));
		try {
			if(rememberMe != null && rememberMe == 1){
				token.setRememberMe(true);
			}
			currentUser.login(token);//shiro：不抛出异常，登陆成功
			return new JsonView().toString();
		}catch(AuthenticationException e){ //登录失败
			return JsonView.render(1, "用户名或密码不正确或者账户已被禁用");
		}
	}
	
	@RequestMapping(value = "/doLogin")
	public ModelAndView doLogin(AuthUser user, String identiryCode, HttpServletRequest request){
		
		//如果已经登录过
		if(SessionContext.getAuthUser() != null){
			return new ModelAndView("redirect:/user/home.html");
		}
		
		//验证码判断
		if(identiryCode!=null && !identiryCode.equalsIgnoreCase(SessionContext.getIdentifyCode(request))){
			ModelAndView mv = new ModelAndView("auth/login");
			mv.addObject("errcode", 1);
			return mv;
		}
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),EncryptUtil.encodedByMD5(user.getPassword()));
		try {
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.login(token);//shiro实现登录
			return new ModelAndView("redirect:/user/home.html");
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
