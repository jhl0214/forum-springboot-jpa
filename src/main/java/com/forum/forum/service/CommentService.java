package com.forum.forum.service;

import com.forum.forum.domain.Comment;
import com.forum.forum.domain.Post;
import com.forum.forum.dto.CommentDTO;
import com.forum.forum.repository.CommentRepository;
import com.forum.forum.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /**
     * Add comment
     */
    @Transactional
    public Long addComment(Long postId, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId).get();
        Comment comment = Comment.builder()
                            .writer(commentDTO.getWriter())
                            .content(commentDTO.getContent())
                            .createdDateTime(LocalDateTime.now())
                            .modifiedDateTime(LocalDateTime.now())
                            .build();

        post.addComment(comment);

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
     * Delete comment
     */
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.delete(commentId);
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
