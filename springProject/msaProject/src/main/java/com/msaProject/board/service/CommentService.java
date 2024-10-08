package com.msaProject.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msaProject.board.Repository.CommentRepository;
import com.msaProject.board.model.Comment;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // 댓글 조회
    public List<Comment> getCommentsByBoardId(Integer no) {
        return commentRepository.findByNo(no);
    }

    // 댓글 추가
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }
    
    // 댓글 삭제
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
