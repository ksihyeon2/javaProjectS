package com.spring.javaProjectS.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.spring.javaProjectS.service.MemberService;
import com.spring.javaProjectS.vo.MemberVO;

@Controller
@RequestMapping("/member")
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	JavaMailSender mailSender;
	
	// 회원 로그인폼 보여주기
	@RequestMapping(value = "/memberLogin", method = RequestMethod.GET)
	public String memberLoginGet(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		if(cookies != null) {
			for(int i=0; i<cookies.length; i++) {
				if(cookies[i].getName().equals("cMid")) {
					request.setAttribute("mid", cookies[i].getValue());
					break;
				}
			}
		}
		return "member/memberLogin";
	}
	
	// 회원 로그인 체크
	@RequestMapping(value = "/memberLogin", method = RequestMethod.POST)
	public String memberLoginPost(HttpSession session,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="mid", defaultValue = "hkd1234", required=false) String mid,
			@RequestParam(name="pwd", defaultValue = "1234", required=false) String pwd,
			@RequestParam(name="idSave", defaultValue = "", required=false) String idSave) {
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		if(vo != null && vo.getUserDel().equals("NO") && passwordEncoder.matches(pwd, vo.getPwd())) {
			// 1.세션처리
			String strLevel = "";
			if(vo.getLevel() == 0) strLevel = "관리자";
			else if(vo.getLevel() == 1) strLevel = "우수회원";
			else if(vo.getLevel() == 2) strLevel = "정회원";
			else if(vo.getLevel() == 3) strLevel = "준회원";
			
			session.setAttribute("sMid", mid);
			session.setAttribute("sNickName", vo.getNickName());
			session.setAttribute("sLevel", vo.getLevel());
			session.setAttribute("strLevel", strLevel);
			
			// 2.쿠키저장/삭제
			if(idSave.equals("on")) {
				Cookie cookieMid = new Cookie("cMid", mid);
				//cookieMid.setPath("/");
				cookieMid.setMaxAge(60*60*24*7);
				response.addCookie(cookieMid);
			}
			else {
				Cookie[] cookies = request.getCookies();
				for(int i=0; i<cookies.length; i++) {
					if(cookies[i].getName().equals("cMid")) {
						cookies[i].setMaxAge(0);
						response.addCookie(cookies[i]);
						break;
					}
				}
			}
			return "redirect:/message/memberLoginOk?mid="+mid;
		}
		else {
			return "redirect:/message/memberLoginNo";
		}
		
	}
	
	//카카오로그인 처리
	@RequestMapping(value = "/kakaoLogin", method = RequestMethod.GET)
	public String kakaoLoginGet(HttpSession session,HttpServletRequest request, HttpServletResponse response,
			String nickName, String email, String accessToken) {
		
		session.setAttribute("sAccessToken", accessToken);
		
		// 카카오로그인한 회원이 현재 우리 회원인지를 조사한다.(넘어온 이메일의 @를 기준으로 아이디와 이메일을 분리후 member2테이블의 정보와 비교한다.)
		MemberVO vo = memberService.getMemberNickNameEmailCheck(nickName, email);
		
		// 현재 카카오로그인한 회원이 우리회원이 아니였다면, 자동으로 우리회원에 가입처리한다.(필수입력사항:아이디,닉네임,이메일) - 단, 성명은 '닉네임'과 동일하게 가입처리한다.
		if(vo == null) {
			// 아이디 결정해주기
			String mid = email.substring(0, email.indexOf("@"));
			
			// 만약에 기존에 같은 아이디가 존재한다면 가입처리할수 없도록 한다.
			MemberVO vo2 = memberService.getMemberIdCheck(mid);
			if(vo2 != null) return "redirect:/message/midSameSearch";
			
			// 임시 비밀번호를 발급처리후 메일로 전송처리한다.
			UUID uid = UUID.randomUUID();
			String pwd = uid.toString().substring(0,8);
			session.setAttribute("sImsiPwd", pwd);
			
			// 새로 발급받은 임시비밀번호로 암호화 처리후 DB에 저장처리한다.
			pwd = passwordEncoder.encode(pwd);
			
			// 새로 발급된 임시비밀번호를 메일로 전송한다.
			// 메일 발송은 생략.... 각자 처리해 주세요...
			
			
			// 자동 회원 가입처리(DB에 앞에서 만들어준 값들로 가입처리한다.)
			memberService.setKaokaoMemberInput(mid, pwd, nickName, email);
			
			// 새로 가입처리된 회원의 정보를 다시 vo에 담아준다.
			vo = memberService.getMemberIdCheck(mid);
		}
			
		// 1.세션처리
		String strLevel = "";
		if(vo.getLevel() == 0) strLevel = "관리자";
		else if(vo.getLevel() == 1) strLevel = "우수회원";
		else if(vo.getLevel() == 2) strLevel = "정회원";
		else if(vo.getLevel() == 3) strLevel = "준회원";
		
		session.setAttribute("sMid", vo.getMid());
		session.setAttribute("sNickName", vo.getNickName());
		session.setAttribute("sLevel", vo.getLevel());
		session.setAttribute("strLevel", strLevel);
		
		return "redirect:/message/kakaoLoginOk?mid="+vo.getMid();
	}
	
	// kakao 로그아웃
	@RequestMapping(value = "/kakaoLogout", method = RequestMethod.GET)
	public String kakaoLogoutGet(HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		String accessToken = (String)session.getAttribute("sAccessToken");
		
		String requestUrl = "https://kapi.kakao.com/v1/user/unlink";
		
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		session.invalidate();
		
		return "redirect:/message/kakaoLogout?mid="+mid;
	}
	
	
	// 회원 로그 아웃처리
	@RequestMapping(value = "/memberLogout", method = RequestMethod.GET)
	public String memberLogoutGet(HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		session.invalidate();
		
		return "redirect:/message/memberLogout?mid="+mid;
	}
	
	@RequestMapping(value = "/memberMain", method = RequestMethod.GET)
	public String memberMainGet() {
		
		return "member/memberMain";
	}
	
	@RequestMapping(value = "/memberJoin", method = RequestMethod.GET)
	public String memberJoinGet() {
		return "member/memberJoin";
	}
	
	@RequestMapping(value = "/memberJoin", method = RequestMethod.POST)
	public String memberJoinPost(MemberVO vo) {
		// 아이디/닉네임 중복체크
		if(memberService.getMemberIdCheck(vo.getMid()) != null) return "redirect:/message/idCheckNo";
		if(memberService.getMemberNickCheck(vo.getNickName()) != null) return "redirect:/message/nickCheckNo";
		
		// 비밀번호 암호화
		vo.setPwd(passwordEncoder.encode(vo.getPwd()));
		
		// 회원사진 처리(service객체에서 처리후 DB에 저장한다.)
		int res = memberService.setMemberJoinOk(vo);
		
		if(res == 1) return "redirect:/message/memberJoinOk";
		else return "redirect:/message/memberJoinNo";
	}
	
	@ResponseBody
	@RequestMapping(value = "/memberIdCheck", method = RequestMethod.POST)
	public String memberIdCheckPost(String mid) {
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		if(vo  != null) return "1";
		else return "0";
	}
	
	@ResponseBody
	@RequestMapping(value = "/memberNickCheck", method = RequestMethod.POST)
	public String memberNickCheckPost(String nickName) {
		MemberVO vo = memberService.getMemberNickCheck(nickName);
		
		if(vo  != null) return "1";
		else return "0";
	}
	
	@ResponseBody
	@RequestMapping(value = "/userDel", method = RequestMethod.POST)
	public String userDelPost(HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		int res = memberService.setUserDel(mid);
		
		if(res == 1) {
			session.invalidate();
			return "1";
		}
		else return "0";
	}
	
	@RequestMapping(value = "/memberPwdCheck/{pwdFlag}", method = RequestMethod.GET)
	public String memberPwdCheckGet(@PathVariable String pwdFlag, Model model) {
		model.addAttribute("pwdFlag", pwdFlag);
		return "member/memberPwdCheck";
	}
	
	@ResponseBody
	@RequestMapping(value = "/memberPwdCheck", method = RequestMethod.POST)
	public String memberPwdCheckPost(String pwd, HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		if(passwordEncoder.matches(pwd, vo.getPwd())) return "1";
		else return "0";
	}
	
	@ResponseBody
	@RequestMapping(value = "/memberPwdChangeOk", method = RequestMethod.POST)
	public String memberPwdChangeOkPost(String pwd, HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		pwd = passwordEncoder.encode(pwd);
		int res = memberService.setPwdChangeOk(mid, pwd);
		
		if(res != 0) return "1";
		else return "0";
	}
	
	@RequestMapping(value = "/memberUpdate", method = RequestMethod.GET)
	public String memberUpdateGet(Model model, HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		MemberVO vo = memberService.getMemberIdCheck(mid);
		model.addAttribute("vo", vo);
		return "member/memberUpdateForm";
	}
	
	@RequestMapping(value = "/memberUpdate", method = RequestMethod.POST)
	public String memberUpdatePost(MemberVO vo, HttpSession session) {
		// 닉네임 체크
		String nickName = (String) session.getAttribute("sNickName");
		if(memberService.getMemberNickCheck(vo.getNickName()) != null && !nickName.equals(vo.getNickName())) {
			return "redirect:/message/nickCheckNo";
		}
		int res = memberService.setMemberUpdateOk(vo);
		if(res != 0) {
			session.setAttribute("sNickName", vo.getNickName());
			return "redirect:/message/memberUpdateOk";
		}
		else return "redirect:/member/memberUpdateNo";
	}
	
