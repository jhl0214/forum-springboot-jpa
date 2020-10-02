package com.forum.forum;

import com.forum.forum.domain.Comment;
import com.forum.forum.domain.Image;
import com.forum.forum.domain.Member;
import com.forum.forum.domain.Post;
import com.forum.forum.dto.CommentDTO;
import com.forum.forum.dto.PostDTO;
import com.forum.forum.repository.CommentRepository;
import com.forum.forum.repository.ImageRepository;
import com.forum.forum.repository.MemberRepository;
import com.forum.forum.repository.PostRepository;
import com.forum.forum.service.CommentService;
import com.forum.forum.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ForumApplicationTests {

	@PersistenceContext
	EntityManager em;
	@Autowired
	MemberRepository memRepo;
	@Autowired
	PostRepository postRepo;
	@Autowired
	CommentRepository comRepo;
	@Autowired
	ImageRepository imgRepo;
	@Autowired
	PostService postService;
	@Autowired
	CommentService comService;

	@Test
	public void testMemRepo() {
		Member member = new Member("name", "username", "pw", "email", "ROLE_USER");
		Member member2 = new Member("name2", "username2", "pw", "email", "ROLE_USER");
		memRepo.save(member);
		memRepo.save(member2);

		Member byId = memRepo.find(member.getId());
		Member byUserName = memRepo.findByUserName(member.getUsername());
		List<Member> all = memRepo.findAll();

		// find member by id
		assertEquals(member, byId);
		// find member by username
		assertEquals(member, byUserName);
		// check if members are added correctly
		assertEquals(2, all.size());

	}

//	@Test
//	public void testPostRepo() {
//		Member member = new Member("name", "username", "pw", "email", "ROLE_USER");
//		Post post = new Post("title", "a", "b", LocalDateTime.now(), LocalDateTime.now());
//		member.addPost(post);
//
//		memRepo.save(member);
//		postRepo.save(post);
//
//		em.flush();
//
//		// Find a post by post id
//		Post byId = postRepo.findById(post.getId()).get();
//
//		// Check if post is added
//		assertEquals(post, byId);
//
//		// Find a post by username
//		List<Post> test = postRepo.findByUserName("username");
//		assertEquals(1, test.size());
//
//		em.remove(byId);
//		byId = postRepo.findById(post.getId()).orElse(null);
//
//		// Check if post is removed
//		assertEquals(null, byId);
//
//	}

//	@Test
//	public void testImgRepo() {
//		Member member = new Member("name", "username", "pw", "email", "ROLE_USER");
//		Post post = new Post("title", "a", "b", LocalDateTime.now(), LocalDateTime.now());
//		member.addPost(post);
//		Image img = new Image("src");
//		post.addImage(img);
//
//		memRepo.save(member);
//		postRepo.save(post);
//
//		em.flush();
//
//		// find img by id
//		Image findImg = imgRepo.find(img.getId());
//		post.getImages().remove(img);
//		em.flush();
//		// find after removing image
//		Image findImg2 = imgRepo.find(img.getId());
//
//		Image img2 = new Image("src2");
//		post.addImage(img2);
//		post.addImage(new Image("src3"));
//		em.flush();
//
//		// check if img is added
//		assertEquals(img, findImg);
//		// check if img is deleted
//		assertEquals(null, findImg2);
//		// check if 2 imgs are added
//		assertEquals(2, imgRepo.findByPostId(post.getId()).size());
//	}

//	@Test
//	public void testPostService() {
//		Member member = new Member("name", "user", "pw", "email", "ROLE_USER");
//
//		Post post = new Post("title", "a", "b", LocalDateTime.now(), LocalDateTime.now());
//
//		member.addPost(post);
//
//		PostDTO dto = new PostDTO();
//		dto.setContent("bb");
//		dto.setTitle("title2");
//		dto.setWriter("aa");
//		dto.setCreatedDateTime(LocalDateTime.now());
//		dto.setModifiedDateTime(LocalDateTime.now());
//
//		memRepo.save(member);
//		postService.addPost(post);
//		em.flush();
//		em.clear();
//
//		Post post2 = postService.findPostById(post.getId());
//		System.out.println("=======================");
//		System.out.println(post2.getTitle());
//
//		postService.updatePost(post.getId(), dto);
//		em.flush();
//		em.clear();
//
//		Post post3 = postService.findPostById(post.getId());
//		System.out.println("=======================");
//		System.out.println(post2.getTitle());
//	}

	@Test
	public void testCommentRepo() {
		Member member = new Member("name", "user", "pw", "email", "ROLE_USER");

		Post post = new Post("title", "a", "b", LocalDateTime.now(), LocalDateTime.now());

		Comment comment = new Comment("comWriter", "comContent", LocalDateTime.now(), LocalDateTime.now());
		Comment comment2 = new Comment("comWriter", "comContent", LocalDateTime.now(), LocalDateTime.now());

		member.addPost(post);
		post.addComment(comment);
		post.addComment(comment2);
		memRepo.save(member);
		postRepo.save(post);
		comRepo.save(comment);
		comRepo.save(comment2);

		Comment findComment = comRepo.find(comment.getId());
		assertEquals(findComment, comment);

		List<Comment> comments = comRepo.findByPostId(post.getId());
		assertEquals(2, comments.size());

		List<Comment> all = comRepo.findAll();
		assertEquals(2, all.size());

		CommentDTO dto = new CommentDTO();
		dto.setContent("update2");
		dto.setWriter("writer");
		dto.setModifiedDateTime(LocalDateTime.now());
		dto.setCreatedDateTime(comment2.getCreatedDateTime());

		comService.updateComment(comment2.getId(), dto);


	}

}
