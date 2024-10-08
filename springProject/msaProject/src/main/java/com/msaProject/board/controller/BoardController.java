package com.msaProject.board.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msaProject.board.model.Board;
import com.msaProject.board.model.Like;
import com.msaProject.board.service.BoardService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class BoardController {
	
	@Autowired
	private BoardService boardService;

	@GetMapping("/board")
	public ResponseEntity<Map<String, Object>> getAllBoards(@RequestParam(value = "p_num", required=false) Integer p_num) {
		if (p_num == null || p_num <= 0) p_num = 1;
		
		return boardService.getPagingBoard(p_num);
	}
	
	@PostMapping("/board")
	public Board createBoard(@RequestBody Board board) {
		return boardService.createBoard(board);
	}
	
	@GetMapping("/board/{no}")
	public ResponseEntity<Board> getBoardByNo(
			@PathVariable("no") Integer no) {
		
		return boardService.getBoard(no);
	}
	
	@PutMapping("/board/{no}")
	public ResponseEntity<Board> updateBoardByNo(
			@PathVariable("no") Integer no, @RequestBody Board board){
		
		return boardService.updateBoard(no, board);
	}
	
	@DeleteMapping("/board/{no}")
	public ResponseEntity<Map<String, Boolean>> deleteBoardByNo(
			@PathVariable("no") Integer no) {
		
		return boardService.deleteBoard(no);
	}
	
	@PostMapping("/board/like")
	public ResponseEntity<Map<String, Boolean>> likeClick(@RequestBody Like like){
		
		return boardService.likeClick(like);
	}
	
	@PostMapping("/board/getLike")
	public ResponseEntity<Boolean> getLike(@RequestBody Like like){
		
		return boardService.getLike(like);
	}
}