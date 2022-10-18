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

}