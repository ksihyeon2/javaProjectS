package com.spring.javaProjectS.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.javaProjectS.service.GuestService;
import com.spring.javaProjectS.vo.GuestVO;

@Controller
@RequestMapping("/guest")
public class GuestController {
	
	@Autowired
	GuestService guestService;

	@RequestMapping(value = "/guestList", method = RequestMethod.GET)
	public String guestListGet(Model model,
			@RequestParam(name="pag", defaultValue="1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue="3", required = false) int pageSize) {
		
		int totRecCnt = guestService.getTotRecCnt();
		int totPage = (totRecCnt % pageSize)==0 ? (totRecCnt / pageSize) : (totRecCnt / pageSize) + 1 ;
		int startIndexNo = (pag - 1) * pageSize;
		int curScrStartNo = totRecCnt - startIndexNo;
		
		int blockSize = 3;
		int curBlock = (pag - 1) / blockSize;
		int lastBlock = (totPage - 1) / blockSize;
		
		List<GuestVO> vos = guestService.guestList(startIndexNo, pageSize);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pag", pag);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totPage", totPage);
		model.addAttribute("curScrStartNo", curScrStartNo);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("curBlock", curBlock);
		model.addAttribute("lastBlock", lastBlock);
		
		return "guest/guestList";
	}
	
	@RequestMapping(value = "/guestInput", method = RequestMethod.GET)
	public String guestInputGet() {
		return "guest/guestInput";
	}
	
	@RequestMapping(value = "/guestInput", method = RequestMethod.POST)
	public String guestInputPost(GuestVO vo) {
		int res = guestService.guestInput(vo);
		
		if(res != 0) return "redirect:/message/guestInputOk";
		else return "redirect:/message/guestInputNo";
	}
	
	@RequestMapping(value = "/adminLogin", method = RequestMethod.GET)
	public String adminLoginGet() {
		return "guest/adminLogin";
	}
	
	@RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
	public String adminLoginPost(HttpSession session,
			@RequestParam(name="mid", defaultValue = "guest", required = false) String mid,
			@RequestParam(name="pwd", defaultValue = "", required = false) String pwd) {
		
		int res = guestService.adminLogin(mid, pwd);
		
		if(res != 0) {
			session.setAttribute("sAdmin", "adminOk");
			return "redirect:/message/adminLoginOk";
		}
		else return "redirect:/message/adminLoginNo";
	}
	
	@RequestMapping(value = "/adminLogout", method = RequestMethod.GET)
	public String adminLogoutGet(HttpSession session) {
		session.invalidate();
		return "redirect:/message/adminLogout";
	}
	
	@RequestMapping(value = "/guestDelete", method = RequestMethod.GET)
	public String guestDeleteGet(int idx) {
		int res = guestService.setGuestDelete(idx);
		
		if(res != 0) return "redirect:/message/guestDeleteOk";
		else return "redirect:/message/guestDeleteNo";
	}
	
}