//	이메일 찾기
	@ResponseBody
	@RequestMapping(value = "/memberEmailSearch", method = RequestMethod.POST)
	public String memberEmailSearchPost(String email, Model model) {
		//System.out.println("email : " + email);
		MemberVO vo = memberService.getMemberEmailSearch(email);
		if(vo != null && vo.getEmail().equals(email)) {
			model.addAttribute("res", vo.getMid());
			return "/member/memberLogin";
		} else {
			return "redirect:/member/memberEmailNo";
		}
	}
	
//비밀번호 찾기
	@ResponseBody
	@RequestMapping(value = "/memberPasswordSearch", method = RequestMethod.POST)
	public String memberPasswordSearchPost(String mid, String email) throws MessagingException {
		MemberVO vo = memberService.getMemberIdCheck(mid);
		if(vo != null && vo.getEmail().equals(email)) {
			// 정보 확인후, 임시비밀번호를 발급받아서 메일로 전송처리한다.
			UUID uid = UUID.randomUUID();
			String pwd = uid.toString().substring(0,8);
			
			// 발급받은 비밀번호를 암호화후 DB에 저장한다.
			memberService.setMemberPasswordUpdate(mid, passwordEncoder.encode(pwd));
			
			// 발급받은 임시번호를 회원 메일주소로 전송처리한다.
			String title = "임시 비밀번호를 발급하셨습니다.";
			String mailFlag = "임시 비밀번호 : " + pwd;
			String res = mailSend(email, title, mailFlag);
			
			if(res == "1") return "1";
		}
		return "0";
	}
	
	//메일 전송하기
	public String mailSend(String toMail, String title, String mailFlag) throws MessagingException {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String content = "";
		// 메일 전송을 위한 객체 : MimeMessage(), MimeMessageHelper()
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		
		// 메일보관함에 회원이 보내온 메세지들의 정보를 모두 저장시킨후 작업처리하자...
		messageHelper.setTo(toMail);
		messageHelper.setSubject(title);
		messageHelper.setText(content);
		
		// 메세지 보관함의 내용(content)에, 발신자의 필요한 정보를 추가로 담아서 전송시켜주면 좋다....
		content = content.replace("\n", "<br>");
		content += "<br><hr><h3>"+mailFlag+"</h3><hr><br>";
		content += "<p><img src=\"cid:main.jpg\" width='500px'></p>";
		content += "<p>방문하기 : <a href='49.142.157.251:9090/cjgreen'>JavaProject</a></p>";
		content += "<hr>";
		messageHelper.setText(content, true);
		
		// 본문에 기재된 그림파일의 경로와 파일명을 별로도 표시한다. 그런후 다시 보관함에 저장한다.
		FileSystemResource file = new FileSystemResource(request.getSession().getServletContext().getRealPath("/resources/images/main.jpg"));
		//FileSystemResource file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javaProjectS\\src\\main\\webapp\\resources\\images\\main.jpg");
		messageHelper.addInline("main.jpg", file);
		
		// 메일 전송하기
		mailSender.send(message);
		
		return "1";
	}
}
