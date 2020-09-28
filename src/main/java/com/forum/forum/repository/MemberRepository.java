package com.forum.forum.repository;
import com.forum.forum.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Member findByUserName(String username) {
        return em.createQuery("select m From Member m where m.username  = :username", Member.class)
                .setParameter("username", username)
                .getSingleResult();
    }

}
