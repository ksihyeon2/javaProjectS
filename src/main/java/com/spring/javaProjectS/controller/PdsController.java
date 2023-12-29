package com.spring.javaProjectS.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spring.javaProjectS.common.SecurityUtil;
import com.spring.javaProjectS.pagination.PageProcess;
import com.spring.javaProjectS.pagination.PageVO;
import com.spring.javaProjectS.service.PdsService;
import com.spring.javaProjectS.vo.PdsVO;

@Controller
@RequestMapping("/pds")
public class PdsController {

	@Autowired
	PdsService pdsService;
	
	@Autowired
	PageProcess pageProcess;
	
	@RequestMapping(value = "/pdsList", method = RequestMethod.GET)
	public String pdsListGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(name="part", defaultValue = "전체", required = false) String part) {
		PageVO pageVO = pageProcess.totRecCnt(pag, pageSize, "pds", part, "");
		
		List<PdsVO> vos = pdsService.getPdsList(pageVO.getStartIndexNo(), pageSize, part);
		
		model.addAttribute("vos",vos);
		model.addAttribute("pageVO", pageVO);
		return "pds/pdsList";
	}
	
	@RequestMapping(value = "/pdsInput", method = RequestMethod.GET)
	public String pdsInputGet(Model model, String part) {
		model.addAttribute("part",part);
		return "pds/pdsInput";
	}
	
	@RequestMapping(value = "/pdsInput", method = RequestMethod.POST)
//	여러개의 파일을 등록했을 대 MultipartHttpServletRequest를 사용 
	public String pdsInputPost(PdsVO vo, MultipartHttpServletRequest file) {
		SecurityUtil security = new SecurityUtil();
		UUID uid = UUID.randomUUID();
		String salt = uid.toString().substring(0,4);
		
		String pwd = salt + security.encryptSHA256(vo.getPwd());
		vo.setPwd(pwd);
		
		int res = pdsService.setPdsInput(vo, file);
		
		if(res == 1) {
			return "redirect:/message/pdsInputOk";
		} else {
			return "redirect:/message/pdsInputNo";
		}
				
	}
	
	@ResponseBody
	@RequestMapping(value = "/pdsDownNumCheck", method = RequestMethod.POST)
	public String pdsDownNumCheckPost(int idx){
		int res = pdsService.setPdsDownNumCheck(idx);
		return res+"";
	}
	
	@ResponseBody
	@RequestMapping(value = "/pdsDeleteOk", method = RequestMethod.POST)
	public String pdsDeleteOkPost(int idx, String pwd){
	  PdsVO vo = pdsService.getPdsSearch(idx);
	  // String tempPwd = vo.getPwd().substring(0,4);
	  int res = 0;
	  SecurityUtil security = new SecurityUtil();
	  if(security.encryptSHA256(pwd).equals(vo.getPwd().substring(4))) {
	  	res = pdsService.setPdsDelete(vo);
	  } 
	  
	  return res + "";
	}
	
	
//	전체 다운로드 (zip)
	@RequestMapping(value = "/pdsTotalDown", method = RequestMethod.GET)
	public String pdsTotalDownGet(HttpServletRequest request,int idx) throws IOException {
		// 파일 다운로드 횟수 증가
		pdsService.setPdsDownNumCheck(idx);
		
		// 여러개의 파일을 하나의 파일(zip)로 압축(통합)하여 다운로드 시켜준다. 압축파일의 이름을 '제목.zip'으로 처리한다.
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/pds/");
		
		PdsVO vo = pdsService.getPdsSearch(idx);
		
		String[] fNames = vo.getFName().split("/");
		String[] fSNames = vo.getFSName().split("/");
		
		// 압축 경로 설정
		String zipPath = realPath + "temp/";
		// 압축 이름 설정
		String zipName = vo.getTitle() + ".zip";
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath + zipName));
		
		byte[] bytes = new byte[2048];
		
		for(int i=0; i<fNames.length; i++) {
			fis = new FileInputStream(realPath + fSNames[i]);
			fos = new FileOutputStream(zipPath + fNames[i]);
			File moveAndRename = new File(zipPath + fNames[i]);
			
			// fis을 fos에 쓰기작업(파일 생성)
			int data;
			while((data = fis.read(bytes, 0, bytes.length)) != -1) {
				fos.write(bytes, 0, data);
			}
			fos.flush();
			fos.close();
			fis.close();
			
			// fos으로 생성된 파일을 zip파일에 쓰기작업처리
			fis = new FileInputStream(moveAndRename);
			zout.putNextEntry(new ZipEntry(fNames[i]));
			while((data = fis.read(bytes, 0, bytes.length)) != -1) {
				zout.write(bytes, 0, data);
			}
			zout.flush();
			// close로 닫게 되면 전체가 다 닫히기 때문에 closeEntry로 한 개의 객체만 닫기
			zout.closeEntry();
			fis.close();
		}
		zout.close();
		
//		한글을 넘길때는 한글처리 하고 넘겨야 함
		return "redirect:/pds/pdsDownAction?file="+java.net.URLEncoder.encode(zipName);
	}
	
	@RequestMapping(value = "/pdsDownAction", method = RequestMethod.GET)
	public void pdsDownActionGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String file = request.getParameter("file") == null ? "" : request.getParameter("file");
		
		String downFilePath = request.getSession().getServletContext().getRealPath("/resources/data/pds/temp/") + file;
		
		File downFile = new File(downFilePath);
		String downFileName = new String(file.getBytes("UTF-8"), "8859_1");
		response.setHeader("Content-Disposition","attachment;filename=" + downFileName);
		
		FileInputStream fis = new FileInputStream(downFile);
		ServletOutputStream sos = response.getOutputStream();
		
		byte[] bytes = new byte[2048];
		
		int data;
		while((data = fis.read(bytes, 0, bytes.length)) != -1) {
			sos.write(bytes, 0, data);
		}
		sos.flush();
		sos.close();
		fis.close();
		
		// 다운로드 완료 후에 전송이 끝난 zip파일을 삭제처리
		downFile.delete();
	}
}
