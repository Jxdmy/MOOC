package com.online.college.service;

import java.io.File;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.online.college.common.storage.QiniuStorage;
import com.online.college.common.storage.ThumbModel;
import com.online.college.common.util.CommonUtil;

public class QiNiuTest extends TestCase {
	Logger log = Logger.getLogger(AppTest.class);
	
	public void testImages() {
		//测试上传图片
		byte[] buff1 = CommonUtil.getFileBytes(new File("F://成为高级的android工程师.jpeg"));
		byte[] buff2 = CommonUtil.getFileBytes(new File("F://开发跨平台的教育系统.jpeg"));
		byte[] buff3 = CommonUtil.getFileBytes(new File("F://ReactJS实战.jpeg"));
		byte[] buff4 = CommonUtil.getFileBytes(new File("F://AngularJS模仿拉钩网.jpeg"));
		String key1 = QiniuStorage.uploadImage(buff1);
		String key2 = QiniuStorage.uploadImage(buff2);
		String key3 = QiniuStorage.uploadImage(buff3);
		String key4 = QiniuStorage.uploadImage(buff4);
		System.out.println("key1 = " + key1);
		System.out.println("key2 = " + key2);
		System.out.println("key3 = " + key3);
		System.out.println("key4 = " + key4);
		
//		String key = "/default/all/0/4c14d4563d0e4e7f9e2a0a834c4d47fa.jpeg";
//		//测试下载图片
//		String url = QiniuStorage.getUrl(key);
//		System.out.println("url = " + url);
		
//		//测试下载不同大小的图片
//		url = QiniuStorage.getUrl(key,ThumbModel.THUMB_256);
//		System.out.println("url = " + url);
		
	}
}

