package com.spring.javaProjectS.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/errorPage")
public class ErrorController {
	
	// 에러 연습폼
	@RequestMapping(value = "/errorMain", method = RequestMethod.GET)
	public String errorMainGet() {
		return "errorPage/errorMain";
	}
	
	// JSP 페이지에서 에러 발생시 이동처리 할 폼 보여주기
	@RequestMapping(value = "/error1", method = RequestMethod.GET)
	public String error1Get() {
		return "errorPage/error1";
	}
	
	// JSP 페이지에서 에러 발생시 공사중 화면으로 보내어 보여주는 폼(jsp 파일에서 처리하게 되면 이곳에서는 처리할 필요가 없다.)
	@RequestMapping(value = "/errorMessage1", method = RequestMethod.GET)
	public String errorMessage1Get() {
		return "errorPage/errorMessage1";
	}
	
	@RequestMapping(value = "/error404", method = RequestMethod.GET)
	public String error404Get() {
		return"errorPage/error404";
	}
	
	@RequestMapping(value = "/errorMessage1Get", method = RequestMethod.POST)
	public String errorMessage1GetPost() {
		return "errorPage/errorMessage1Get";
	}
	
	@RequestMapping(value = "/error405", method = RequestMethod.GET)
	public String error405Get() {
		return"errorPage/error405";
	}
	
	@RequestMapping(value = "/error500Check", method = RequestMethod.GET)
	public String error500CheckGet(HttpSession session) {
		String mid = (String) session.getAttribute("ssMid");
		int su = 100 + Integer.parseInt(mid);
		System.out.println("su : " + su);
		return"errorPage/errorMessage1";
	}
	
	@RequestMapping(value = "/errorNumberFormat", method = RequestMethod.GET)
	public String errorNumberFormatGet() {
		return"errorPage/errorNumberFormat";
	}
	
	@RequestMapping(value = "/errorNullPointerCheck", method = RequestMethod.GET)
	/*public String errorNullPointerCheckGet(@RequestParam(name = "name", defaultValue = "", required = false) String name) {*/
	public String errorNullPointerCheckGet(String name) {
		System.out.println("name : " + name);
		if(name.equals("admin")) {
			return"";
		} else {
			return "errorPage/errorMain";
		}
	}
	
	@RequestMapping(value = "/errorNullPointer", method = RequestMethod.GET)
	public String errorNullPointerGet() {
		return"errorPage/errorNullPointer";
	}
}
