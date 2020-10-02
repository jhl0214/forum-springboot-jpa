package com.forum.forum.repository;
import com.forum.forum.domain.Member;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Member findByUserName(String username) {
        List<Member> members = em.createQuery("select m From Member m where m.username  = :username", Member.class)
                .setParameter("username", username.toLowerCase())
                .getResultList();
        return members.size() == 0 ? null : members.get(0);
    }

    public Member findByUserNameAndPassword(String username, String password) {
        List<Member> members =  em.createQuery("select m From Member m where m.username  = :username and m.password = :password", Member.class)
                .setParameter("username", username.toLowerCase())
                .setParameter("password", password)
                .getResultList();
        return members.size() == 0 ? null : members.get(0);
    }

}
