package com.spring.javaProjectS.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.spring.javaProjectS.dao.StudyDAO;
import com.spring.javaProjectS.dao.User2DAO;
import com.spring.javaProjectS.vo.KakaoAddressVO;
import com.spring.javaProjectS.vo.UserVO;

@Service
public class StudyServiceImpl implements StudyService {
	
	@Autowired
	StudyDAO studyDAO;
	
	@Autowired
	User2DAO user2DAO;
	

	@Override
	public String[] getCityStringArray(String dodo) {
		String[] strArray = new String[100];
		
		if(dodo.equals("서울")) {
			strArray[0] = "강남구";
			strArray[1] = "서초구";
			strArray[2] = "관악구";
			strArray[3] = "마포구";
			strArray[4] = "영등포구";
			strArray[5] = "강북구";
			strArray[6] = "동대문구";
			strArray[7] = "성북구";
		}
		else if(dodo.equals("경기")) {
			strArray[0] = "수원시";
			strArray[1] = "안양시";
			strArray[2] = "안성시";
			strArray[3] = "평택시";
			strArray[4] = "용인시";
			strArray[5] = "의정부시";
			strArray[6] = "광명시";
			strArray[7] = "성남시";
		}
		else if(dodo.equals("충북")) {
			strArray[0] = "청주시";
			strArray[1] = "충주시";
			strArray[2] = "괴산군";
			strArray[3] = "제천시";
			strArray[4] = "단양군";
			strArray[5] = "증평군";
			strArray[6] = "옥천군";
			strArray[7] = "영동군";
		}
		else if(dodo.equals("충남")) {
			strArray[0] = "천안시";
			strArray[1] = "아산시";
			strArray[2] = "논산시";
			strArray[3] = "공주시";
			strArray[4] = "부여군";
			strArray[5] = "홍성군";
			strArray[6] = "예산군";
			strArray[7] = "청양군";
		}
		
		return strArray;
	}

	@Override
	public ArrayList<String> getCityArrayList(String dodo) {
		ArrayList<String> vos = new ArrayList<String>();
		
		if(dodo.equals("서울")) {
			vos.add("강남구");
			vos.add("서초구");
			vos.add("관악구");
			vos.add("마포구");
			vos.add("영등포구");
			vos.add("강북구");
			vos.add("동대문구");
			vos.add("성북구");
		}
		else if(dodo.equals("경기")) {
			vos.add("수원시");
			vos.add("안양시");
			vos.add("안성시");
			vos.add("평택시");
			vos.add("용인시");
			vos.add("의정부시");
			vos.add("광명시");
			vos.add("성남시");
		}
		else if(dodo.equals("충북")) {
			vos.add("청주시");
			vos.add("충주시");
			vos.add("괴산군");
			vos.add("제천시");
			vos.add("단양군");
			vos.add("증평군");
			vos.add("옥천군");
			vos.add("영동군");
		}
		else if(dodo.equals("충남")) {
			vos.add("천안시");
			vos.add("아산시");
			vos.add("논산시");
			vos.add("공주시");
			vos.add("부여군");
			vos.add("홍성군");
			vos.add("예산군");
			vos.add("청양군");
		}
		
		return vos;
	}

	@Override
	public UserVO getUserSearch(String mid) {
		return user2DAO.getUserSearchVO(mid);
	}

	@Override
	public List<UserVO> getUser2SearchMid(String mid) {
		return user2DAO.getUser2SearchMid(mid);
	}

	@Override
	public int fileUpload(MultipartFile fName, String mid) {
		int res = 0;
		// 파일 이름에 대한 중복 처리(랜덤으로 문자 발생시킨 후 file 이름에 붙여서 중복 불허 처리하기)
		UUID uid = UUID.randomUUID();
		String oFileName = fName.getOriginalFilename();
		String sFileName = mid + "_" + uid + "_" + oFileName;
		
		// 파일 복사 처리(서버 메모리에 올라와 있는 파일의 정보를 실제 서버 파일시스템에 저장시킨다.)
		try {
			writeFile(fName, sFileName);
			res = 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}

	// 파일 복사를 위한 메소드
	private void writeFile(MultipartFile fName, String sFileName) throws IOException {
		// MultipartFile은 메모리에 있는 파일을 가져오기 위한 선언
		// request 객체를 사용하기 위한 준비
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		// 저장 위치 설정
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/study/"); // realPath 는 webapp
		
		// 클라이언트에서 서버로 http 통신을 통해 저장 / 서버에 저장할 객체는 output, 읽어오는 객체는 input 객체를 사용
		// 만약 ckeditor에 저장된 파일을 읽어서 다른 파일에 저장시키려면 읽기 위해 input을 사용하여 output으로 저장해야 하므로 둘 다 사용
		// 현재는 서버 메모리에 이미 올라와 있는 상태이므로 저장할 객체만 필요하므로 output만 사용
		FileOutputStream fos = new FileOutputStream(realPath + sFileName);  // 예외 처리 후 ioexception으로 변경하고 위에 writefile을 try 처리 해주기 
		// 앞에서 넘어온 파일을 바이널리에서 바이트로 변경 후 넘겨주기
//		fos.write(fName.getBytes());
//		fos.close();
		if((fName.getBytes().length) != -1) {
			fos.write(fName.getBytes());
		}
		fos.flush();
		fos.close();
		
	}

	@Override
	public KakaoAddressVO getKakaoAddressSearch(String address) {
		return studyDAO.getKakaoAddressSearch(address);
	}

	@Override
	public void setKakaoAddressInput(KakaoAddressVO vo) {
		studyDAO.setKakaoAddressInput(vo);
	}

	@Override
	public List<KakaoAddressVO> getKaKaoAddressList() {
		return studyDAO.getKaKaoAddressList();
	}

	@Override
	public void setkakaoAddressDelete(String address) {
		studyDAO.setkakaoAddressDelete(address);
	}
	
	
}
