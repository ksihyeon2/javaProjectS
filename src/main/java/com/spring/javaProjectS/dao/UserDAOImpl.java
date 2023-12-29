package com.spring.javaProjectS.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.javaProjectS.vo.UserVO;

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

	@Autowired
	SqlSession sqlSession;

	@Override
	public List<UserVO> getUserList() {
		List<UserVO> vos = sqlSession.selectList("userNS.getUserList");
		return vos;
	}

	@Override
	public int setUserDelete(int idx) {
		return sqlSession.delete("userNS.setUserDelete", idx);
	}

	@Override
	public List<UserVO> getUserSearch(String name) {
		List<UserVO> vos = sqlSession.selectList("userNS.getUserSearch", name);
		return vos;
	}
	
}
