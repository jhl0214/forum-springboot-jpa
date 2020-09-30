package com.forum.forum.service;

import com.forum.forum.domain.Comment;
import com.forum.forum.dto.CommentDTO;
import com.forum.forum.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * Add comment
     */
    @Transactional
    public Long addComment(Comment comment) {
        commentRepository.save(comment);
        return comment.getId();
    }

    /**
     * Update comment
     */
    @Transactional
    public void updateComment(Long commentId, CommentDTO commentDto) {
        Comment comment = commentRepository.find(commentId);
        comment.updateComment(comment.getId(), commentDto);
    }

    /**
     * Get comments
     */
    public Comment findCommentById(Long commentId) {
        return commentRepository.find(commentId);
    }

    public List<Comment> findCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> findAllComments() {
        return commentRepository.findAll();
    }

}
