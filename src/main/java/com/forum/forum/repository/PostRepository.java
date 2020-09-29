package com.forum.forum.repository;
import com.forum.forum.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p From Post p where p.member.username  = ?1")
    List<Post> findByUserName(String username);

    Page<Post> findByMemberUsername(String username, Pageable pageable);

}
