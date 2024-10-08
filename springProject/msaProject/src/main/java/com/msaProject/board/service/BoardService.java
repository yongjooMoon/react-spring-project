package com.msaProject.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.msaProject.board.Repository.BoardLikeRepository;
import com.msaProject.board.Repository.BoardRepository;
import com.msaProject.core.exception.ResourceNotFoundException;
import com.msaProject.board.model.Board;
import com.msaProject.board.model.Like;
import com.msaProject.core.util.PagingUtil;

@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private BoardLikeRepository boardLikeRepository;
	
	// get boards data
	public List<Board> getAllBoard() {
		return boardRepository.findAll();
	}
	
	public int findAllCount() {
		return (int) boardRepository.count();
	}
	
	public Page<Board> getBoards(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return boardRepository.findAll(pageable);
    }
	
	// 게시판 페이징 조회
	public ResponseEntity<Map<String, Object>> getPagingBoard(Integer p_num) {
		Map<String, Object> result = new HashMap<>();
		
		PagingUtil pu = new PagingUtil(p_num, 5, 5); // ($1:표시할 현재 페이지, $2:한페이지에 표시할 글 수, $3:한 페이지에 표시할 페이지 버튼의 수 )
		Page<Board> page = getBoards(pu.getObjectStartNum(), pu.getObjectCountPerPage());
		List<Board> list = page.getContent();
		pu.setObjectCountTotal(findAllCount());
		pu.setCalcForPaging();
				
		if (list == null || list.size() == 0) {
			return null;
		}
		
		result.put("pagingData", pu);
		result.put("list", list);
		
		return ResponseEntity.ok(result);
	}	
	
	// 게시판 생성
	public Board createBoard(Board board) {
		return boardRepository.save(board);
	}
	
	// 게시글 가져오기
	public ResponseEntity<Board> getBoard(Integer no) {
		Board board = boardRepository.findById(no)
				.orElseThrow(() -> new ResourceNotFoundException("Not exist Board Data by no : ["+no+"]"));

		board.setCounts(board.getCounts() + 1);
		return ResponseEntity.ok(board);
	}

	// 게시글 수정
	public ResponseEntity<Board> updateBoard(
			Integer no, Board updatedBoard) {
		Board board = boardRepository.findById(no)
				.orElseThrow(() -> new ResourceNotFoundException("Not exist Board Data by no : ["+no+"]"));
		board.setType(updatedBoard.getType());
		board.setTitle(updatedBoard.getTitle());
		board.setContents(updatedBoard.getContents());
		
		Board endUpdatedBoard = boardRepository.save(board);
		return ResponseEntity.ok(endUpdatedBoard);
	}
	
	// 게시글 삭제
	public ResponseEntity<Map<String, Boolean>> deleteBoard(
			Integer no) {
		// 게시판 삭제
		Board board = boardRepository.findById(no)
				.orElseThrow(() -> new ResourceNotFoundException("Not exist Board Data by no : ["+no+"]"));
		
		boardRepository.delete(board);
		
		Like like = new Like();
		like.setNo(board.getNo());
		like.setId(board.getMemberNo());
		
		// 좋아요 삭제
		boardLikeRepository.deleteByNoAndId(like.getNo(), like.getId());
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("Deleted Board Data by id : ["+no+"]", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
	
	// 게시글 좋아요
	public ResponseEntity<Map<String, Boolean>> likeClick(Like updateLike){
		Board board = boardRepository.findById(updateLike.getNo())
				.orElseThrow(() -> new ResourceNotFoundException("Not exist Board Data by no : ["+updateLike.getNo()+"]"));
		
		if(updateLike.getIsLiked()) {
			boardLikeRepository.save(updateLike);
			board.setLikes(board.getLikes() + 1);
		}else {
			Like like = boardLikeRepository.findByNoAndId(updateLike.getNo(), updateLike.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Not exist Board Data by no and id : ["+updateLike.getNo()+" , " + updateLike.getId() + "]"));
			
			boardLikeRepository.delete(like);
			board.setLikes(board.getLikes() - 1);
		}
		Map<String, Boolean> response = new HashMap<>();
		response.put("성공", Boolean.TRUE);
		
		return ResponseEntity.ok(response);
	}
	
	// 좋아요 누른 게시글 정보 가져오기
	public ResponseEntity<Boolean> getLike(Like updateLike){
		
		Optional<Like> like = boardLikeRepository.findByNoAndId(updateLike.getNo(), updateLike.getId());
		
		if (like.isPresent()) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
	}
	
}