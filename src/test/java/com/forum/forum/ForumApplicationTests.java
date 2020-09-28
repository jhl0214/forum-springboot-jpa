package com.forum.forum;

import com.forum.forum.domain.Image;
import com.forum.forum.domain.Member;
import com.forum.forum.domain.Post;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
class ForumApplicationTests {

	@PersistenceContext
	EntityManager em;

	@Test
	public void test() {


	}

}
