package com.online.college.opt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.online.college.common.web.SessionContext;
import com.online.college.core.dashboard.domain.Dash;
import com.online.college.core.dashboard.service.DashService;

/**
 * 后台管理
 */
@Controller
@RequestMapping()
public class CmsController {
	@Autowired
	private DashService dashService;
	/**
	 * 首页
	 */
	@RequestMapping("/index")
	public ModelAndView index(){
		if(SessionContext.isLogin()){
			ModelAndView mv = new ModelAndView("cms/index");
			// 折线图数据(统计今年每个月的销售总额)
			List<Integer> lineValues = new ArrayList<Integer>();
			List<Dash> list = dashService.listDash();

			// 遍历出所有数据放折线图map
			Map<Integer, Integer> lineMap = new HashMap<Integer, Integer>();
			for (int j = 0; j < list.size(); j++) {
				lineMap.put(list.get(j).getMonth(),list.get(j).getAmount());
			}
			// 遍历十二个月的数据
			for (int i = 1; i <= 12; i++) {
				// 如果数据库中此月份有数据则用数据库数据，否则数据为0
				if (null != lineMap.get(i)) {
					lineValues.add(lineMap.get(i));
				} else {
					lineValues.add(0);
				}
			}
			mv.addObject("lineValues",lineValues);
			mv.addObject("curNav", "home");
			return mv;
		}else{
			return new ModelAndView("auth/login");
		}
	}
	
}

