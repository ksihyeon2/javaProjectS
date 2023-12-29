package com.spring.javaProjectS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javaProjectS.pagination.PageProcess;
import com.spring.javaProjectS.pagination.PageVO;
import com.spring.javaProjectS.service.BoardService;
import com.spring.javaProjectS.vo.Board2ReplyVO;
import com.spring.javaProjectS.vo.BoardVO;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	PageProcess pageProcess;
	
	@RequestMapping(value = "/boardList", method = RequestMethod.GET)
	public String boardListGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize) {
		
		PageVO pageVO = pageProcess.totRecCnt(pag, pageSize, "board", "", "");
		
		List<BoardVO> vos = boardService.getBoardList(pageVO.getStartIndexNo(),pageSize);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVO", pageVO);
		
		return "board/boardList";
	}
	
	@RequestMapping(value = "/boardInput", method = RequestMethod.GET)
	public String boardInputGet() {
		return "board/boardInput";
	}
	
	@RequestMapping(value = "/boardInput", method = RequestMethod.POST)
	public String boardInputPost(BoardVO vo) {
//		content에 이미지가 저장되어 있다면, 저장된 이미지만 골라서 /resources/data/board/폴더에 저장시켜준다.
		if(vo.getContent().indexOf("src=\"/") != -1) {
			boardService.imgCheck(vo.getContent());
		}
		
	// 이미지들의 모든 복사작업을 마치면, ckeditor폴더경로를 borad폴더 경로로 변경처리한다.('/data/ckeditor/' ==> 'data/board/')
		vo.setContent(vo.getContent().replace("/data/ckeditor/", "/data/board/"));
		
		// content안의 내용정리가 끝나면 변경된 vo를 DB에 저장시켜준다.
		int res = boardService.setBoardInput(vo);
		
		if(res == 1) return "redirect:/message/boardInputOk";
		else return "redirect:/message/boardInputNo";
	}
	
	@RequestMapping(value = "/boardContent", method = RequestMethod.GET)
	public String boardContentGet(int idx, Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize) {
//		조회수 증가하기
		boardService.setReadNumPlus(idx);

		BoardVO vo = boardService.getBoardContent(idx);
		
//		이전/다음 글 가져오기
		BoardVO preVo = boardService.getPreNextSearch(idx,"preVo");
		BoardVO nextVo = boardService.getPreNextSearch(idx,"nextVo");
		model.addAttribute("preVo",preVo);
		model.addAttribute("nextVo",nextVo);
		
		model.addAttribute("vo", vo);
		model.addAttribute("pag", pag);
		model.addAttribute("pageSize", pageSize);
		
//		댓글(대댓글) 추가로 처리
		List<Board2ReplyVO> replyVos = boardService.getBoard2ReplyList(idx);
		model.addAttribute("replyVos", replyVos);
		
		return "board/boardContent";
	}
	
	@RequestMapping(value = "/boardDelete", method = RequestMethod.GET)
	public String boardDeleteGet(int idx,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize) {
//		게시글에 사진이 존재한다면 서버에 저장된 사진파일을 먼저 삭제시킨다.
		BoardVO vo = boardService.getBoardContent(idx);
		if(vo.getContent().indexOf("src=\"/") != -1) {
			boardService.imgDelete(vo.getContent());
		}
		
//		앞의 작업을 마치면, DB에서 실제로 존재하는 게시글을 삭제처리 한다.
		int res = boardService.setBoardDelete(idx);
		
		if(res != 1) {
			return "redirect:/message/boardDeleteNo?idx="+idx+"&pag="+pag+"&pagSize="+pageSize;
		} else {
			return "redirect:/message/boardDeleteOk?idx="+idx+"&pag="+pag+"&pagSize="+pageSize;
		}
	}
	
	@RequestMapping(value = "/boardUpdate", method = RequestMethod.GET)
	public String boardUpdateGet(Model model, int idx,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize) {
//		수정 할 원본 자료에 그림 파일이 존재한다면, 현재 폴더(board)의 그림 파일을 ckeditor폴더로 복사 시킨다.
		BoardVO vo = boardService.getBoardContent(idx);
		if(vo.getContent().indexOf("src=\"/") != -1) {
			boardService.imgBackup(vo.getContent());
		}
		
		model.addAttribute("vo", vo);
		model.addAttribute("pag", pag);
		model.addAttribute("pageSize", pageSize);
		
		return "board/boardUpdate";
	}
	
	@RequestMapping(value = "/boardUpdate", method = RequestMethod.POST)
	public String boardUpdatePost(Model model, BoardVO vo,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize) {
//		수정 된 자료가 원본 자료와 완전히 동일하다면 수정 할 필요가 없다. 즉, DB에 저장 된 원본 자료를 불러와서 현재 vo에 담긴 내용(content)과 비교
		BoardVO origVo = boardService.getBoardContent(vo.getIdx());
		
//		content의 내용이 조금이라도 변경이 되었다면 내용을 수정한 것이기에, 그림파일의 처리 유무를 결정
		if(!origVo.getContent().equals(vo.getContent())) {
//		1. 실제로 수정하기 버튼을 클릭하면, 기존 board 폴더의 이미지 파일이 존재했다면 모두 삭제처리(원본은 수정창 들어오기 전에 ckeditor 폴더에 복사시켜둔다.)
			if(origVo.getContent().indexOf("src=\"/") != -1) {
				boardService.imgDelete(origVo.getContent());
			}
//		2. 그림 파일의 경로를 'board'에서 'ckeditor' 경로로 변경
			vo.setContent(vo.getContent().replace("/data/board/", "/data/ckeditor/"));
			
//		앞의 작업이 끝나면 파일을 처음 업로드 한 것과 동일한 작업을 처리시켜준다.
//		즉, content에 이미지가 저장되어 있다면, 저장된 이미지만 골라서 '/data/board/' 폴더에 다시 저장 시켜준다.
			boardService.imgCheck(vo.getContent());
			
// 		이미지들의 모든 복사작업을 마치면, ckeditor폴더경로를 borad폴더 경로로 변경처리한다.('/data/ckeditor/' ==> 'data/board/')
			vo.setContent(vo.getContent().replace("/data/ckeditor/", "/data/board/"));
		}
		
//		content 내용과 이미지 파일까지 잘 정비된 vo를 DB에 Update 시켜준다.
		int res = boardService.setBoardUpdate(vo);
		
		
		model.addAttribute("idx", vo.getIdx());
		model.addAttribute("pag", pag);
		model.addAttribute("pageSize", pageSize);
		
		if(res != 1) {
			return "redirect:/message/boardUpdateNo";
		} else {
			return "redirect:/message/boardUpdateOk";
		}
	}
	
