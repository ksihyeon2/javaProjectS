package com.spring.javaProjectS.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.spring.javaProjectS.dao.BoardDAO;
import com.spring.javaProjectS.vo.Board2ReplyVO;
import com.spring.javaProjectS.vo.BoardVO;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardDAO boardDAO;
	
	@Override
	public int setBoardInput(BoardVO vo) {
		return boardDAO.setBoardInput(vo);
	}

	@Override
	public BoardVO getBoardContent(int idx) {
		return boardDAO.getBoardContent(idx);
	}

	@Override
	public List<BoardVO> getBoardList(int startIndexNo, int pageSize) {
		return boardDAO.getBoardList(startIndexNo,pageSize);
	}

	@Override
	public void imgCheck(String content) {
		//				 0				 1				 2				 3				 4				 5
		//         012345678901234567890123456789012345678901234567890
		// <p><img src="/javaProjectS/data/ckeditor/231220123415_헉.jpg" style="height:235px; width:400px"
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
//		최종 저장 위치 설정
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/");
		
		int position = 33;
//		현재 첫 번째 이미지만 변수에 저장
	  String nextImg = content.substring(content.indexOf("src=\"/")+position);
	  
	  boolean sw = true;
	  while(sw) {
	  	String imgFile = nextImg.substring(0,nextImg.indexOf("\""));
	  	
	  	String origFilePath = realPath + "ckeditor/" + imgFile;
	  	String copyFilePath = realPath + "board/" + imgFile;
	  	
	  	fileCopyCheck(origFilePath, copyFilePath);	// ckeditor 폴더의 이미지 파일을 board 폴더 위치로 복사하기 위한 메소드 선언
	  	
//	   남은 이미지 저장
	  	if(nextImg.indexOf("src=\"/") == -1) {
	  		sw = false;
	  	} else {
	  		nextImg = nextImg.substring(nextImg.indexOf("src=\"/")+position);
	  	}
	  }
	}

//	파일을 복사처리 하는 메소드
	private void fileCopyCheck(String origFilePath, String copyFilePath) {
		try {
//			파일을 받아와서 다시 입력해야 하므로 input ouput 둘 다 생성
//			input으로 원래 저장 파일 위치를 가져오기
			FileInputStream fis = new FileInputStream(new File(origFilePath));
//			ouput으로 저장시킬 위치 지정
			FileOutputStream fos = new FileOutputStream(new File(copyFilePath));
			
			byte[]	bytes = new byte[2048];
			
			int cnt = 0;
			while((cnt = fis.read(bytes)) != -1) {
				fos.write(bytes, 0, cnt);
			}
//			파일 정리
			fos.flush();
			fos.close();
			fis.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int setBoardDelete(int idx) {
		return boardDAO.setBoardDelete(idx);
	}

	@Override
	public void imgDelete(String content) {
		//	 			 0				 1				 2				 3				 4				 5
		//         012345678901234567890123456789012345678901234567890
		// <p><img src="/javaProjectS/data/board/231220123415_헉.jpg" style="height:235px; width:400px"
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		//최종 저장 위치 설정
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/");
		
		int position = 30;
		//현재 첫 번째 이미지만 변수에 저장
		String nextImg = content.substring(content.indexOf("src=\"/")+position);
		
		boolean sw = true;
		while(sw) {
			String imgFile = nextImg.substring(0,nextImg.indexOf("\""));
			
			String origFilePath = realPath + "board/" + imgFile;
			
			fileDelete(origFilePath);	// board 폴더의 이미지 파일을 삭제하기 위한 메소드 선언
			
			//남은 이미지 저장
			if(nextImg.indexOf("src=\"/") == -1) {
				sw = false;
			} else {
				nextImg = nextImg.substring(nextImg.indexOf("src=\"/")+position);
			}
		}
	}

//	실제 서버에 저장된 파일을 삭제 처리한다.
	private void fileDelete(String origFilePath) {
		File delFile = new File(origFilePath);
//		exists로 파일이 존재하는지 확인 후 있으면 delete로 파일 삭제 처리
		if(delFile.exists()) {
			delFile.delete();
		}
	}

	@Override
	public void imgBackup(String content) {
		//	 			 0				 1				 2				 3				 4				 5
		//         012345678901234567890123456789012345678901234567890
		// <p><img src="/javaProjectS/data/board/231220123415_헉.jpg" style="height:235px; width:400px"
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/");
		
		int position = 30;
		//현재 첫 번째 이미지만 변수에 저장
		String nextImg = content.substring(content.indexOf("src=\"/")+position);
		
		boolean sw = true;
		while(sw) {
			String imgFile = nextImg.substring(0,nextImg.indexOf("\""));
			
			String origFilePath = realPath + "board/" + imgFile;
			String copyFilePath = realPath + "ckeditor/" + imgFile;
			
			fileCopyCheck(origFilePath, copyFilePath);	// ckeditor 폴더의 이미지 파일을 board 폴더 위치로 복사하기 위한 메소드 선언
			
			//남은 이미지 저장
			if(nextImg.indexOf("src=\"/") == -1) {
				sw = false;
			} else {
				nextImg = nextImg.substring(nextImg.indexOf("src=\"/")+position);
			}
		}
	}

	@Override
	public int setBoardUpdate(BoardVO vo) {
		return boardDAO.setBoardUpdate(vo);
	}

	@Override
	public BoardVO getPreNextSearch(int idx, String str) {
		return boardDAO.getPreNextSearch(idx,str);
	}

	@Override
	public Board2ReplyVO getBoardParentReplyCheck(int boardIdx) {
		return boardDAO.getBoardParentReplyCheck(boardIdx);
	}

	@Override
	public int setBoardReplyInput(Board2ReplyVO replyVo) {
		return boardDAO.setBoardReplyInput(replyVo);
	}

	@Override
	public List<Board2ReplyVO> getBoard2ReplyList(int idx) {
		return boardDAO.getBoard2ReplyList(idx);
	}

	@Override
	public void setReplyOrderUpdate(int boardIdx, int re_order) {
		boardDAO.setReplyOrderUpdate(boardIdx,re_order);
	}

	@Override
	public void setReadNumPlus(int idx) {
		boardDAO.setReadNumPlus(idx);
	}

	@Override
	public List<BoardVO> getBoardSearchList(int startIndexNo, int pageSize, String search, String searchString) {
		return boardDAO.getBoardSearchList(startIndexNo,pageSize,search,searchString);
	}
}
