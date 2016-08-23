package com.luke.cms.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luke.cms.model.FooBean;
import com.luke.cms.model.Student;
import com.luke.cms.model.StudentList;
import com.luke.cms.model.fruit.Apple;
import com.luke.cms.model.fruit.Fruit;
import com.luke.cms.model.rspnstatus.Failed;
import com.luke.cms.model.rspnstatus.Result;
import com.luke.cms.model.rspnstatus.Success;
import com.luke.cms.service.FooServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spring 用例
 * 
 * @Component @Service 等默认都是 @Scope("singleton") 
 * 除非特别声明为 @Scope("prototype") 否则都是以单例方式注入
 */
@Controller
@RequestMapping("/test/*")
@Scope("singleton")
@PropertySource("classpath:../backstage.properties")
public class FooController {
	// LogBack 打印对象
	private static final Logger logger = LoggerFactory
			.getLogger(FooController.class);

	private final AtomicLong counter = new AtomicLong();

	@Autowired
	@Qualifier("orange")
	// inject instance which name is "orange" for fruit implement
	private Fruit fruit;

	@Autowired
	private FooServiceImpl fooService;

	@PostConstruct
	public void init() {
		logger.info("'@PostContrust' run once when this Bean's lifecycle start in spring container");
	}

	@PreDestroy
	public void destory() {
		logger.info("'@PreDestroy' run once when this Bean's lifecycle will destory in spring container");
	}
	
	@RequestMapping(value = "/jsp/{favourite}", method = RequestMethod.GET)
	public String jsp(@PathVariable String favourite, ModelMap model) {
		Apple apple = new Apple();
		logger.info("fruit-1 name: " + fruit.getName());
		logger.info("apple-2 name: " + apple.getName());
		logger.info("favourite: " + favourite);
		fruit.setMade("YanTai");

		// model will be used in JSP View
		model.addAttribute("fruit", fruit);
		model.addAttribute("favourite", favourite);
		
		// go to sale-fruit.jsp
		return "/sale/sale-fruit";
	}
	
	@RequestMapping(value = "xml", method = RequestMethod.GET)
	@ResponseBody
	public StudentList resultFormatByAccept() {
		ArrayList<Student> list = new ArrayList<Student>();
		list.add(new Student(3, "Robert", "Parera", "robert@gmail.com",
				"8978767878"));
		list.add(new Student(93, "Andrew", "Strauss",
				"andrew@gmail.com", "8978767878"));
		list.add(new Student(239, "Eddy", "Knight", "knight@gmail.com",
				"7978767878"));
		StudentList sl = new StudentList(list);
		return sl;
	}
	
	@RequestMapping(value = "/json", method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<FooBean> paramChinese(
			@RequestParam(value = "name", required = false, defaultValue = "anonymous") String name, 
			@RequestParam(value = "chinese_name", required = false, defaultValue = "无名") String chinese, ModelMap model) {
		ArrayList<FooBean> list = new ArrayList<FooBean>();
		FooBean foo1 = new FooBean(counter.incrementAndGet(), String.format("English name, %s", name));
		FooBean foo2 = new FooBean(counter.incrementAndGet(), String.format("Chinese name, %s", chinese));
		list.add(foo1);
		list.add(foo2);
		return list;
	}
	
	@RequestMapping(value = "/json/fruit/{fruitName}/chinese/{fruitChinese}", method = RequestMethod.GET)
	@ResponseBody
	public HashMap<String, String> restfulChinese(@PathVariable String fruitName, @PathVariable String fruitChinese) {
		logger.info("chinese name of fruit: " + fruitChinese);
		logger.info("name of fruit: " + fruitName);
		// model will be used in JSP View
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("fruit", fruitName);
		result.put("chinese", fruitChinese);
		return result;
	}
	
	@RequestMapping(value = "/json/post_json", method = RequestMethod.POST)
	@ResponseBody
	public Result handleJsonRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			// Read json string from request.inputStream
			String inputJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			inputJson = reader.readLine();
			System.out.println(inputJson);

			// From json string to map object
			ObjectMapper mapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			HashMap<String, Object> inputData = mapper.readValue(inputJson, HashMap.class);
			return new Success("return receive data", inputData);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Failed("500");
	}
	
