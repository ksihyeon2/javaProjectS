package com.spring.javaProjectS.service;

import java.util.List;

import com.spring.javaProjectS.vo.UserVO;

public interface UserService {

	public List<UserVO> getUserList();

	public int setUserDelete(int idx);

	public List<UserVO> getUserSearch(String name);

	public List<UserVO> getUser2List();

	public List<UserVO> getUser2Search(String name);

	public int setUser2Delete(int idx);

	public int setUser2Input(UserVO vo);

	public int setUser2Update(UserVO vo);

}
