package com.forum.forum.repository;
import com.forum.forum.domain.Image;
import com.forum.forum.domain.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ImageRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Image image) {
        em.persist(image);
    }

    public Image find(Long id) {
        return em.find(Image.class, id);
    }

    public List<Image> findAll() {
        return em.createQuery("select i from Image i", Image.class)
                .getResultList();
    }

    public List<Image> findByPostId(Long postId) {
        return em.createQuery("select i From Image i where i.post.id = :postId", Image.class)
                .setParameter("postId", postId)
                .getResultList();
    }

}
