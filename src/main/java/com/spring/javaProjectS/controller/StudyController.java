package com.spring.javaProjectS.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.javaProjectS.common.ARIAUtil;
import com.spring.javaProjectS.common.SecurityUtil;
import com.spring.javaProjectS.service.StudyService;
import com.spring.javaProjectS.vo.ChartVO;
import com.spring.javaProjectS.vo.KakaoAddressVO;
import com.spring.javaProjectS.vo.MailVO;
import com.spring.javaProjectS.vo.UserVO;

@Controller
@RequestMapping("/study")
public class StudyController {
  
	@Autowired
	StudyService studyService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	JavaMailSender mailSender;
	
	@RequestMapping(value = "/ajax/ajaxForm", method = RequestMethod.GET)
	public String ajaxFormGet() {
		
		return "study/ajax/ajaxForm";
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest1", method = RequestMethod.POST)
	public String ajaxTest1Get(int idx) {
		System.out.println("idx : " + idx);
		
		return idx+"";
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest2", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String ajaxTest2Get(String str) {
		System.out.println("str : " + str);
		
		return str;
	}
	
	@RequestMapping(value = "/ajax/ajaxTest3_1", method = RequestMethod.GET)
	public String ajaxTest3_1Get(String str) {
		return "study/ajax/ajaxTest3_1";
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest3_1", method = RequestMethod.POST)
	public String[] ajaxTest3_1Post(String dodo) {
		//String[] strArray = new String[100];
		//strArray = studyService.getCityStringArray(dodo);
		//return strArray;
		return studyService.getCityStringArray(dodo);
	}
	
	@RequestMapping(value = "/ajax/ajaxTest3_2", method = RequestMethod.GET)
	public String ajaxTest3_2Get() {
		return "study/ajax/ajaxTest3_2";
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest3_2", method = RequestMethod.POST)
	public ArrayList<String> ajaxTest3_2Post(String dodo) {
		return studyService.getCityArrayList(dodo);
	}
	
	@RequestMapping(value = "/ajax/ajaxTest3_3", method = RequestMethod.GET)
	public String ajaxTest3_3Get() {
		return "study/ajax/ajaxTest3_3";
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest3_3", method = RequestMethod.POST)
	public HashMap<Object, Object> ajaxTest3_3Post(String dodo) {
		ArrayList<String> vos = studyService.getCityArrayList(dodo);
		
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		map.put("city", vos);
		
		return map;
	}
	
	@RequestMapping(value = "/ajax/ajaxTest4_1", method = RequestMethod.GET)
	public String ajaxTest4_1Get() {
		return "study/ajax/ajaxTest4_1";
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest4_1", method = RequestMethod.POST)
	public UserVO ajaxTest4_1Post(String mid) {
		return studyService.getUserSearch(mid);
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest4_2", method = RequestMethod.POST)
	public List<UserVO> ajaxTest4_2Post(String mid) {
		return studyService.getUser2SearchMid(mid);
	}

	@RequestMapping(value = "/uuid/uidForm", method = RequestMethod.GET)
	public String uidFormGet() {
		return "study/uuid/uidForm";
	}
	
	@ResponseBody
	@RequestMapping(value = "/uuid/uidForm", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String uidFormPost() {
		UUID uid = UUID.randomUUID();
		return uid.toString();
	}
	
	@RequestMapping(value = "/password/sha256", method = RequestMethod.GET)
	public String sha256Get() {
		return "study/password/sha256";
	}
	
	@ResponseBody
	@RequestMapping(value = "/password/sha256", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String sha256Post(String pwd) {
		UUID uid = UUID.randomUUID();
		String salt = uid.toString().substring(0,8);
		
		SecurityUtil security = new SecurityUtil();
		String encPwd = security.encryptSHA256(pwd+salt);
		
		pwd = "원본 비밀번호 : " + pwd + " / salt키 : " + salt + " / 암호화된 비밀번호 : " + encPwd;
		
		return pwd;
	}
	
	@RequestMapping(value = "/password/aria", method = RequestMethod.GET)
	public String ariaGet() {
		return "study/password/aria";
	}
	
	@ResponseBody
	@RequestMapping(value = "/password/aria", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String ariaPost(String pwd) throws InvalidKeyException, UnsupportedEncodingException {
		UUID uid = UUID.randomUUID();
		String salt = uid.toString().substring(0,8);
		
		String encPwd = "";
		String decPwd = "";
		
		encPwd = ARIAUtil.ariaEncrypt(pwd + salt);
		decPwd = ARIAUtil.ariaDecrypt(encPwd);
		
		pwd = "원본 비밀번호 : " + pwd + "/ salt : " + salt + " / 암호화된 비밀번호 : " + encPwd + " / 복호화된 비밀번호 : " + decPwd;
		
		return pwd;
	}
	
	@RequestMapping(value = "/password/bCryptPassword", method = RequestMethod.GET)
	public String bCryptPasswordGet() {
		return "study/password/bCryptPassword";
	}
	
	// BcryptPasswordEncoder 암호화
	@ResponseBody
	@RequestMapping(value = "/password/bCryptPassword", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String bCryptPasswordPost(String pwd) {
		String encPwd = "";
		encPwd = passwordEncoder.encode(pwd);
		
		pwd = "원본 비밀번호 : " + pwd + "/ 암호화된 비밀번호 : " + encPwd;
		
		return pwd;
	}
	
	// 메일 전송폼 호출
	@RequestMapping(value = "/mail/mail", method = RequestMethod.GET)
	public String mailGet() {
		return "study/mail/mailForm";
	}
	
	// 메일 전송하기
	@RequestMapping(value = "/mail/mail", method = RequestMethod.POST)
	public String mailPost(MailVO vo, HttpServletRequest request) throws MessagingException {
		String toMail = vo.getToMail();
		String title = vo.getTitle();
		String content = vo.getContent();
		
		// 메일 전송을 위한 객체 : MimeMessage(), MimeMessageHelper()
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		
		// 메일보관함에 회원이 보내온 메세지들의 정보를 모두 저장시킨후 작업처리하자...
		messageHelper.setTo(toMail);
		messageHelper.setSubject(title);
		messageHelper.setText(content);
		
		// 메세지 보관함의 내용(content)에, 발신자의 필요한 정보를 추가로 담아서 전송시켜주면 좋다....
		content = content.replace("\n", "<br>");
		content += "<br><hr><h3>JavaProjectS 보냅니다.</h3><hr><br>";
		content += "<p><img src=\"cid:main.jpg\" width='500px'></p>";
		content += "<p>방문하기 : <a href='49.142.157.251:9090/cjgreen'>JavaProject</a></p>";
		content += "<hr>";
		messageHelper.setText(content, true);
		
		// 본문에 기재된 그림파일의 경로와 파일명을 별로도 표시한다. 그런후 다시 보관함에 저장한다.
		//FileSystemResource file = new FileSystemResource("D:\\JavaProject\\springframework\\works\\javaProjectS\\src\\main\\webapp\\resources\\images\\main.jpg");
		//request.getSession().getServletContext().getRealPath("");
		FileSystemResource file = new FileSystemResource(request.getSession().getServletContext().getRealPath("/resources/images/main.jpg"));
		messageHelper.addInline("main.jpg", file);
		
		// 첨부파일 보내기
		file = new FileSystemResource(request.getSession().getServletContext().getRealPath("/resources/images/chicago.jpg"));
		messageHelper.addAttachment("chicago.jpg", file);
		
		file = new FileSystemResource(request.getSession().getServletContext().getRealPath("/resources/images/main.zip"));
		messageHelper.addAttachment("main.zip", file);
		
		
		// 메일 전송하기
		mailSender.send(message);
		
		return "redirect:/message/mailSendOk";
	}
	
	@RequestMapping(value = "/fileUpload/fileUpload", method = RequestMethod.GET)
	public String fileUploadGet(HttpServletRequest request, Model model) {
//		경로 뒤에 /를 생략하면 폴더로 보고 /를 생략하지 않으면 파일로 간주
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/study");
		
//		마지막 list는 realPath로 설정한 폴더를 파일로 간주하겠다는 의미
		String[] files = new File(realPath).list();
		
		model.addAttribute("files", files);
		model.addAttribute("fileCount", files.length);
		
		return "study/fileUpload/fileUpload";
	}
	
	@RequestMapping(value = "/fileUpload/fileUpload", method = RequestMethod.POST)
	public String fileUploadPost(MultipartFile fName, String mid) {
		// 서비스 객체를 통해 파일 업로드 시키기
		int res = studyService.fileUpload(fName, mid);
		
		//return "study/fileUpload/fileUpload";
		if(res != 0) {
			return "redirect:/message/fileUploadOk";
		} else {
			return "redirect:/message/fileUploadNo";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/fileUpload/fileDelete", method = RequestMethod.POST)
	public String fileDeletePost(HttpServletRequest request,
			@RequestParam(name="file", defaultValue = "", required = false) String fName) {
		int res = 0;
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/study/");
		
		File file = new File(realPath + fName);
		
		// exists로 파일의 존재 유무 확인
		if(file.exists()) {
			file.delete();
			res = 1;
		}
		
		return res+"";
	}
	
	// 파일 다운로드
	@RequestMapping(value = "/fileUpload/fileDownAction", method = RequestMethod.GET)
	public void fileDownActionGet(HttpServletRequest request, String file, HttpServletResponse response) throws IOException {
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/study/");
		
		File downFile = new File(realPath + file);
		
		//한글처리 윈도우 8859_1에서 넘어오는 방식을 UTF-8로 한글 처리 한다는 의미
		String downFileName = new String(file.getBytes("UTF-8"), "8859_1");
		response.setHeader("Content-Disposition", "attachment:filename=" + downFileName); // 첫 번째, 두 번째(첨부) 예약어임
		
		FileInputStream fis = new FileInputStream(downFile);
		
		ServletOutputStream sos = response.getOutputStream();
		
		// 2KByte씩 저장
		byte[] bytes = new byte[2048];
		int data = 0;
		while((data = fis.read(bytes, 0, bytes.length)) != -1) {
			sos.write(bytes,0,data);
		}
		sos.flush();
		sos.close();
		fis.close();
	}
	
//	API 연습
	@RequestMapping(value = "/kakao/kakaomap", method = RequestMethod.GET)
	public String kakaomapGet() {
		return "study/kakao/kakaomap";
	}
	
//	마커 생성
	@RequestMapping(value = "/kakao/kakaoEx1", method = RequestMethod.GET)
	public String kakaoEx1Get() {
		return "study/kakao/kakaoEx1";
	}
	
//	마커 새성 후 DB 저장
	@ResponseBody
	@RequestMapping(value = "/kakao/kakaoEx1", method = RequestMethod.POST)
	public String kakaoEx1Post(KakaoAddressVO vo) {
		KakaoAddressVO searchVO = studyService.getKakaoAddressSearch(vo.getAddress());
		if(searchVO != null) {
			return "0";
		} else {
			studyService.setKakaoAddressInput(vo);
			return "1";
		}
	}
	
//	MyDB에 저장된 자료 목록 가져오기, 지점 검색 추가
	@RequestMapping(value = "/kakao/kakaoEx2", method = RequestMethod.GET)
	public String kakaoEx2Get(Model model,KakaoAddressVO vo,
			@RequestParam(name="address", defaultValue = "", required = false) String address) {
		List<KakaoAddressVO> vos = studyService.getKaKaoAddressList();
		if(address.equals("")) {
			vo.setLatitude(36.63508151255993);
			vo.setLongtitude(127.45951825211571);
		} else {
			vo = studyService.getKakaoAddressSearch(address);
		}
		
		model.addAttribute("vos",vos);
		model.addAttribute("vo",vo);
		return "study/kakao/kakaoEx2";
	}

//	MyDB에 저장된 주소 삭제
	@ResponseBody
	@RequestMapping(value = "/kakao/kakaoAddressDelete", method = RequestMethod.POST)
	public void kakaoAddressDeletePost(@RequestParam(name="address", defaultValue = "", required = false) String address) {
		studyService.setkakaoAddressDelete(address);
	}
	
	@RequestMapping(value = "/kakao/kakaoEx3", method = RequestMethod.GET)
	public String kakaoEx3Get(Model model,
			@RequestParam(name="address", defaultValue = "청주시청", required = false) String address) {
		
		model.addAttribute("address",address);
		return "/study/kakao/kakaoEx3";
	}
	
	
//	차트 연습
	@RequestMapping(value = "/chart/chart", method = RequestMethod.GET)
	public String chartGet(Model model,
			@RequestParam(name="part", defaultValue = "", required = false) String part) {
		model.addAttribute("part",part);
		return "study/chart/chart";
	}
	
//	차트 보여주기
	@RequestMapping(value = "/chart2/chart", method = RequestMethod.GET)
	public String chart2Get(Model model, ChartVO vo) {
		model.addAttribute("vo", vo);
		return "study/chart2/chart";
	}
	
	@RequestMapping(value = "/chart2/chart", method = RequestMethod.POST)
	public String chart2Post(Model model, ChartVO vo) {
		model.addAttribute("vo", vo);
		return "study/chart2/chart";
	}
	
//	randomAlphaNumeric : 알파벳과 숫자를 랜덤하게 출력 폼
	@RequestMapping(value = "/captcha/randomAlphaNumeric", method = RequestMethod.GET)
	public String randomAlphaNumericGet() {
		return "study/captcha/randomAlphaNumeric";
	}
	
//	randomAlphaNumeric : 알파벳과 숫자를 랜덤하게 출력 처리
//	라이브러리 추가 후 사용
	@ResponseBody
	@RequestMapping(value = "/captcha/randomAlphaNumeric", method = RequestMethod.POST)
	public String randomAlphaNumericPost() {
		String res = RandomStringUtils.randomAlphabetic(64);
		
		return res;
	}
	
// 캡차 : 사람과 기계 구별하기 폼
	@RequestMapping(value = "/captcha/captcha", method = RequestMethod.GET)
	public String chptchaGet() {
		return "study/captcha/captcha";
	}
	
// 캡차 이미지 만들기
	//@ResponseBody
	//@RequestMapping(value = "/captcha/captchaImage", method = RequestMethod.POST)
	@RequestMapping(value = "/captcha/captchaImage", method = RequestMethod.GET)
	public String captchaImageGet(HttpSession session, HttpServletRequest request, Model model) {
		// 시스템에 설정된 폰트 출력해보기
//		Font[] fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts(); // 윈도우 폰트 가져오기
//		for(Font f : fontList) {
//			System.out.println(f.getName());
//		}
		
		try {
			// 알파뉴메릭문자 5개를 가져온다.
			String randomString = RandomStringUtils.randomAlphanumeric(5);
			System.out.println("randomString" + randomString);
			session.setAttribute("sCaptcha", randomString);
			
			Font font = new Font("Jokerman",Font.ITALIC,30); // jokerman 폰트 사용
			FontRenderContext frc = new FontRenderContext(null, true, true);
			Rectangle2D bounds = font.getStringBounds(randomString, frc);
			int w = (int) bounds.getWidth();
			int h = (int) bounds.getHeight();
			
			// 이미지로 생성하기
			BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB); // 폭, 높이, 색상
			Graphics2D g = image.createGraphics();
			
			g.fillRect(0, 0, w, h);
			g.setColor(new Color(0,156,240)); // rgb 색상
			g.setFont(font);  // 그래픽 안에 들어갈 폰트 삽입
			// 각종 랜더링 명령어에 의한 captcha 문자 작업(생략)
			g.drawString(randomString, (float)bounds.getX(), (float)-bounds.getY());
			g.dispose(); // 마감
			
			String realPath = request.getSession().getServletContext().getRealPath("/resources/data/study/");
			int temp = (int)(Math.random()*5) + 1;
			String captchaImage = "captcha" + temp + ".png";
			//ImageIO.write(image, "png", new File(realPath + "captcha.png"));
			ImageIO.write(image, "png", new File(realPath + captchaImage));
			model.addAttribute("captchaImage",captchaImage);
					
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "study/captcha/captcha";
	}
	
	@ResponseBody
	@RequestMapping(value = "/captcha/captcha", method = RequestMethod.POST)
	public String captchaPost(HttpSession session, String strCaptcha) {
		if(strCaptcha.equals(session.getAttribute("sCaptcha").toString())) {
			return "1";
		} else {
			return "0";
		}
	}
	
}
