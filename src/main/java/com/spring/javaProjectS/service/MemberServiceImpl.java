package com.spring.javaProjectS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javaProjectS.dao.MemberDAO;
import com.spring.javaProjectS.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	MemberDAO memberDAO;

	@Override
	public MemberVO getMemberIdCheck(String mid) {
		return memberDAO.getMemberIdCheck(mid);
	}

	@Override
	public MemberVO getMemberNickCheck(String nickName) {
		return memberDAO.getMemberNickCheck(nickName);
	}

	@Override
	public int setMemberJoinOk(MemberVO vo) {
		// 사진처리...
		vo.setPhoto("noimage.jpg");
		
		
		return memberDAO.setMemberJoinOk(vo);
	}

	@Override
	public int setUserDel(String mid) {
		return memberDAO.setUserDel(mid);
	}

	@Override
	public int setPwdChangeOk(String mid, String pwd) {
		return memberDAO.setPwdChangeOk(mid, pwd);
	}

	@Override
	public int setMemberUpdateOk(MemberVO vo) {
		return memberDAO.setMemberUpdateOk(vo);
	}

	@Override
	public void setMemberPasswordUpdate(String mid, String pwd) {
		memberDAO.setMemberPasswordUpdate(mid, pwd);
	}

	@Override
	public MemberVO getMemberEmailSearch(String email) {
		return memberDAO.getMemberEmailSearch(email);
	}

	@Override
	public MemberVO getMemberNickNameEmailCheck(String nickName, String email) {
		return memberDAO.getMemberNickNameEmailCheck(nickName,email);
	}

	@Override
	public void setKaokaoMemberInput(String mid, String pwd, String nickName, String email) {
		memberDAO.setKaokaoMemberInput(mid,pwd,nickName,email);
	}
	
}
