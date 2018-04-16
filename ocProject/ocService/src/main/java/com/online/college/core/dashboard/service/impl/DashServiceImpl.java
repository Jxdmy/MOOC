package com.online.college.core.dashboard.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.college.core.dashboard.dao.DashDao;
import com.online.college.core.dashboard.domain.Dash;
import com.online.college.core.dashboard.service.DashService;

@Service
public class DashServiceImpl implements DashService{

	@Autowired
	private DashDao dashDao;
	public List<Dash> listDash(){
		return dashDao.listDash();
	}
}
