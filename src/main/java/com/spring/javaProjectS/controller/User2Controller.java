package com.spring.javaProjectS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.javaProjectS.service.UserService;
import com.spring.javaProjectS.vo.UserVO;

@Controller
@RequestMapping("/user2")
public class User2Controller {

	@Autowired
	UserService userService;
	
	// User 전체 리스트
	@RequestMapping(value = "/user2List", method = RequestMethod.GET)
	public String user2ListGet(Model model) {
		List<UserVO> vos = userService.getUser2List();
		
		model.addAttribute("vos", vos);
		
		return "study/user/user2List";
	}
	
	// User 가입
//	validator를 이용한 Backend 유효성 검사하기, 검사 후 정확한 자료를 DB에 저장시켜준다.
	@RequestMapping(value = "/user2List", method = RequestMethod.POST)
	public String user2ListPost(@Validated UserVO vo, BindingResult bindingResult, Model model) {
		int res = userService.setUser2Input(vo);
		
//		System.out.println("error : " + bindingResult.hasErrors());
		
//		에러가 있으면 전체 에러를 type을 모르기 때문에 validate에서 재공해주는 objectError로 변수에 담는다.
		if(bindingResult.hasFieldErrors()) {
			List<ObjectError> list = bindingResult.getAllErrors();
			
			String temp = "";
			for(ObjectError e : list) {
				temp = e.getDefaultMessage().substring(e.getDefaultMessage().indexOf("/")+1);
				if(temp.equals("midEmpty") || temp.equals("midSizeNo") || temp.equals("nameEmpty") || temp.equals("nameSizeNo") || temp.equals("ageRangeNo")) {
					break;
				}
			}
			System.out.println("temp : " + temp);
			
			model.addAttribute("temp",temp);
			return "redirect:/message/validatorError";
		}
				
		if(res != 0) return "redirect:/message/user2InputOk";
		else return "redirect:/message/user2InputNo";
	}
	
	// User 개별 검색 리스트
	@RequestMapping(value = "/user2Search", method = RequestMethod.GET)
	public String user2SearchGet(Model model, String name) {
		List<UserVO> vos = userService.getUser2Search(name);
		
		model.addAttribute("vos", vos);
		model.addAttribute("name", name);
		
		return "study/user/user2List";
	}
	
	// User 삭제처리
	@RequestMapping(value = "/user2Delete", method = RequestMethod.GET)
	public String user2DeleteGet(Model model, int idx) {
		int res = userService.setUser2Delete(idx);
		
		if(res != 0) return "redirect:/message/user2DeleteOk";
		else return "redirect:/message/user2DeleteNo";
	}
	
	// User 수정처리
	@RequestMapping(value = "/user2Update", method = RequestMethod.POST)
	public String user2DeleteGet(UserVO vo) {
		int res = userService.setUser2Update(vo);
		
		if(res != 0) return "redirect:/message/user2UserUpdateOk";
		else return "redirect:/message/user2UserUpdateNo";
	}
}
