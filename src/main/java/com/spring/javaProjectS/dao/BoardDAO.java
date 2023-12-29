package com.spring.javaProjectS.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javaProjectS.vo.Board2ReplyVO;
import com.spring.javaProjectS.vo.BoardVO;

public interface BoardDAO {

	public int setBoardInput(@Param("vo") BoardVO vo);

	public BoardVO getBoardContent(@Param("idx") int idx);

	public int getTotRecCnt();

	public List<BoardVO> getBoardList(@Param("startIndexNo") int startIndexNo, @Param("pageSize") int pageSize);

	public int setBoardDelete(@Param("idx") int idx);

	public int setBoardUpdate(@Param("vo") BoardVO vo);

	public BoardVO getPreNextSearch(@Param("idx") int idx, @Param("str") String str);

	public Board2ReplyVO getBoardParentReplyCheck(@Param("boardIdx") int boardIdx);

	public int setBoardReplyInput(@Param("replyVo") Board2ReplyVO replyVo);

	public List<Board2ReplyVO> getBoard2ReplyList(@Param("idx") int idx);

	public void setReplyOrderUpdate(@Param("boardIdx") int boardIdx, @Param("re_order") int re_order);

	public void setReadNumPlus(@Param("idx") int idx);

	public int getTotRecCntSearch(@Param("search") String search, @Param("searchString") String searchString);

	public List<BoardVO> getBoardSearchList(@Param("startIndexNo") int startIndexNo, @Param("pageSize") int pageSize, @Param("search") String search, @Param("searchString") String searchString);


}