	@RequestMapping(value = "/json/form_post", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, String> fromPost(@RequestParam(value = "username", required = true) String username,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
	    System.out.println(request.getCharacterEncoding());
		System.out.println(username);
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("you post username is", username);
		return result;
	}
	
	/* 302 Download */
	@RequestMapping(value = "/download302", method = RequestMethod.GET)
	public String downloadBy302() {
		return "redirect:http://www.baidu.com";
	}

	/* Download CSV */
	@RequestMapping(value = "/downloadCSV", method = RequestMethod.GET)
	public void downloadCSV(HttpServletRequest request,
			HttpServletResponse response) {
		String titles = "\"编号\", \"内容\"\n";
		ArrayList<FooBean> contentList = new ArrayList<FooBean>();
		contentList.add(new FooBean(20, "测试内容1"));
		contentList.add(new FooBean(30, "测试内容,\"2"));
		response.setHeader("Content-Disposition", "attachment;filename=wo.csv");
		response.setHeader("Content-Type", "application/csv");
		try {
			response.getOutputStream().write(
					fooService.toCSV(titles, contentList).getBytes("utf-8"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * only return a black image which is 1x1 pix
	 * 
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/image/tiny", method = RequestMethod.GET)
	public void tinyImage(HttpServletResponse response) throws IOException {
		final byte[] whiteSpot = new byte[] { 71, 73, 70, 56, 57, 97, 1, 0, 1,
				0, -128, -1, 0, -1, -1, -1, 0, 0, 0, 44, 0, 0, 0, 0, 1, 0, 1,
				0, 0, 2, 2, 68, 1, 0, 59 };

		response.setContentType("image/gif");
		response.getOutputStream().write(whiteSpot);
		response.setContentLength(whiteSpot.length);
		response.setStatus(200);
		response.flushBuffer();
	}
	
	@RequestMapping(value = "/transaction/readonly", method = RequestMethod.GET)
	@ResponseBody
	public String testConnectionReadOnly() {
		try {
			// 注意在 persistenceContext.xml 里配置了 get*, find*, query* 都是ReadOnly连接
			fooService.getUpdateUserByReadOnly();
			return "done";
		} catch(Exception e) {
			logger.error("testConnectionReadOnly Error!", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/transaction/rollback", method = RequestMethod.GET)
	@ResponseBody
	public String testTransactionRollback() {
		try {
			// 抛出异常，回滚本次事务对数据库的成功操作
			fooService.addUserWithExceptionInTransaction();
			return "done";
		} catch(Exception e) {
			logger.error("testTransactionRollback Error!", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/transaction/success", method = RequestMethod.GET)
	@ResponseBody
	public String testTransaction() {
		try {
			// 抛出异常，回滚本次事务对数据库的成功操作
			fooService.addUserInTransaction();
			return "done";
		} catch(Exception e) {
			logger.error("testTransactionRollback Error!", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/non-transaction", method = RequestMethod.GET)
	public String testNonTransaction() {
		try {
			// 抛出异常，不回滚对数据库的成功操作
			fooService.xxxAddUserWithoutTransaction();
			return "done";
		} catch(Exception e) {
			logger.error("testNonTransaction Error!", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/cache", method = RequestMethod.GET)
	@ResponseBody
	public String testCache() {
		fooService.testCache("my parameter 1");
		return "test cache finished";
	}
	
	@RequestMapping(value = "/mylogout", method = RequestMethod.GET)
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.authenticate(response))
			request.logout();
		response.sendRedirect(request.getContextPath());
	}
	
	@RequestMapping("/log")
	@ResponseBody
	public String log() {
		logger.trace(">>>trace");
		logger.debug(">>>debug");
		logger.info(">>>info");
		logger.warn(">>>warn");
		logger.error(">>>error");
		return "print log done";
	}
	
	@RequestMapping("/exit")
	public void exit() {
		// @destory注解将被调用
		logger.warn("exit now...");
		System.exit(1);
	}
}