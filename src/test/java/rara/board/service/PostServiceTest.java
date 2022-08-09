package rara.board.service;

import rara.board.domain.Post;
import rara.board.domain.DelStatus;
import rara.board.repository.PostDto;
import rara.board.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
@Rollback(value = false)
class PostServiceTest {

    @Autowired EntityManager em;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostService postService;

    Post savedPost;

    @BeforeEach
    void beforeEach() {
        PostDto post = new PostDto();
        post.setTitle("제목");
        post.setContent("내용");
        post.setWriter("소라");

        savedPost = postService.save(post);
    }

    @Test
    void save() {
        //given
        PostDto post = new PostDto();
        post.setTitle("제목");
        post.setContent("내용");
        post.setWriter("체리");

        //when
        Post savedPost = postService.save(post);

        //then
        assertThat(savedPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(savedPost.getContent()).isEqualTo(post.getContent());
        assertThat(savedPost.getWriter()).isEqualTo(post.getWriter());

//        log.info("board = {}", savedPost);

    }

    @Test
    void update() {
        //when
        Post updatePost = postService.update(savedPost.getId(), "제목2", "내용2");

        log.info("update post = {}", updatePost.toString());

        //then
        assertThat(updatePost.getTitle()).isEqualTo("제목2");
        assertThat(updatePost.getContent()).isEqualTo("내용2");

    }

    @Test
    void delete() {
        //when
        Post deletePost = postService.delete(savedPost.getId());

        //then
        assertThat(deletePost.getDel()).isEqualTo(DelStatus.Y);

    }
}