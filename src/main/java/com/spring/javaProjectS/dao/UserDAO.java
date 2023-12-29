package com.spring.javaProjectS.dao;

import java.util.List;

import com.spring.javaProjectS.vo.UserVO;

public interface UserDAO {

	public List<UserVO> getUserList();

	public int setUserDelete(int idx);

	public List<UserVO> getUserSearch(String name);


}
