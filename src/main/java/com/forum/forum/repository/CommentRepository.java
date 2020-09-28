package com.forum.forum.repository;
import com.forum.forum.domain.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CommentRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    public Comment find(Long id) {
        return em.find(Comment.class, id);
    }

    public List<Comment> findAll() {
        return em.createQuery("select c from Comment c", Comment.class)
                .getResultList();
    }

    public List<Comment> findByPostId(Long postId) {
        return em.createQuery("select c From Comment c where c.post.id  = :postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

}
