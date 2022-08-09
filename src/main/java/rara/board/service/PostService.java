package rara.board.service;

import rara.board.Util;
import rara.board.domain.Post;
import rara.board.domain.DelStatus;
import rara.board.repository.PostDto;
import rara.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public Post save(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setWriter(Util.getSessionMember().getId());
        post.setDel(DelStatus.N);
        post.setRegDate(LocalDateTime.now());
        post.setUpdateDate(LocalDateTime.now());
        return postRepository.save(post);
    }

    public Post update(Long id, String title, String content) {
        Optional<Post> findPost = postRepository.findById(id);

        if (findPost.isEmpty()) {
            // todo 예외처리 필요
        }
        Post post = findPost.get();
        post.setTitle(title);
        post.setContent(content);
        post.setUpdateDate(LocalDateTime.now());
        return post;
    }

    public Post delete(Long id) {
        return postRepository.delete(id);
    }
}