//	부모 댓글 입력처리(원본 글에 대한 댓글)
	@ResponseBody
	@RequestMapping(value = "/boardReplyInput", method = RequestMethod.POST)
	public String boardReplyInputPost(Board2ReplyVO replyVo) {
//		부모 댓글인 경우 re_step = 0, re_order = 1로 처리(단, 원본글의 첫 번째 댓글은 re_order 가 1이지만, 새로 댓글이 생기게 되면 그 이전의 댓글은 re_order +1씩 처리 시켜준다.
		Board2ReplyVO replyParentVO = boardService.getBoardParentReplyCheck(replyVo.getBoardIdx());
		
		if(replyParentVO == null) {
			replyVo.setRe_order(1);
		} else {
			replyVo.setRe_order(replyParentVO.getRe_order()+1);
		}
		replyVo.setRe_step(0);
		
		int res = boardService.setBoardReplyInput(replyVo);
		
		return res+"";
	}
	
//	답글 입력 처리
	@ResponseBody
	@RequestMapping(value = "/boardReplyInputRe", method = RequestMethod.POST)
	public String boardReplyInputRePost(Board2ReplyVO replyVo) {
//		답변글일 경우는 re_step은 부모의 re_step+1, re_order는 부모의 re_order보다 큰 댓글은 모두 +1 시킨 후 자신의 re_order+1 처리한다.
		// 1. 답글의 re_step을 부모의 re_step에 +1 처리
		replyVo.setRe_step(replyVo.getRe_step()+1);
		
		// 2. 부모 이외의 댓글 re_order에 +1 처리
		boardService.setReplyOrderUpdate(replyVo.getBoardIdx(), replyVo.getRe_order());
		
		// 3. 답글의 re_order를 +1 처리
		replyVo.setRe_order(replyVo.getRe_order()+1);

		int res = boardService.setBoardReplyInput(replyVo);
		
		return res+"";
	}
	
//	검색기
	@RequestMapping(value = "/boardSearch", method = RequestMethod.GET)
	public String boardSearchGet(String search, Model model,
			@RequestParam(name="searchString", defaultValue = "", required = false) String searchString,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
		  @RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize) {
		PageVO pageVO = pageProcess.totRecCnt(pag, pageSize, "board", search, searchString);
		//System.out.println("pageVO : " + pageVO);
		
		List<BoardVO> vos = boardService.getBoardSearchList(pageVO.getStartIndexNo(), pageSize, search, searchString);
		
		String searchTitle = "";
		if(pageVO.getSearch().equals("title")) searchTitle = "글제목";
		else if(pageVO.getSearch().equals("name")) searchTitle = "글쓴이";
		else searchTitle = "글내용";
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVO", pageVO);
		model.addAttribute("searchTitle", searchTitle);
		model.addAttribute("searchCount", vos.size());
		
		return "board/boardSearchList";
	}
}
