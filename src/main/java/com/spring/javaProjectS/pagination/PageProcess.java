package com.spring.javaProjectS.pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javaProjectS.dao.BoardDAO;
import com.spring.javaProjectS.dao.PdsDAO;

@Service
public class PageProcess {

		@Autowired
		BoardDAO boardDAO;
		
		@Autowired
		PdsDAO pdsDAO;
		
//		board인지 guest인지 pds인지 등을 구분하기 위해 String section으로 받아와 DAO처리
//		board 안에서의 구분 (이름인지 제목인지 등)을 위해 part로 받아옴
		public PageVO totRecCnt(int pag, int pageSize, String section, String part, String searchString) {
			PageVO pageVO = new PageVO();
			
			int totRecCnt = 0;
			String search = "";
			
//			section으로 받아온 값으로 해당 카운트 구하기
			if(section.equals("board")) {
				if(part.equals("")) {
					totRecCnt = boardDAO.getTotRecCnt();
				} else {
					search = part;
					totRecCnt = boardDAO.getTotRecCntSearch(search,searchString);
				}
			} else if(section.equals("pds")) {
					totRecCnt = pdsDAO.getTotRecCnt(part);
			}
			
			int totPage = (totRecCnt % pageSize)==0 ? (totRecCnt / pageSize) : (totRecCnt / pageSize) + 1 ;
			int startIndexNo = (pag - 1) * pageSize;
			int curScrStartNo = totRecCnt - startIndexNo;
			
			int blockSize = 3;
			int curBlock = (pag - 1) / blockSize;
			int lastBlock = (totPage - 1) / blockSize;
			
			pageVO.setPag(pag);
			pageVO.setPageSize(pageSize);
			pageVO.setTotRecCnt(totRecCnt);
			pageVO.setTotPage(totPage);
			pageVO.setStartIndexNo(startIndexNo);
			pageVO.setCurScrStartNo(curScrStartNo);
			pageVO.setBlockSize(blockSize);
			pageVO.setCurBlock(curBlock);
			pageVO.setLastBlock(lastBlock);
			pageVO.setPart(part);
//			글쓴이, 제목 등
			pageVO.setSearch(search);
//			해당 글에 대한 내용
			pageVO.setSearchString(searchString);
			
			return pageVO;
		}
}
