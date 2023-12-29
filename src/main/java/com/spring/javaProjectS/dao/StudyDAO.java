package com.spring.javaProjectS.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javaProjectS.vo.KakaoAddressVO;

public interface StudyDAO {

	public KakaoAddressVO getKakaoAddressSearch(@Param("address") String address);

	public void setKakaoAddressInput(@Param("vo") KakaoAddressVO vo);

	public List<KakaoAddressVO> getKaKaoAddressList();

	public void setkakaoAddressDelete(@Param("address") String address);

}
